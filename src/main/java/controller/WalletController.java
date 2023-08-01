package controller;

import dto.WalletDTO;
import entity.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.WalletService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wallets")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDTO> getWalletById(@PathVariable Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId);

        WalletDTO walletDTO = WalletDTO.fromWallet(wallet);

        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<WalletDTO>> getAllWallets() {
        List<Wallet> wallets = walletService.getAllWallets();

        List<WalletDTO> walletDTOs = wallets.stream()
                .map(WalletDTO::fromWallet)
                .collect(Collectors.toList());

        return new ResponseEntity<>(walletDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WalletDTO> createWallet(@RequestBody WalletDTO walletDTO) {
        Wallet wallet = walletService.createWalletWithCurrencies(
                walletDTO.getBalance(),
                walletDTO.getCurrency(),
                walletDTO.getUserId(),
                walletDTO.getCryptoCurrencyIds()
        );

        return new ResponseEntity<>(WalletDTO.fromWallet(wallet), HttpStatus.CREATED);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<WalletDTO> updateWallet(@PathVariable Long walletId, @RequestBody WalletDTO walletDTO) {
        Wallet updatedWallet = walletService.updateWallet(walletId, walletDTO.getBalance());

        return new ResponseEntity<>(WalletDTO.fromWallet(updatedWallet), HttpStatus.OK);
    }
}

