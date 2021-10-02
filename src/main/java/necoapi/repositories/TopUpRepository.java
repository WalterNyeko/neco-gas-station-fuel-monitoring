package necoapi.repositories;

import necoapi.models.Account;
import necoapi.models.TopUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUpRepository extends JpaRepository<TopUp, Long> {
    List<TopUp> findByAccount(Account account);
}
