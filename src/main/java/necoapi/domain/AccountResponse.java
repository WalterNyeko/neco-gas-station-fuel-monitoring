package necoapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse extends AccountRequest{

    @JsonProperty("accountId")
    private Long accountId;

    @JsonProperty("currentBalance")
    private Double currentBalance;

    @JsonProperty("createdBy")
    String createdBy;

    @JsonProperty("createdDate")
    Date createdDate;

    @JsonProperty("lastModifiedBy")
    String lastModifiedBy;

    @JsonProperty("lastModifiedDate")
    Date lastModifiedDate;

    @Builder(builderMethodName = "accountResponseBuilder")
    public AccountResponse(String accountName,
                           Double openingBalance,
                           String openingDate,
                           Double currentBalance,
                           Long accountId,
                           Boolean bankOverdraftAllowed,
                           String createdBy,
                           Date createdDate,
                           String lastModifiedBy,
                           Date lastModifiedDate) {
        super(accountName, openingBalance, openingDate, bankOverdraftAllowed);
        this.currentBalance = currentBalance;
        this.accountId = accountId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }
}
