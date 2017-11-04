package dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestType {

    TEXT_SINGLE("text/singlebook"),
    TEXT_DOUBLE("text/twobooks"),
    TEST("text/test/"),

    AUDIO("audio"),
    VIDEO("video"),
    IMAGE("image");

    private String type;

}
