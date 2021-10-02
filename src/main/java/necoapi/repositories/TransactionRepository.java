package necoapi.repositories;

import necoapi.domain.TransactionResponse;
import necoapi.models.Account;
import necoapi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionByAccount(Account account);
}
