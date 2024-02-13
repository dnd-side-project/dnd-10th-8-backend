package ac.dnd.mour.server.file.domain.model;

public record PresignedUrlDetails(
        String preSignedUrl,
        String uploadFileUrl
) {
}
