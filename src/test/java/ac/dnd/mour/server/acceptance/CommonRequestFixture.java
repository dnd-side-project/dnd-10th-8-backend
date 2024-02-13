package ac.dnd.mour.server.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static ac.dnd.mour.server.auth.domain.model.AuthToken.REFRESH_TOKEN_HEADER;
import static ac.dnd.mour.server.auth.domain.model.AuthToken.TOKEN_TYPE;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.ContentType.MULTIPART;

public class CommonRequestFixture {
    public static ValidatableResponse getRequest(final String uri) {
        return request(given -> given
                .contentType(JSON)
                .when()
                .get(uri)
        );
    }

    public static ValidatableResponse getRequestWithAccessToken(final String uri, final String accessToken) {
        return request(given -> given
                .contentType(JSON)
                .auth().oauth2(accessToken)
                .when()
                .get(uri)
        );
    }

    public static ValidatableResponse postRequest(final String uri) {
        return request(given -> given
                .contentType(JSON)
                .when()
                .post(uri)
        );
    }

    public static ValidatableResponse postRequest(final String url, final Object body) {
        return request(given -> given
                .contentType(JSON)
                .body(body)
                .when()
                .post(url)
        );
    }

    public static ValidatableResponse postRequestWithAccessToken(final String uri, final String accessToken) {
        return request(given -> given
                .contentType(JSON)
                .auth().oauth2(accessToken)
                .when()
                .post(uri)
        );
    }

    public static ValidatableResponse postRequestWithRefreshToken(final String uri, final String refreshToken) {
        return request(given -> given
                .contentType(JSON)
                .header(REFRESH_TOKEN_HEADER, String.join(" ", TOKEN_TYPE, refreshToken))
                .when()
                .post(uri)
        );
    }

    public static ValidatableResponse postRequestWithAccessToken(final String uri, final Object body, final String accessToken) {
        return request(given -> given
                .contentType(JSON)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .post(uri)
        );
    }

    public static ValidatableResponse multipartRequest(final String url, final String fileName) {
        return request(given -> given
                .contentType(MULTIPART)
                .multiPart("file", getFile(fileName))
                .when()
                .post(url)
        );
    }

    public static ValidatableResponse multipartRequest(final String uri, final Map<String, String> params) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .request();
        params.keySet().forEach(paramKey -> request.multiPart(paramKey, params.get(paramKey)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final String fileName,
            final Map<String, String> params
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .multiPart("file", getFile(fileName))
                .request();
        params.keySet().forEach(paramKey -> request.multiPart(paramKey, params.get(paramKey)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final String fileName,
            final String accessToken
    ) {
        return request(given -> given
                .contentType(MULTIPART)
                .auth().oauth2(accessToken)
                .multiPart("file", getFile(fileName))
                .when()
                .post(uri)
        );
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final Map<String, String> params,
            final String accessToken
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .auth().oauth2(accessToken)
                .request();
        params.keySet().forEach(paramKey -> request.multiPart(paramKey, params.get(paramKey)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final String fileName,
            final Map<String, String> params,
            final String accessToken
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .auth().oauth2(accessToken)
                .multiPart("file", getFile(fileName))
                .request();
        params.keySet().forEach(paramKey -> request.multiPart(paramKey, params.get(paramKey)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final List<String> fileNames
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .request();
        fileNames.forEach(fileName -> request.multiPart("files", getFile(fileName)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final List<String> fileNames,
            final Map<String, String> params
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .request();
        fileNames.forEach(fileName -> request.multiPart("files", getFile(fileName)));
        params.keySet().forEach(paramKey -> request.multiPart(paramKey, params.get(paramKey)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final List<String> fileNames,
            final String accessToken
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .auth().oauth2(accessToken)
                .request();
        fileNames.forEach(fileName -> request.multiPart("files", getFile(fileName)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse multipartRequest(
            final String uri,
            final List<String> fileNames,
            final Map<String, String> params,
            final String accessToken
    ) {
        final RequestSpecification request = RestAssured.given().log().all()
                .contentType(MULTIPART)
                .auth().oauth2(accessToken)
                .request();
        fileNames.forEach(fileName -> request.multiPart("files", getFile(fileName)));
        params.keySet().forEach(paramKey -> request.multiPart(paramKey, params.get(paramKey)));

        return request.post(uri)
                .then().log().all();
    }

    public static ValidatableResponse patchRequest(final String uri) {
        return request(given -> given
                .contentType(JSON)
                .when()
                .patch(uri)
        );
    }

    public static ValidatableResponse patchRequestWithAccessToken(final String uri, final String accessToken) {
        return request(given -> given
                .contentType(JSON)
                .auth().oauth2(accessToken)
                .when()
                .patch(uri)
        );
    }

    public static ValidatableResponse patchRequestWithAccessToken(
            final String uri,
            final Object body,
            final String accessToken
    ) {
        return request(given -> given
                .contentType(JSON)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .patch(uri)
        );
    }

    public static ValidatableResponse deleteRequest(final String uri) {
        return request(given -> given
                .contentType(JSON)
                .when()
                .delete(uri)
        );
    }

    public static ValidatableResponse deleteRequestWithAccessToken(final String uri, final String accessToken) {
        return request(given -> given
                .contentType(JSON)
                .auth().oauth2(accessToken)
                .when()
                .delete(uri)
        );
    }

    private static ValidatableResponse request(final Function<RequestSpecification, Response> function) {
        final RequestSpecification given = RestAssured.given().log().all();
        final Response response = function.apply(given);
        return response.then().log().all();
    }

    private static File getFile(final String fileName) {
        final String BASE_PATH = "files/";
        try {
            return new ClassPathResource(BASE_PATH + fileName).getFile();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
