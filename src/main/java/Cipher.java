import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Cipher {

    AES("AES-pure", "AES-ideal", ""),
    DES("DES-pure", "DES-ideal", "/encrypted/des/"),
    BLOWFISH("Blowfish-pure", "Blowfish-ideal", ""),
    CAST_128("Cast-128-pure", "Cast-128-deal", ""),
    GOST_28147("GOST-28147-pure", "GOST-28147-ideal", ""),
    LOKI97("Loki97-pure", "Loki97-ideal", ""),
    RC2("RC2-pure", "RC2-ideal", ""),
    SERPENT("Serpent-pure", "Serpent-ideal", ""),
    XTEA("XTEA-pure", "XTEA-ideal", "");

    private String pure;
    private String ideal;
    private String path;

}
