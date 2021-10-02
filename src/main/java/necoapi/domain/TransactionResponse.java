package necoapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    @JsonProperty("transactionId")
    private Long transactionId;

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("transactionQuantity")
    private Double transactionQuantity;

    @JsonProperty("transactionAmount")
    private Double transactionAmount;

    @JsonProperty("transactionDate")
    private String transactionDate;

    @JsonProperty("vehicleNumber")
    private String vehicleNumber;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("currentBalance")
    private Double currentBalance;

    @JsonProperty("productName")
    private String productName;
}
