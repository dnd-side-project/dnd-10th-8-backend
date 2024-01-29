package ac.dnd.mur.server.member.domain.model;

import ac.dnd.mur.server.common.UnitTest;
import ac.dnd.mur.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static ac.dnd.mur.server.member.exception.MemberExceptionCode.INVALID_NICKNAME_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Member -> 도메인 [Nickname] 테스트")
class NicknameTest extends UnitTest {
    @Nested
    @DisplayName("Nickname 생성")
    class Construct {
        @ParameterizedTest
        @ValueSource(strings = {"", "aaaaabbbbbcccccd", "H ell"})
        @DisplayName("형식에 맞지 않는 Nickname이면 생성에 실패한다")
        void throwExceptionByInvalidNicknameFormat(final String value) {
            assertThatThrownBy(() -> Nickname.from(value))
                    .isInstanceOf(MemberException.class)
                    .hasMessage(INVALID_NICKNAME_PATTERN.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"하이", "Hello", "Hello하이123"})
        @DisplayName("Nickname을 생성한다")
        void construct(final String value) {
            assertDoesNotThrow(() -> Nickname.from(value));
        }
    }

    @Test
    @DisplayName("닉네임을 수정한다")
    void success() {
        // given
        final Nickname nickname = Nickname.from("Hello");

        // when
        final Nickname updateNickname = nickname.update("하이");

        // then
        assertThat(updateNickname.getValue()).isEqualTo("하이");
    }
}
