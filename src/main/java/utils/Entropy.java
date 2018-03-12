package utils;

import application.Main;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;

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
        Main.LOG.info("{} ( H = {} )", cipher, BigDecimal.valueOf(h).setScale(6, BigDecimal.ROUND_CEILING));

        return BigDecimal.valueOf(h).setScale(6, BigDecimal.ROUND_CEILING);
    }

    public BigDecimal calculate(String cipher, byte[] plainBytes) {
        int length = plainBytes.length;

        /* Alphabet */
        Set<Integer> alphabet = getAlphabet(plainBytes);
        List<Integer> cipherTextList = getCipherTextAsList(plainBytes);

        /* Frequency */
        Map<Integer, Integer> frequency = getFrequencyForBinary(alphabet, cipherTextList);

        Double h = calculateHValueForBinary(frequency, length);
        Main.LOG.info("{} ( H = {} )", cipher, BigDecimal.valueOf(h).setScale(6, BigDecimal.ROUND_CEILING));

        return BigDecimal.valueOf(h).setScale(6, BigDecimal.ROUND_CEILING);
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

    public Set<Integer> getAlphabet(byte[] text) {
        Set<Integer> found = new HashSet<>();

        for (byte letter : text) {
            int value = Byte.valueOf(letter).intValue();
            if (BooleanUtils.isFalse(found.contains(value))) {
                found.add(value);
            }
        }

        return found;
    }

    /**
     * Returns list of all letters found in text.
     * Used to calculate frequency of letters.
     */
    public List<String> getCipherTextAsList(String text) {
        List<String> found = new ArrayList<>();
        found.addAll(Arrays.asList(text.split(StringUtils.EMPTY)));
        return found;
    }

    /**
     * Returns list of all numbers found in byte array.
     * Used to calculate frequency of numbers.
     */
    public List<Integer> getCipherTextAsList(byte[] text) {
        List<Integer> found = new ArrayList<>();
        for (byte letter : text) {
            found.add(Byte.valueOf(letter).intValue());
        }
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

    /**
     * This list of maps does not have any duplicates, because loop was fed with stub map (that was fed with alphabet).
     */
    private Map<Integer, Integer> getFrequencyForBinary(Set<Integer> alphabet, List<Integer> cipherText) {
        /* Generate stub frequency */
        Map<Integer, Integer> frequency = new HashMap<>();
        alphabet.forEach(letter -> frequency.putAll(createMap(letter, 0)));

        /* 10 sec parallel stream with 2 - 12 MB */
        /* 25 sec with 95 MB */
        cipherText.parallelStream().forEach(letter -> {
            synchronized (frequency) {
                /* TODO how about to do 'value' as mutex instead of 'frequency' ? */
                Integer value = frequency.get(letter);
                frequency.put(letter, value + 1);
            }
        });

        /* 5 sec with 2 - 12 MB */
        /* 32 sec with 95 MB */
        /*cipherText.forEach(letter -> {
            Integer value = frequency.get(letter);
            frequency.put(letter, value + 1);
        });*/

        return frequency;
    }

    private Map<String, Integer> createMap(String key, Integer value) {
        Map<String, Integer> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<Integer, Integer> createMap(Integer key, Integer value) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Double calculateHValue(List<Map<String, Integer>> list, Integer textLength) {
        List<Double> pValues = getPList(list, textLength);
        Double result = 0D;

        for (Double pValue : pValues) {
            result += logBaseTwo(pValue);
        }

        return -result;
        /* 12MB file - (result: -0.09375, pValue: 0.015625, log: -6.0000, pValues: 64) */
        /* 9MB  file - (result: -0.09375, pValue: 0.015625, log: -6.0000, pValues: 64) */
        /* 2MB  file - (result: -0.09375, pValue: 0.015625, log: -6.0000, pValues: 64) */
        /* 1MB  file - (result: -0.09265, pValue: 0.015384, log: -6.0223, pValues: 65) */
    }

    private Double calculateHValueForBinary(Map<Integer, Integer> frequency, Integer textLength) {
        List<Double> pValues = getPListForBinary(frequency, textLength);
        Double result = 0D;

        for (Double pValue : pValues) {
            /* Shannon`s formula */
            result += logBaseTen(pValue);
        }

        return -result;
        /* 12MB file - (result: -0.09375, pValue: 0.015625, log: -6.0000, pValues: 64) */
        /* 9MB  file - (result: -0.09375, pValue: 0.015625, log: -6.0000, pValues: 64) */
        /* 2MB  file - (result: -0.09375, pValue: 0.015625, log: -6.0000, pValues: 64) */
        /* 1MB  file - (result: -0.09265, pValue: 0.015384, log: -6.0223, pValues: 65) */
    }

    private double logBaseTwo(Double pValue) {
        return pValue * ( log(pValue) / log(2D) );
    }

    private double logBaseTen(Double pValue) {
        return pValue * ( (log(pValue) / log(10D)) / (log(2D) / log(10D)) );
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

    private List<Double> getPListForBinary(Map<Integer, Integer> frequency, Integer length) {
        return frequency.entrySet().parallelStream()
                .map(value -> calculatePValue(value.getValue(), length))
                .collect(Collectors.toList());
    }

}
