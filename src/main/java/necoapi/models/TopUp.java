package necoapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.helpers.APIConstants;
import necoapi.helpers.Auditable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopUp extends Auditable<String> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Double previousBalance;
    private Double amount;
    private Double currentBalance;
    @ManyToOne
    @JoinColumn(name = APIConstants.TOPUP_ACCOUNT_ID,
            referencedColumnName = APIConstants.ACCOUNT_ID)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;
}
