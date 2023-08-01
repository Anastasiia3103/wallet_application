package controller;

import dto.CryptoCurrencyConversionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.CryptoCurrencyConversionService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/conversions")
public class CryptoCurrencyConversionController {

    private final CryptoCurrencyConversionService conversionService;

    @Autowired
    public CryptoCurrencyConversionController(CryptoCurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/btc-to-usdt")
    public ResponseEntity<CryptoCurrencyConversionDTO> convertBtcToUsdt(@RequestParam BigDecimal amount) {
        CryptoCurrencyConversionDTO conversionDTO = conversionService.performConversion(amount, "BTC", "USDT");
        return new ResponseEntity<>(conversionDTO, HttpStatus.OK);
    }

    @GetMapping("/usdt-to-btc")
    public ResponseEntity<CryptoCurrencyConversionDTO> convertUsdtToBtc(@RequestParam BigDecimal amount) {
        CryptoCurrencyConversionDTO conversionDTO = conversionService.performConversion(amount, "USDT", "BTC");
        return new ResponseEntity<>(conversionDTO, HttpStatus.OK);
    }
}

