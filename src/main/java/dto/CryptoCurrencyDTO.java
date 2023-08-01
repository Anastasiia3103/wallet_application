package dto;

import entity.CryptoCurrency;
import entity.Wallet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CryptoCurrencyDTO {
    private Long id;
    private String symbol;
    private String name;
    private String currencyCode;
    private BigDecimal usdRate;
    private List<Wallet> wallets;

    public static CryptoCurrencyDTO fromCryptoCurrency(CryptoCurrency cryptoCurrency) {
        CryptoCurrencyDTO cryptoCurrencyDTO = new CryptoCurrencyDTO();
        cryptoCurrencyDTO.setId(cryptoCurrency.getId());
        cryptoCurrencyDTO.setSymbol(cryptoCurrency.getSymbol());
        cryptoCurrencyDTO.setName(cryptoCurrency.getName());
        cryptoCurrencyDTO.setCurrencyCode(cryptoCurrency.getCurrencyCode());
        cryptoCurrencyDTO.setUsdRate(cryptoCurrency.getUsdRate());
        return cryptoCurrencyDTO;
    }
}
