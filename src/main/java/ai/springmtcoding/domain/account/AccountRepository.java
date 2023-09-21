package ai.springmtcoding.domain.account;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // select * from account where number = :number
    // TODO : 고객 관련 정보 가져와야하니.
//    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u WHERE ac.number = :number")
    Optional<Account> findByNumber(@Param("number") Long number);

    List<Account> findByUserId(Long id);
}
