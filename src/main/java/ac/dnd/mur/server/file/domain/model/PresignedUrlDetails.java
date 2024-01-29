package ac.dnd.mur.server.file.domain.model;

public record PresignedUrlDetails(
        String preSignedUrl,
        String uploadFileUrl
) {
}
