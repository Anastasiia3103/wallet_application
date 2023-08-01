package controller;

import dto.TransactionDTO;
import dto.WalletDTO;
import entity.Transaction;
import entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.TransactionService;
import service.WalletService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final WalletService walletService;

    @Autowired
    public TransactionController(TransactionService transactionService, WalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        Wallet senderWallet = WalletDTO.toWallet(transactionDTO.getSender());
        Wallet recipientWallet = WalletDTO.toWallet(transactionDTO.getRecipient());

        Transaction transaction = transactionService.createTransaction(senderWallet, recipientWallet, transactionDTO.getAmount());

        return new ResponseEntity<>(TransactionDTO.fromTransaction(transaction), HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return new ResponseEntity<>(TransactionDTO.fromTransaction(transaction), HttpStatus.OK);
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistoryForWallet(@PathVariable Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId);

        List<Transaction> transactions = transactionService.getTransactionHistoryForWallet(wallet);

        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(TransactionDTO::fromTransaction)
                .collect(Collectors.toList());


        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }
}

