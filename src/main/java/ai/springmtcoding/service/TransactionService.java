package ai.springmtcoding.service;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.transaction.Transaction;
import ai.springmtcoding.domain.transaction.TransactionRepository;
import ai.springmtcoding.dto.transaction.TransactionRespDto.TransactionListRespDto;
import ai.springmtcoding.handler.ex.CustomApiException;
import ai.springmtcoding.util.CustomDateUtil;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionListRespDto withdrawDepositList(Long userId, Long accountNumber, String gubun,
        int page) {

        Account accountPS = accountRepository.findByNumber(accountNumber)
                                             .orElseThrow(
                                                 () -> new CustomApiException("해당 계좌를 찾을 수 없습니다."));

        accountPS.checkOwner(userId);

        List<Transaction> transactionListPS = transactionRepository.findTransactionList(
            accountPS.getId(), gubun, page);
        System.out.println("transactionListPS = " + transactionListPS);

        return new TransactionListRespDto(transactionListPS, accountPS);
    }


}
