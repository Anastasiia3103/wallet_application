package service;

import entity.CryptoCurrency;
import entity.Transaction;
import entity.User;
import entity.Wallet;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;
import repository.CryptoCurrencyRepository;
import repository.UserRepository;
import repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final UserRepository userRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, CryptoCurrencyRepository cryptoCurrencyRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
        this.userRepository = userRepository;
    }

    public Wallet createWalletWithCurrencies(BigDecimal balance, String currency, Long userId, List<Long> cryptoCurrencyIds) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EmptyResultDataAccessException("User not found with ID: " + userId, 1));

        List<CryptoCurrency> cryptoCurrencies = cryptoCurrencyRepository.findAllById(cryptoCurrencyIds);

        if (cryptoCurrencies.size() != cryptoCurrencyIds.size()){
            throw new EntityNotFoundException("One or more cryptocurrencies not found.");
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(balance);
        wallet.setCurrency(currency);
        wallet.setUser(existingUser);
        wallet.setCryptoCurrencies(cryptoCurrencies);

        return walletRepository.save(wallet);
    }


    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found with ID: " + id));
    }

    public Wallet updateWallet(Long walletId, BigDecimal newBalance) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found with ID: " + walletId));

        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public void updateWalletsAfterTransaction(Transaction transaction) {
        Wallet senderWallet = transaction.getSender();
        Wallet recipientWallet = transaction.getRecipient();
        BigDecimal transactionAmount = transaction.getAmount();

        BigDecimal senderBalance = senderWallet.getBalance();
        senderWallet.setBalance(senderBalance.subtract(transactionAmount));

        BigDecimal recipientBalance = recipientWallet.getBalance();
        recipientWallet.setBalance(recipientBalance.add(transactionAmount));

        walletRepository.save(senderWallet);
        walletRepository.save(recipientWallet);
    }
}


