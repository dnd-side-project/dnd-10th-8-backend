package ac.dnd.mur.server.common.utils;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;

import java.util.Arrays;
import java.util.stream.Stream;

import static ac.dnd.mur.server.auth.domain.model.AuthToken.ACCESS_TOKEN_HEADER;
import static ac.dnd.mur.server.auth.domain.model.AuthToken.REFRESH_TOKEN_HEADER;
import static ac.dnd.mur.server.common.utils.RestDocsSpecificationUtils.SnippetFactory.header;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;

public class RestDocsSpecificationUtils {
    public static RestDocumentationResultHandler successDocs(final String identifier, final Snippet... snippets) {
        return document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                snippets
        );
    }

    public static RestDocumentationResultHandler successDocsWithAccessToken(final String identifier, final Snippet... snippets) {
        return document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                Stream.concat(Arrays.stream(new Snippet[]{getHeaderWithAccessToken()}), Arrays.stream(snippets)).toArray(Snippet[]::new)
        );
    }

    public static RestDocumentationResultHandler successDocsWithRefreshToken(final String identifier, final Snippet... snippets) {
        return document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                Stream.concat(Arrays.stream(new Snippet[]{getHeaderWithRefreshToken()}), Arrays.stream(snippets)).toArray(Snippet[]::new)
        );
    }

    public static RestDocumentationResultHandler failureDocs(final String identifier, final Snippet... snippets) {
        return document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                Stream.concat(Arrays.stream(new Snippet[]{getExceptionResponseFields()}), Arrays.stream(snippets)).toArray(Snippet[]::new)
        );
    }

    public static RestDocumentationResultHandler failureDocsWithAccessToken(final String identifier, final Snippet... snippets) {
        return document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                Stream.concat(
                        Arrays.stream(new Snippet[]{getHeaderWithAccessToken(), getExceptionResponseFields()}),
                        Arrays.stream(snippets)
                ).toArray(Snippet[]::new)
        );
    }

    public static RestDocumentationResultHandler failureDocsWithRefreshToken(final String identifier, final Snippet... snippets) {
        return document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                Stream.concat(
                        Arrays.stream(new Snippet[]{getHeaderWithRefreshToken(), getExceptionResponseFields()}),
                        Arrays.stream(snippets)
                ).toArray(Snippet[]::new)
        );
    }

    private static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(prettyPrint());
    }

    private static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

    private static Snippet getHeaderWithAccessToken() {
        return HeaderDocumentation.requestHeaders(
                header(ACCESS_TOKEN_HEADER, "Access Token", true)
        );
    }

    private static Snippet getHeaderWithRefreshToken() {
        return HeaderDocumentation.requestHeaders(
                header(REFRESH_TOKEN_HEADER, "Refresh Token", true)
        );
    }

    private static Snippet getExceptionResponseFields() {
        return responseFields(
                fieldWithPath("code").description("커스텀 예외 코드"),
                fieldWithPath("message").description("예외 메시지")
        );
    }

    public static Snippet[] createHttpSpecSnippets(final Snippet... snippets) {
        return Arrays.stream(snippets).toArray(Snippet[]::new);
    }

    public static class SnippetFactory {
        /**
         * HeaderDocumentation.requestHeaders()
         * <br>
         * HeaderDocumentation.responseHeaders()
         */
        public static HeaderDescriptor header(final String name, final String description) {
            return headerWithName(name).description(description);
        }

        public static HeaderDescriptor header(final String name, final String description, final boolean mustRequired) {
            final HeaderDescriptor result = headerWithName(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static HeaderDescriptor header(final String name, final String description, final String constraint) {
            return headerWithName(name).description(description).attributes(constraint(constraint));
        }

        public static HeaderDescriptor header(final String name, final String description, final String constraint, final boolean mustRequired) {
            final HeaderDescriptor result = headerWithName(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        /**
         * CookieDocumentation.requestCookies()
         * <br>
         * CookieDocumentation.responseCookies()
         */
        public static CookieDescriptor cookie(final String name, final String description) {
            return cookieWithName(name).description(description);
        }

        public static CookieDescriptor cookie(final String name, final String description, final boolean mustRequired) {
            final CookieDescriptor result = cookieWithName(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static CookieDescriptor cookie(final String name, final String description, final String constraint) {
            return cookieWithName(name).description(description).attributes(constraint(constraint));
        }

        public static CookieDescriptor cookie(final String name, final String description, final String constraint, final boolean mustRequired) {
            final CookieDescriptor result = cookieWithName(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        /**
         * RequestDocumentation.pathParameters()
         */
        public static ParameterDescriptor path(final String name, final String description) {
            return parameterWithName(name).description(description);
        }

        public static ParameterDescriptor path(final String name, final String description, final boolean mustRequired) {
            final ParameterDescriptor result = parameterWithName(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static ParameterDescriptor path(final String name, final String description, final String constraint) {
            return parameterWithName(name).description(description).attributes(constraint(constraint));
        }

        public static ParameterDescriptor path(final String name, final String description, final String constraint, final boolean mustRequired) {
            final ParameterDescriptor result = parameterWithName(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        /**
         * RequestDocumentation.queryParameters()
         */
        public static ParameterDescriptor query(final String name, final String description) {
            return parameterWithName(name).description(description);
        }

        public static ParameterDescriptor query(final String name, final String description, final boolean mustRequired) {
            final ParameterDescriptor result = parameterWithName(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static ParameterDescriptor query(final String name, final String description, final String constraint) {
            return parameterWithName(name).description(description).attributes(constraint(constraint));
        }

        public static ParameterDescriptor query(final String name, final String description, final String constraint, final boolean mustRequired) {
            final ParameterDescriptor result = parameterWithName(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        /**
         * RequestDocumentation.partWithName()
         */
        public static RequestPartDescriptor file(final String name, final String description) {
            return partWithName(name).description(description);
        }

        public static RequestPartDescriptor file(final String name, final String description, final boolean mustRequired) {
            final RequestPartDescriptor result = partWithName(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static RequestPartDescriptor file(final String name, final String description, final String constraint) {
            return partWithName(name).description(description).attributes(constraint(constraint));
        }

        public static RequestPartDescriptor file(final String name, final String description, final String constraint, final boolean mustRequired) {
            final RequestPartDescriptor result = partWithName(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        /**
         * PayloadDocumentation.requestFields()
         * <br>
         * PayloadDocumentation.responseFields()
         */
        public static FieldDescriptor body(final String name, final String description) {
            return fieldWithPath(name).description(description);
        }

        public static FieldDescriptor body(final String name, final String description, final boolean mustRequired) {
            final FieldDescriptor result = fieldWithPath(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static FieldDescriptor body(final String name, final String description, final String constraint) {
            return fieldWithPath(name).description(description).attributes(constraint(constraint));
        }

        public static FieldDescriptor body(final String name, final String description, final String constraint, final boolean mustRequired) {
            final FieldDescriptor result = fieldWithPath(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        public static FieldDescriptor subsection(final String name, final String description) {
            return subsectionWithPath(name).description(description);
        }

        public static FieldDescriptor subsection(final String name, final String description, final boolean mustRequired) {
            final FieldDescriptor result = subsectionWithPath(name).description(description);
            return mustRequired ? result : result.optional();
        }

        public static FieldDescriptor subsection(final String name, final String description, final String constraint) {
            return subsectionWithPath(name).description(description).attributes(constraint(constraint));
        }

        public static FieldDescriptor subsection(final String name, final String description, final String constraint, final boolean mustRequired) {
            final FieldDescriptor result = subsectionWithPath(name).description(description).attributes(constraint(constraint));
            return mustRequired ? result : result.optional();
        }

        private static Attributes.Attribute constraint(final String value) {
            return new Attributes.Attribute("constraints", value);
        }
    }
}
