package necoapi.repositories;

import necoapi.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByAccountName(String accountName);

    @Query("SELECT a FROM Account a WHERE CONCAT(" +
            "a.accountName, a.createdDate, a.bankOverdraftAllowed, " +
            "a.createdBy, a.id, a.openingDate, a.lastModifiedDate, " +
            "a.lastModifiedBy) LIKE %?1%")
    List<Account> searchAccount(String keyword);
}
