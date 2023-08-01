package controller;

import dto.CryptoCurrencyDTO;
import entity.CryptoCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CryptoCurrencyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cryptocurrencies")
public class CryptoCurrencyController {

    private final CryptoCurrencyService cryptoCurrencyService;

    @Autowired
    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }

    @GetMapping
    public ResponseEntity<List<CryptoCurrencyDTO>> getAllCryptoCurrencies() {
        List<CryptoCurrency> cryptoCurrencies = cryptoCurrencyService.getAllCryptoCurrencies();
        List<CryptoCurrencyDTO> cryptoCurrencyDTOs = cryptoCurrencies.stream()
                .map(CryptoCurrencyDTO::fromCryptoCurrency)
                .collect(Collectors.toList());
        return new ResponseEntity<>(cryptoCurrencyDTOs, HttpStatus.OK);
    }

    @GetMapping("/{cryptoCurrencyId}")
    public ResponseEntity<CryptoCurrencyDTO> getCryptoCurrencyById(@PathVariable Long cryptoCurrencyId) {
        CryptoCurrency cryptoCurrency = cryptoCurrencyService.getCryptoCurrencyById(cryptoCurrencyId);
        CryptoCurrencyDTO cryptoCurrencyDTO = CryptoCurrencyDTO.fromCryptoCurrency(cryptoCurrency);
        return new ResponseEntity<>(cryptoCurrencyDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CryptoCurrencyDTO> createCryptoCurrency(@RequestBody CryptoCurrencyDTO cryptoCurrencyDTO) {
        CryptoCurrency cryptoCurrency = cryptoCurrencyService.createCryptoCurrency(
                cryptoCurrencyDTO.getCurrencyCode(),
                cryptoCurrencyDTO.getName(),
                cryptoCurrencyDTO.getUsdRate()
        );
        return new ResponseEntity<>(CryptoCurrencyDTO.fromCryptoCurrency(cryptoCurrency), HttpStatus.CREATED);
    }

    @PutMapping("/{cryptoCurrencyId}")
    public ResponseEntity<CryptoCurrencyDTO> updateCryptoCurrency(@PathVariable Long cryptoCurrencyId, @RequestBody CryptoCurrencyDTO cryptoCurrencyDTO) {
        CryptoCurrency updatedCryptoCurrency = cryptoCurrencyService.updateCryptoCurrency(cryptoCurrencyId, cryptoCurrencyDTO.getSymbol(), cryptoCurrencyDTO.getName());
        return new ResponseEntity<>(CryptoCurrencyDTO.fromCryptoCurrency(updatedCryptoCurrency), HttpStatus.OK);
    }


    @DeleteMapping("/{cryptoCurrencyId}")
    public ResponseEntity<Void> deleteCryptoCurrency(@PathVariable Long cryptoCurrencyId) {
        cryptoCurrencyService.deleteCryptoCurrency(cryptoCurrencyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

