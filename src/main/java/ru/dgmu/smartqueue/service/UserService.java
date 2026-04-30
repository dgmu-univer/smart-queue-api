package ru.dgmu.smartqueue.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.dgmu.smartqueue.entity.User;

public interface UserService {
    User create(User user);
    UserDetailsService userDetailsService();
}
