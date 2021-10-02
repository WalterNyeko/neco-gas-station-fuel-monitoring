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
public class TransactionRequest {

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("transactionQuantity")
    private Double transactionQuantity;

    @JsonProperty("transactionDate")
    private String transactionDate;

    @JsonProperty("vehicleNumber")
    private String vehicleNumber;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("productId")
    private String productId;
}
