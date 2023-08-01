package service;

import entity.CryptoCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CryptoCurrencyRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CryptoCurrencyService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    public CryptoCurrencyService(CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }

    public CryptoCurrency createCryptoCurrency(String currencyCode, String name, BigDecimal usdRate) {
        CryptoCurrency existingCryptoCurrency = cryptoCurrencyRepository.findByCurrencyCode(currencyCode).orElse(null);
        if (existingCryptoCurrency != null){
            throw new IllegalArgumentException("Cryptocurrency with code " + currencyCode + " already exists.");
        }

        CryptoCurrency newCryptoCurrency = new CryptoCurrency();
        newCryptoCurrency.setCurrencyCode(currencyCode);
        newCryptoCurrency.setName(name);
        newCryptoCurrency.setUsdRate(usdRate);

        return cryptoCurrencyRepository.save(newCryptoCurrency);
    }

    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return cryptoCurrencyRepository.findAll();
    }

    public CryptoCurrency getCryptoCurrencyById(Long id) {
        return cryptoCurrencyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cryptocurrency not found with ID: " + id));
    }

    public CryptoCurrency updateCryptoCurrency(Long cryptoCurrencyId, String symbol, String name) {
        CryptoCurrency existingCryptoCurrency = cryptoCurrencyRepository.findById(cryptoCurrencyId)
                .orElseThrow(() -> new EntityNotFoundException("Cryptocurrency not found with ID: " + cryptoCurrencyId));

        existingCryptoCurrency.setSymbol(symbol);
        existingCryptoCurrency.setName(name);

        return cryptoCurrencyRepository.save(existingCryptoCurrency);
    }

    public void deleteCryptoCurrency(Long id) {
        cryptoCurrencyRepository.deleteById(id);
    }
}
