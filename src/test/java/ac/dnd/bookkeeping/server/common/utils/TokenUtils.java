package ac.dnd.bookkeeping.server.common.utils;

import jakarta.servlet.http.Cookie;

public class TokenUtils {
    public static final String BEARER_TOKEN = "Bearer";
    public static final String ID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjc3OTM3MjI0LCJleHAiOjE2Nzc5NDQ0MjR9.t61tw4gDEBuXBn_DnCwiPIDaI-KcN9Zkn3QJSEK7fag";
    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZXMiOlsiTUVOVE9SIiwiTUVOVEVFIl0sImlhdCI6MTcwMzc0NjI1NywiZXhwIjozMjgwNTQ2MjU3fQ.hJaFVG4SgXNoVs_6GB6yoPSTr7WO8n30LD0RSgmcyPY";
    public static final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNzAzNzQ2MjU3LCJleHAiOjMyODA1NDYyNTd9.64LBOtAZIjO4FMpJoxfQTSBLp5azilb7wjk0hpa1Pz8";
    public static final long EXPIRES_IN = 3000;

    public static String applyAccessToken() {
        return String.join(" ", BEARER_TOKEN, ACCESS_TOKEN);
    }

    /**
     * TODO 안드로이드랑 회의 후 RefreshToken 핸들링 방식 정하고 결정
     */
    public static Cookie applyRefreshToken() {
        return new Cookie("추후 RefreshToken 관련 핸들링 협의 후 정의", REFRESH_TOKEN);
    }
}
