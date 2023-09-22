package ai.springmtcoding.domain.transaction;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

interface Dao {

    List<Transaction> findTransactionList(
        @Param("accountId") Long accountId,
        @Param("gubun") String gubun,
        @Param("page") Integer page);
}

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao {

    private final EntityManager em;

    @Override
    public List<Transaction> findTransactionList(
        final Long accountId,
        final String gubun,
        final Integer page) {

        // 동적쿼리 (gubun = DEPOSIT or WITHDRAW, ALL)
        String sql = "";
        sql += "select t from Transaction t ";

        if (gubun.equals("WITHDRAW")) {
            sql += "join fetch t.withdrawAccount wa ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId";
        } else if (gubun.equals("DEPOSIT")) {
            sql += "join fetch t.depositAccount da ";
            sql += "where t.depositAccount.id = :depositAccountId";
        } else { // gubun = ALL
            sql += "left join t.withdrawAccount wa "; // 1,3,4,5
            sql += "left join t.depositAccount da "; // 3,4,5
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
            sql += "or ";
            sql += "t.depositAccount.id = :depositAccountId";
        }

        TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);

        if (gubun.equals("WITHDRAW")) {
            query = query.setParameter("withdrawAccountId", accountId);
        } else if (gubun.equals("DEPOSIT")) {
            query = query.setParameter("depositAccountId", accountId);
        } else {
            query = query.setParameter("withdrawAccountId", accountId);
            query = query.setParameter("depositAccountId", accountId);
        }

        query.setFirstResult(page * 5); // 5, 10, 15
        query.setMaxResults(5);

        return query.getResultList();
    }
}
