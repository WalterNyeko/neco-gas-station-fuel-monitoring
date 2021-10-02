package necoapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse  extends ProductRequest {

    @JsonProperty("productId")
    private Long productId;

    @Builder(builderMethodName = "productResponseBuilder")
    public ProductResponse(String productName, String productDescription, Double productUnitPrice, Long productId) {
        super(productName, productDescription, productUnitPrice);
        this.productId = productId;
    }
}
