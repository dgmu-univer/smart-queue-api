package ru.dgmu.smartqueue.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByPin(String pin);

  boolean existsByUsername(String username);
}
