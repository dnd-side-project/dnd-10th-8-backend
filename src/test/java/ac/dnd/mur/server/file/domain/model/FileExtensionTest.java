package ac.dnd.mur.server.file.domain.model;

import ac.dnd.mur.server.common.UnitTest;
import ac.dnd.mur.server.file.exception.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static ac.dnd.mur.server.file.exception.FileExceptionCode.INVALID_FILE_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("File -> 도메인 [FileExtension] 테스트")
class FileExtensionTest extends UnitTest {
    @ParameterizedTest
    @ValueSource(strings = {"hello.gif", "hello.mp3", "hello.xls", "hello.alz"})
    @DisplayName("제공하지 않는 파일의 확장자면 예외가 발생한다")
    void throwExceptionByInvalidFileExtension(final String fileName) {
        assertThatThrownBy(() -> FileExtension.getExtensionViaFimeName(fileName))
                .isInstanceOf(FileException.class)
                .hasMessage(INVALID_FILE_EXTENSION.getMessage());
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "hello.jpg:JPG",
                    "hello.jpeg:JPEG",
                    "hello.png:PNG"
            },
            delimiter = ':'
    )
    @DisplayName("파일 확장자에 대한 FileExtension을 얻는다")
    void success(final String fileName, final FileExtension extension) {
        assertThat(FileExtension.getExtensionViaFimeName(fileName)).isEqualTo(extension);
    }
}
