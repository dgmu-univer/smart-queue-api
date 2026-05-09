package ru.dgmu.smartqueue.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.dgmu.smartqueue.entites.User;

public interface UserService {
    User create(User user);
    UserDetailsService userDetailsService();
}
