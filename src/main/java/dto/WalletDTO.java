package dto;

import entity.Wallet;
import entity.User;
import entity.CryptoCurrency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class WalletDTO {
    private Long id;
    private BigDecimal balance;
    private String currency;
    private Long userId;
    private List<Long> cryptoCurrencyIds;

    public static WalletDTO fromWallet(Wallet wallet) {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setId(wallet.getId());
        walletDTO.setBalance(wallet.getBalance());
        walletDTO.setCurrency(wallet.getCurrency());

        if (wallet.getUser() != null){
            walletDTO.setUserId(wallet.getUser().getId());
        }


        if (wallet.getCryptoCurrencies() != null){
            List<Long> cryptoCurrencyIds = wallet.getCryptoCurrencies().stream()
                    .map(CryptoCurrency::getId)
                    .collect(Collectors.toList());
            walletDTO.setCryptoCurrencyIds(cryptoCurrencyIds);
        }

        return walletDTO;
    }

    public static Wallet toWallet(WalletDTO walletDTO) {
        Wallet wallet = new Wallet();
        wallet.setId(walletDTO.getId());
        wallet.setBalance(walletDTO.getBalance());
        wallet.setCurrency(walletDTO.getCurrency());


        if (walletDTO.getUserId() != null){
            User user = new User();
            user.setId(walletDTO.getUserId());
            wallet.setUser(user);
        }

        if (walletDTO.getCryptoCurrencyIds() != null){
            List<CryptoCurrency> cryptoCurrencies = walletDTO.getCryptoCurrencyIds().stream()
                    .map(cryptoCurrencyId -> {
                        CryptoCurrency cryptoCurrency = new CryptoCurrency();
                        cryptoCurrency.setId(cryptoCurrencyId);
                        return cryptoCurrency;
                    })
                    .collect(Collectors.toList());
            wallet.setCryptoCurrencies(cryptoCurrencies);
        }

        return wallet;
    }
}


