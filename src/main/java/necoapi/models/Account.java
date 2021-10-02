package necoapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.helpers.Auditable;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "account")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE account_id=?")
@Where(clause = "deleted=false")
public class Account extends Auditable<String> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "account_name", unique=true)
    private String accountName;

    @Column(name = "opening_balance")
    private Double openingBalance;

    @Column(name = "opening_date")
    private String openingDate;

    @Column(name = "current_balance")
    private Double currentBalance;

    @Column(name = "bank_overdraft_allowed")
    private Boolean bankOverdraftAllowed;

    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;
}
