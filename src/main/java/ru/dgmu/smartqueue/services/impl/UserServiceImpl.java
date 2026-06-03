package ru.dgmu.smartqueue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.exceptions.AuthenticationFailedException;
import ru.dgmu.smartqueue.repositories.UserRepository;
import ru.dgmu.smartqueue.services.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  @Override
  public User create(User user) {
    if (repository.existsByUsername(user.getUsername())) {
      throw new AuthenticationFailedException("Пользователь с таким именем уже существует");
    }
    return repository.save(user);
  }

  public UserDetailsService userDetailsService() {
    return this::getByUsername;
  }

  private User getByUsername(String username) {
    return repository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

  }
}
