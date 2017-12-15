package dictionary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Cipher {

    AES("AES-pure", "AES-ideal", "/encrypted/aes/", "AES"),
    DES("DES-pure", "DES-ideal", "/encrypted/des/", "DES"),
    BLOWFISH("Blowfish-pure", "Blowfish-ideal", "/encrypted/blowfish/", "Blowfish"),
    CAST_128("Cast-128-pure", "Cast-128-ideal", EMPTY, EMPTY),
    GOST_28147("GOST-28147-pure", "GOST-28147-ideal", EMPTY, EMPTY),
    LOKI97("Loki97-pure", "Loki97-ideal", EMPTY, EMPTY),
    RC2("RC2-pure", "RC2-ideal", EMPTY, EMPTY),
    SERPENT("Serpent-pure", "Serpent-ideal", EMPTY, EMPTY),
    XTEA("XTEA-pure", "XTEA-ideal", EMPTY, EMPTY),
    XOR_WEAK("XOR-WEAK", "XOR-WEAK-ideal", "/encrypted/pseudorandomnumbergenerator/weak/", EMPTY),
    XOR_STRONG("XOR-STRONG", "XOR-STRONG-ideal", "/encrypted/pseudorandomnumbergenerator/strong/", EMPTY),

    /** Means no cipher at all ( plaintext ) */
    RAW("RAW", "RAW-ideal", "/raw/", EMPTY);

    String pure;
    String ideal;
    String path;
    String javaCipherNotation;

}
