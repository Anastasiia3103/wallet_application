package service;

import entity.Transaction;
import entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TransactionRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
    }

    public Transaction createTransaction(Wallet senderWallet, Wallet recipientWallet, BigDecimal amount) {
        Wallet existingSenderWallet = walletService.getWalletById(senderWallet.getId());
        Wallet existingRecipientWallet = walletService.getWalletById(recipientWallet.getId());

        BigDecimal senderBalance = existingSenderWallet.getBalance();
        if (senderBalance.compareTo(amount) < 0){
            throw new IllegalArgumentException("Insufficient balance in the sender's wallet.");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(existingSenderWallet);
        transaction.setRecipient(existingRecipientWallet);
        transaction.setAmount(amount);

        transaction = transactionRepository.save(transaction);

        walletService.updateWalletsAfterTransaction(transaction);

        return transaction;
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionId));
    }

    public List<Transaction> getTransactionHistoryForWallet(Wallet wallet) {
        return transactionRepository.findBySenderOrRecipientOrderByTransactionDateDesc(wallet, wallet);
    }
}

