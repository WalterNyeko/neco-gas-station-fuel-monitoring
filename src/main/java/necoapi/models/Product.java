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
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE product_id=?")
@Where(clause = "deleted=false")
public class Product extends Auditable<String> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", unique=true)
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_unit_price")
    private Double productUnitPrice;

    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;
}
