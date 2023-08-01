package dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CryptoCurrencyConversionDTO {
    private Long id;
    private String fromCurrencySymbol;
    private String toCurrencySymbol;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private LocalDateTime conversionDate;
}
