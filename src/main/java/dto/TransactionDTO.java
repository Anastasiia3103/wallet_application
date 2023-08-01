package dto;

import entity.Transaction;
import entity.Wallet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionDTO {
    private Long id;
    private WalletDTO sender;
    private WalletDTO recipient;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    public static TransactionDTO fromTransaction(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setSender(WalletDTO.fromWallet(transaction.getSender()));
        transactionDTO.setRecipient(WalletDTO.fromWallet(transaction.getRecipient()));
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        return transactionDTO;
    }

    public static WalletDTO toWalletDTO(Wallet wallet) {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setId(wallet.getId());
        // Map other fields as needed
        return walletDTO;
    }

    public static Wallet toWallet(WalletDTO walletDTO) {
        Wallet wallet = new Wallet();
        wallet.setId(walletDTO.getId());
        return wallet;
    }

}
