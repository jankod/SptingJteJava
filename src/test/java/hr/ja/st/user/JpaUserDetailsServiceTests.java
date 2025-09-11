package hr.ja.st.user;

import hr.ja.st.security.auth.JpaUserDetailsService;
import hr.ja.st.user.domain.Roles;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.repo.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class JpaUserDetailsServiceTests {

    @Test
    @DisplayName("loads authorities and enabled flag from User")
    void loadUser_ok() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        User u = User.builder()
                .username("ana")
                .password("ENC")
                .enabled(false)
                .roles(new HashSet<>(List.of(Roles.USER, Roles.ADMIN)))
                .build();
        when(repo.findByUsername("ana")).thenReturn(Optional.of(u));

        JpaUserDetailsService svc = new JpaUserDetailsService(repo);
        UserDetails ud = svc.loadUserByUsername("ana");
        assertThat(ud.getUsername()).isEqualTo("ana");
        assertThat(ud.getPassword()).isEqualTo("ENC");
        assertThat(ud.getAuthorities()).extracting("authority")
                .containsExactlyInAnyOrder(Roles.ADMIN, Roles.USER);
        assertThat(ud.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("throws when user not found")
    void loadUser_not_found() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        when(repo.findByUsername("missing")).thenReturn(Optional.empty());
        JpaUserDetailsService svc = new JpaUserDetailsService(repo);
        assertThatThrownBy(() -> svc.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
