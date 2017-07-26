import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Cipher {

    AES("AES-pure", "AES-ideal", "29v0r5/lm7YeAk7HR6yY3lLJQZogNuhcidaZ3kyJ5ZvN5hAuJDHCVXzqQzI0tyKjixjx6jV0Bx8+JBAu4GoG1xofy8UahS0nEGi54tahiUo="),
    DES("DES-pure", "DES-ideal", "kjF5a7pwbdvLlgFaBl9jwO5QA6lXdvw1C1T0oX4QVo5t2iwZQpI+EH9NrjZZrukKzas8t+2dczfPDkSJKlStqtwqmfQw73x9"),
    BLOWFISH("Blowfish-pure", "Blowfish-ideal", "jnSwcgZZhm70cUFR+eQ9oW9b81xEVzsvUeJHyWmg7wEZk6phvdixej1d2Zt71cZSZVjj6XeAN8ipCCavkFLunlmCctqhkVuj"),
    CAST_128("Cast-128-pure", "Cast-128-deal", "5gR7waZApZO+fyDzoz3UmqhZMXVW1v0G4K/7BpUtht1nYDACL2wk4UILVwk9RaAMdvm/lW5I9oADdEzeJAiNa9hL0oKC7JFl"),
    GOST_28147("GOST-28147-pure", "GOST-28147-ideal", "py/xwrnJW00cw89qWpUaxfAKjiQIT6pKxklL2NPzlsKbfZHIj19uLRfVERhpd5sBnIeHJ09xgxtS9VhOOuZjJkqGmlDuNpqa"),
    LOKI97("Loki97-pure", "Loki97-ideal", "4wFdnD8t5YTRxdD2QPybg1eMcOx9JY0BVTNAPoByk0CldLxnqxiKdvb77ljG/UQkq9c4wkD+hc+woUqfQb8rS6Xif+xHqZOqi6ADWQ7YaP4="),
    RC2("RC2-pure", "RC2-ideal", "6+jdmdIvFIyscnfidImbiTmm6mfctxtQHBNG6KGj/XW1NtuYqGh6OudZiVboLu8aMwF0sH8y0V4D+VzCii/dnZ/FXAY3IKmX"),
    SERPENT("Serpent-pure", "Serpent-ideal", "C0J3WdfaZNJHGMeq5D2NyahKxq+pEBMd+D0SUEwKAvAzYH4WT9Wbc6NnBZqrHBz1hB8N3vD3V+djd4s00EB1C2GsvN3sMkaL5TcVteZjNxo="),
    XTEA("XTEA-pure", "XTEA-ideal", "7oCj3eEXo/S3sWVlTSzjo1hZ5O6burcjlZU6TZvhusS3pB76oLB4wFVvz8SeXSnKvUUcSZi3E0xL2JCelJ9spI50diCxvJnY");

    private String pure;
    private String ideal;
    private String cipherText;

}
