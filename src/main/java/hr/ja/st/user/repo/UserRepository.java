package hr.ja.st.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import hr.ja.st.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
