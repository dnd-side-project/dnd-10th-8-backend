package ac.dnd.mur.server.auth.application.adapter;

public interface TokenStore {
    void synchronizeRefreshToken(final Long memberId, final String refreshToken);

    void updateRefreshToken(final long memberId, final String newRefreshToken);

    void deleteRefreshToken(final long memberId);

    boolean isMemberRefreshToken(final long memberId, final String refreshToken);
}
