package ac.dnd.mour.server.common.config;

import ac.dnd.mour.server.file.domain.service.BucketFileNameGenerator;
import ac.dnd.mour.server.schedule.domain.service.UnrecordedStandardDefiner;
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
        final LocalDate now = LocalDate.now();
        return () -> LocalDate.of(now.getYear() + 1, 1, 20);
    }
}
