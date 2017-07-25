import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.log;

@NoArgsConstructor
public class Entropy {

    public BigDecimal calculate(String cipher, String cipherText) {
        int length = cipherText.getBytes().length;

        /* Alphabet */
        Set<String> alphabet = getAlphabet(cipherText);
        List<String> cipherTextList = getCipherTextAsList(cipherText);

        /* Frequency */
        List<Map<String, Integer>> frequency = getFrequency(alphabet, cipherTextList);

        Double h = calculateHValue(frequency, length);
        Main.LOG.info("{} ( H = {} )", cipher, BigDecimal.valueOf(h).setScale(2, BigDecimal.ROUND_CEILING));

        return BigDecimal.valueOf(h).setScale(2, BigDecimal.ROUND_CEILING);
    }

    public Set<String> getAlphabet(String text) {
        Set<String> found = new HashSet<>();

        Arrays.stream(text.split(StringUtils.EMPTY)).forEach(letter -> {
            if ( !found.contains(letter) ) {
                found.add(letter);
            }
        });
        return found;
    }

    public List<String> getCipherTextAsList(String text) {
        List<String> found = new ArrayList<>();
        found.addAll(Arrays.asList(text.split(StringUtils.EMPTY)));
        return found;
    }

    /**
     * This list of maps does not have any duplicates, because loop was fed with stub map (that was fed with alphabet).
     */
    private List<Map<String, Integer>> getFrequency(Set<String> alphabet, List<String> cipherText) {
        /* Generate stub frequency */
        List<Map<String, Integer>> frequency = new ArrayList<>();
        alphabet.forEach(letter -> frequency.add(createMap(letter, 0)));

        cipherText.forEach(letter -> frequency.forEach(map -> {
            Map.Entry entry = map.entrySet().iterator().next();
            if ( letter.equals(entry.getKey()) ) {
                map.put(letter, (Integer) entry.getValue() + 1);
            }
        }));

        return frequency;
    }

    private Map<String, Integer> createMap(String key, Integer value) {
        Map<String, Integer> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Double calculateHValue(List<Map<String, Integer>> list, Integer textLength) {
        List<Double> pValues = getPList(list, textLength);
        Double result = 0D;

        for (Double pValue : pValues) {
            result += pValue * (log(pValue) / log(2));
        }

        return -result;
    }

    private Double calculatePValue(Integer value, Integer length) {
        return value / length.doubleValue();
    }

    private List<Double> getPList(List<Map<String, Integer>> list, Integer length) {
        List<Double> pValues = new ArrayList<>();
        list.forEach(map -> {
            Map.Entry entry = map.entrySet().iterator().next();
            pValues.add(calculatePValue((Integer) entry.getValue(), length));
        });
        return pValues;
    }

}
