package necoapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.helpers.Auditable;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {
    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("openingBalance")
    private Double openingBalance;

    @JsonProperty("openingDate")
    private String openingDate;

    @JsonProperty("bankOverdraftAllowed")
    private Boolean bankOverdraftAllowed;
}
