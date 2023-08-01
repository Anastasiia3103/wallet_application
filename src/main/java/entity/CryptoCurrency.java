package entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "crypto_currencies")
public class CryptoCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String currencyCode;

    @OneToMany(mappedBy = "cryptoCurrency", cascade = CascadeType.ALL)
    private List<Wallet> wallets;

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoCurrency.class);

    public BigDecimal getUsdRate() {
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                BigDecimal usdRate = jsonNode.get("bitcoin").get("usd").decimalValue();

                return usdRate;
            }
            else {
                LOGGER.error("API call failed with response code: {}", responseCode);
                return BigDecimal.ZERO;
            }
        } catch (IOException e) {
            LOGGER.error("Error fetching USD exchange rate: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setUsdRate(BigDecimal usdRate) {
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}

