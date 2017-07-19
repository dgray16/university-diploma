import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class Entropy {

    public void calculate(String cipherText) {
        int lenght = cipherText.getBytes().length;

        /* Alphabet */
        List<String> alphabet = getAlphabet(cipherText);

        /* TODO calculate frequency (List<Map<String, Integer>>) */
    }

    private List<String> getAlphabet(String text) {
        List<String> found = new ArrayList<>();

        Arrays.stream(text.split(StringUtils.EMPTY)).forEach(letter -> {
            if ( !found.contains(letter) ) {
                found.add(letter);
            }
        });
        return found;
    }

}
