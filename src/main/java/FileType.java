import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {

    TEXT("text"),
    AUDIO("audio");

    private String type;

}
