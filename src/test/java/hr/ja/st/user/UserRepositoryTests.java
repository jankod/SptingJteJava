package hr.ja.st.user;

import hr.ja.st.user.domain.Roles;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.repo.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("findByUsername finds saved user")
    void findByUsername_ok() {
        User u = User.builder()
                .username("marko")
                .password("x")
                .enabled(true)
                .roles(new java.util.HashSet<>(List.of(Roles.USER)))
                .build();
        userRepository.saveAndFlush(u);

        assertThat(userRepository.findByUsername("marko")).isPresent();
    }

    @Test
    @DisplayName("unique username constraint enforced")
    void unique_username() {
        User a = User.builder().username("dup").password("x").enabled(true)
                .roles(new java.util.HashSet<>(List.of(Roles.USER))).build();
        User b = User.builder().username("dup").password("y").enabled(true)
                .roles(new java.util.HashSet<>(List.of(Roles.USER))).build();
        userRepository.saveAndFlush(a);
        assertThatThrownBy(() -> userRepository.saveAndFlush(b))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("prePersist sets createdAt and default role")
    void prePersist_defaults() {
        User u = User.builder()
                .username("iva")
                .password("x")
                .enabled(true)
                .roles(new java.util.HashSet<>()) // empty
                .build();
        User saved = userRepository.saveAndFlush(u);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getRoles()).contains(Roles.USER);
    }
}
