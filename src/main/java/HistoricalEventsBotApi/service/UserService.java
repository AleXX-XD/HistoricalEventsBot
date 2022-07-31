package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String chatId) {
        return userRepository.findByChatId(chatId);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getSubUsers() {
        return userRepository.findAllBySubscriptionIsTrue();
    }

    public List<User> getAdminUsers() {
        return userRepository.findAllByAdmin();
    }

    public long countAll() {
        return userRepository.count();
    }

    public long countActiveUsers() {
        return userRepository.countAllByActiveTrue();
    }

    public long countSubUsers() {
        return userRepository.countAllBySubscriptionTrue();
    }
}
