package ai.springmtcoding.domain.account;

import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.handler.ex.CustomApiException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "account_tb")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 4)    // TODO : 나중에 수정
    private Long number;    // 계좌번호

    @Column(nullable = false, length = 4)
    private Long password; // 계좌비번

    @Column(nullable = false)
    private Long balance;       // 잔액 (기본값 1000원)

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Account(
        final Long id,
        final Long number,
        final Long password,
        final Long balance,
        final User user,
        final LocalDateTime createdAt,
        final LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void checkOwner(final Long userId) {
        if (user.getId() != userId) {
            throw new CustomApiException("계좌 소유자가 아닙니다.");
        }
    }

    public void deposit(final Long amount) {
        balance = balance + amount;
    }

    public void checkSamePassword(final Long password) {
        if (this.password.longValue() != password.longValue()) {
            throw new CustomApiException("계좌 비밀번호 검증에 실패했습니다.");
        }
    }

    public void checkBalance(final Long amount) {
        if (this.balance < amount) {
            throw new CustomApiException("계좌 잔액이 부족합니다.");
        }
    }

    public void withdraw(final Long amount) {
        checkBalance(amount);
        balance = balance - amount;
    }
}
