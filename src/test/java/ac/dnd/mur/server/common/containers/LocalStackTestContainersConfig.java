package ac.dnd.mur.server.common.containers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketCannedACL;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@TestConfiguration
public class LocalStackTestContainersConfig {
    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        final LocalStackContainer container = new LocalStackContainer(LOCALSTACK_IMAGE)
                .withServices(LocalStackContainer.Service.S3);
        System.setProperty("spring.cloud.aws.region.static", container.getRegion());
        System.setProperty("spring.cloud.aws.credentials.access-key", container.getAccessKey());
        System.setProperty("spring.cloud.aws.credentials.secret-key", container.getSecretKey());
        System.setProperty("spring.cloud.aws.s3.bucket", "mur-upload");
        return container;
    }

    @Bean
    public S3Client s3Client(final LocalStackContainer container) {
        final S3Client s3Client = S3Client
                .builder()
                .endpointOverride(container.getEndpoint())
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        container.getAccessKey(),
                        container.getSecretKey()
                )))
                .region(Region.of(container.getRegion()))
                .build();

        s3Client.createBucket(CreateBucketRequest.builder()
                .acl(BucketCannedACL.PUBLIC_READ)
                .bucket("mur-upload")
                .build());

        return s3Client;
    }
}
