package necoapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.helpers.APIConstants;
import necoapi.helpers.Auditable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transaction")
@SQLDelete(sql = "UPDATE transaction SET deleted = true WHERE transaction_id=?")
@Where(clause = "deleted=false")
public class Transaction extends Auditable<String> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = APIConstants.TRANSACTION_ACCOUNT_ID,
            referencedColumnName = APIConstants.ACCOUNT_ID)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;

    @Column(name = "transaction_amount")
    private Double transactionAmount;

    @Column(name = "transaction_date")
    private String transactionDate;

    @Column(name = "transaction_quantity")
    private Double transactionQuantity;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "order_number")
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = APIConstants.TRANSACTION_PRODUCT_ID,
            referencedColumnName = APIConstants.PRODUCT_ID)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;
}
