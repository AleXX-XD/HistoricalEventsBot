package HistoricalEventsBotApi.repository;

import HistoricalEventsBotApi.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByChatId(String chatId);
    @Query(value = "SELECT u FROM User u WHERE u.subscription = true")
    List<User> findAllBySubscriptionIsTrue();
    @Query(value = "SELECT u FROM User u WHERE u.isAdmin = true")
    List<User> findAllByAdmin();
    long countAllByActiveTrue();
    long countAllBySubscriptionTrue();


}
