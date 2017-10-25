import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@AllArgsConstructor
@Getter
public enum Cipher {

    AES("AES-pure", "AES-ideal", EMPTY),
    DES("DES-pure", "DES-ideal", "/encrypted/des/"),
    BLOWFISH("Blowfish-pure", "Blowfish-ideal", EMPTY),
    CAST_128("Cast-128-pure", "Cast-128-deal", EMPTY),
    GOST_28147("GOST-28147-pure", "GOST-28147-ideal", EMPTY),
    LOKI97("Loki97-pure", "Loki97-ideal", EMPTY),
    RC2("RC2-pure", "RC2-ideal", EMPTY),
    SERPENT("Serpent-pure", "Serpent-ideal", EMPTY),
    XTEA("XTEA-pure", "XTEA-ideal", EMPTY),
    XOR_WEAK("XOR-WEAK", "XOR-WEAK-ideal", "/encrypted/pseudorandomnumbergenerator/weak/"),
    XOR_STRONG("XOR-STRONG", "XOR-STRONG-ideal", "/encrypted/pseudorandomnumbergenerator/strong/"),

    /** Means no cipher at all ( plaintext ) */
    RAW("RAW", "RAW-idea", "raw/");

    private String pure;
    private String ideal;
    private String path;

}
