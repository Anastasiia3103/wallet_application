package repository;

import entity.User;
import entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT SUM(w.balance) FROM Wallet w WHERE w.user = :user")
    BigDecimal getTotalBalanceByUser(User user);
}
