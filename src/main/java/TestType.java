import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestType {

    TEXT("text"),
    AUDIO("audio"),
    VIDEO("video"),
    IMAGE("image");

    private String type;

}
