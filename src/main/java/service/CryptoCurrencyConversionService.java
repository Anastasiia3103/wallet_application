package service;

import dto.CryptoCurrencyConversionDTO;
import entity.CryptoCurrency;
import entity.CryptoCurrencyConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CryptoCurrencyConversionRepository;
import repository.CryptoCurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CryptoCurrencyConversionService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CryptoCurrencyConversionRepository cryptoCurrencyConversionRepository;

    @Autowired
    public CryptoCurrencyConversionService(CryptoCurrencyRepository cryptoCurrencyRepository, CryptoCurrencyConversionRepository cryptoCurrencyConversionRepository) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
        this.cryptoCurrencyConversionRepository = cryptoCurrencyConversionRepository;
    }

    public BigDecimal convertToBitcoin(BigDecimal amountInUSDT) {
        CryptoCurrency usdt = getCryptoCurrencyByCode("USDT");
        CryptoCurrency bitcoin = getCryptoCurrencyByCode("BTC");

        BigDecimal usdtToUsdRate = usdt.getUsdRate();
        BigDecimal btcToUsdRate = bitcoin.getUsdRate();

        BigDecimal usdtToBtcRate = btcToUsdRate.divide(usdtToUsdRate, 8, BigDecimal.ROUND_HALF_UP);
        return amountInUSDT.multiply(usdtToBtcRate);
    }

    public BigDecimal convertToUSDT(BigDecimal amountInBitcoin) {
        CryptoCurrency usdt = getCryptoCurrencyByCode("USDT");
        CryptoCurrency bitcoin = getCryptoCurrencyByCode("BTC");

        BigDecimal usdtToUsdRate = usdt.getUsdRate();
        BigDecimal btcToUsdRate = bitcoin.getUsdRate();

        BigDecimal btcToUsdtRate = usdtToUsdRate.divide(btcToUsdRate, 8, BigDecimal.ROUND_HALF_UP);
        return amountInBitcoin.multiply(btcToUsdtRate);
    }

    public CryptoCurrencyConversionDTO performConversion(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode) {
        CryptoCurrency fromCurrency = getCryptoCurrencyByCode(fromCurrencyCode);
        CryptoCurrency toCurrency = getCryptoCurrencyByCode(toCurrencyCode);

        BigDecimal convertedAmount = convertToBitcoin(amount);

        CryptoCurrencyConversionDTO conversionDTO = new CryptoCurrencyConversionDTO();
        conversionDTO.setAmount(amount);
        conversionDTO.setFromCurrencySymbol(fromCurrencyCode);
        conversionDTO.setToCurrencySymbol(toCurrencyCode);
        conversionDTO.setConvertedAmount(convertedAmount);
        conversionDTO.setConversionDate(LocalDateTime.now());

        recordCryptoCurrencyConversion(amount, fromCurrency, toCurrency, convertedAmount, conversionDTO.getConversionDate());

        return conversionDTO;
    }

    public void recordCryptoCurrencyConversion(BigDecimal amount, CryptoCurrency fromCurrency, CryptoCurrency toCurrency, BigDecimal convertedAmount, LocalDateTime conversionDate) {
        CryptoCurrencyConversion conversion = new CryptoCurrencyConversion();
        conversion.setAmount(amount);
        conversion.setFromCurrency(fromCurrency);
        conversion.setToCurrency(toCurrency);
        conversion.setConvertedAmount(convertedAmount);
        conversion.setConversionDate(conversionDate);

        cryptoCurrencyConversionRepository.save(conversion);
    }

    private CryptoCurrency getCryptoCurrencyByCode(String currencyCode) {
        return cryptoCurrencyRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Cryptocurrency not found with code: " + currencyCode));
    }
}

