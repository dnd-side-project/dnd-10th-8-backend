package ac.dnd.mur.server.file.domain.service;

@FunctionalInterface
public interface BucketFileNameGenerator {
    String get(final String fileName);
}
