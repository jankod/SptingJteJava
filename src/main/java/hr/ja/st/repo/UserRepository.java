package hr.ja.st.repo;

import hr.ja.st.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT new hr.ja.st.domain.User(u.id, u.username) FROM User u ORDER BY u.username ASC")
    List<User> findAllUsers();
}
