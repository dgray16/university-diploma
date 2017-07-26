import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Cipher {

    AES("AES-pure", "AES-ideal", "29v0r5/lm7YeAk7HR6yY3lLJQZogNuhcidaZ3kyJ5ZvN5hAuJDHCVXzqQzI0tyKjixjx6jV0Bx8+JBAu4GoG1xofy8UahS0nEGi54tahiUo="),
    DES("DES-pure", "DES-ideal", "kjF5a7pwbdvLlgFaBl9jwO5QA6lXdvw1C1T0oX4QVo5t2iwZQpI+EH9NrjZZrukKzas8t+2dczfPDkSJKlStqtwqmfQw73x9"),
    BLOWFISH("Blowfish-pure", "Blowfish-ideal", "jnSwcgZZhm70cUFR+eQ9oW9b81xEVzsvUeJHyWmg7wEZk6phvdixej1d2Zt71cZSZVjj6XeAN8ipCCavkFLunlmCctqhkVuj");

    private String pure;
    private String ideal;
    private String cipherText;

}
