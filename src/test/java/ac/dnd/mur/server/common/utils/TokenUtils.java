package ac.dnd.mur.server.common.utils;

import static ac.dnd.mur.server.auth.domain.model.AuthToken.TOKEN_TYPE;

public class TokenUtils {
    public static final String ACCESS_TOKEN = "ACCESS-TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH-TOKEN";

    public static String applyAccessToken() {
        return String.join(" ", TOKEN_TYPE, ACCESS_TOKEN);
    }

    public static String applyRefreshToken() {
        return String.join(" ", TOKEN_TYPE, REFRESH_TOKEN);
    }
}
