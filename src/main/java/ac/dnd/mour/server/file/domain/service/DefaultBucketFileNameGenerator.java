package ac.dnd.mour.server.file.domain.service;

import ac.dnd.mour.server.file.domain.model.FileExtension;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultBucketFileNameGenerator implements BucketFileNameGenerator {
    @Override
    public String get(final String fileName) {
        final FileExtension extension = FileExtension.getExtensionViaFimeName(fileName);
        return UUID.randomUUID() + extension.getValue();
    }
}
