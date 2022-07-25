package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByChatId(String chatId) {
        return userRepository.findByChatId(chatId);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
