package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.common.UnitTest;
import ac.dnd.mur.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.INVALID_EMAIL_PATTERN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Member -> 도메인 [Email] 테스트")
class EmailTest extends UnitTest {
    @Nested
    @DisplayName("Email 생성")
    class Construct {
        @ParameterizedTest
        @ValueSource(strings = {"", "abc", "@gmail.com", "abc@gmail", "abc@gmail.c"})
        @DisplayName("형식에 맞지 않는 Email이면 예외가 발생한다")
        void throwExceptionByInvalidEmailPattern(final String value) {
            assertThatThrownBy(() -> Email.from(value))
                    .isInstanceOf(MemberException.class)
                    .hasMessage(INVALID_EMAIL_PATTERN.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"sjiwon@gmail.com", "sjiwon@kakao.com", "sjiwon@naver.com", "sjiwon@nate.com"})
        @DisplayName("Email을 생성한다")
        void construct(final String value) {
            assertDoesNotThrow(() -> Email.from(value));
        }
    }
}
