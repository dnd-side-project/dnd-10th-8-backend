package ac.dnd.mur.server.common.config;

import ac.dnd.mur.server.file.domain.service.BucketFileNameGenerator;
import ac.dnd.mur.server.schedule.domain.service.UnrecordedStandardDefiner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;

@TestConfiguration
public class BlackboxLogicControlConfig {
    public static final String BUCKET_UPLOAD_PREFIX = "bucket-upload-";

    @Bean
    @Primary
    public BucketFileNameGenerator bucketFileNameGenerator() {
        return fileName -> BUCKET_UPLOAD_PREFIX + fileName;
    }

    @Bean
    @Primary
    public UnrecordedStandardDefiner unrecordedStandardCreator() {
        return () -> LocalDate.of(2024, 1, 20);
    }
}
