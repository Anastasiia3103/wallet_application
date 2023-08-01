package entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "crypto_currency_conversions")
public class CryptoCurrencyConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_currency_id", referencedColumnName = "id")
    private CryptoCurrency fromCurrency;

    @ManyToOne
    @JoinColumn(name = "to_currency_id", referencedColumnName = "id")
    private CryptoCurrency toCurrency;

    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private LocalDateTime conversionDate;

}

