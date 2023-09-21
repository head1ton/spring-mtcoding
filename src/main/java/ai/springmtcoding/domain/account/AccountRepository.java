package ai.springmtcoding.domain.account;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // select * from account where number = :number
    // TODO : 고객 관련 정보 가져와야하니.
    Optional<Account> findByNumber(Long number);

    List<Account> findByUserId(Long id);
}
