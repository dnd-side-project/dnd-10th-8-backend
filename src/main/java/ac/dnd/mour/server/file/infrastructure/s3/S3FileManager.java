package ac.dnd.mour.server.file.infrastructure.s3;

import ac.dnd.mour.server.file.application.adapter.FileManager;
import ac.dnd.mour.server.file.domain.model.PresignedFileData;
import ac.dnd.mour.server.file.domain.model.PresignedUrlDetails;
import ac.dnd.mour.server.file.domain.model.RawFileData;
import ac.dnd.mour.server.file.domain.service.BucketFileNameGenerator;
import ac.dnd.mour.server.file.infrastructure.BucketPath;
import ac.dnd.mour.server.global.exception.GlobalException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

import static ac.dnd.mour.server.file.infrastructure.BucketPath.MEMBER_PROFILE;
import static ac.dnd.mour.server.global.exception.GlobalExceptionCode.UNEXPECTED_SERVER_ERROR;

@Slf4j
@Component
public class S3FileManager implements FileManager {
    private final S3Template s3Template;
    private final BucketFileNameGenerator bucketFileNameGenerator;
    private final String bucket;

    public S3FileManager(
            final S3Template s3Template,
            final BucketFileNameGenerator bucketFileNameGenerator,
            @Value("${spring.cloud.aws.s3.bucket}") final String bucket
    ) {
        this.s3Template = s3Template;
        this.bucketFileNameGenerator = bucketFileNameGenerator;
        this.bucket = bucket;
    }

    @Override
    public PresignedUrlDetails createPresignedUrl(final PresignedFileData file) {
        final String uploadFileName = bucketFileNameGenerator.get(file.fileName());
        final URL preSignedUrl = s3Template.createSignedPutURL(
                bucket,
                MEMBER_PROFILE.completePath(uploadFileName),
                Duration.ofMinutes(5)
        );
        return new PresignedUrlDetails(
                preSignedUrl.toString(),
                createUploadUrlPrefix(preSignedUrl, MEMBER_PROFILE, uploadFileName)
        );
    }

    private String createUploadUrlPrefix(
            final URL preSignedUrl,
            final BucketPath path,
            final String uploadFileName
    ) {
        return preSignedUrl.getProtocol()
                + "://" + preSignedUrl.getHost()
                + "/" + path.getPath()
                + "/" + uploadFileName;
    }

    @Override
    public String upload(final RawFileData file) {
        try (final InputStream inputStream = file.content()) {
            final ObjectMetadata objectMetadata = ObjectMetadata.builder()
                    .contentType(file.contenType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            final String uploadFileName = bucketFileNameGenerator.get(file.fileName());

            return s3Template.upload(bucket, uploadFileName, inputStream, objectMetadata)
                    .getURL()
                    .toString();
        } catch (final IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage(), e);
            throw new GlobalException(UNEXPECTED_SERVER_ERROR);
        }
    }
}
