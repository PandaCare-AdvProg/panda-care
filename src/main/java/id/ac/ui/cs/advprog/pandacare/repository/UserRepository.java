package id.ac.ui.cs.advprog.pandacare.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import id.ac.ui.cs.advprog.pandacare.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
}
