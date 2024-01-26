package ac.dnd.bookkeeping.server.member.domain.model;

import ac.dnd.bookkeeping.server.common.UnitTest;
import ac.dnd.bookkeeping.server.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ac.dnd.bookkeeping.server.member.domain.model.Gender.FEMALE;
import static ac.dnd.bookkeeping.server.member.domain.model.Gender.MALE;
import static ac.dnd.bookkeeping.server.member.exception.MemberExceptionCode.INVALID_GENDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Member -> 도메인 [Gender] 테스트")
class GenderTest extends UnitTest {
    @Test
    @DisplayName("male / female 이외의 성별이 들어오면 예외가 발생한다")
    void throwExceptionByInvalidGender() {
        assertThatThrownBy(() -> Gender.from("anonymous"))
                .isInstanceOf(MemberException.class)
                .hasMessage(INVALID_GENDER.getMessage());
    }

    @Test
    @DisplayName("Gender로 변환한다")
    void success() {
        assertAll(
                () -> assertThat(Gender.from("male")).isEqualTo(MALE),
                () -> assertThat(Gender.from("female")).isEqualTo(FEMALE)
        );
    }
}
