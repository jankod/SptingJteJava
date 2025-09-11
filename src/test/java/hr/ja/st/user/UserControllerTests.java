package hr.ja.st.user;

import hr.ja.st.user.domain.Roles;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.repo.UserRepository;
import hr.ja.st.user.web.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({TestConfig.class})
class UserControllerTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(userRepository, passwordEncoder);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /users returns view and model with users")
    void listUsers_ok() throws Exception {
        given(userRepository.findAll()).willReturn(List.of());
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/user/list.jte"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /users validation: password mismatch returns form with errors")
    void createUser_password_mismatch() throws Exception {
        given(userRepository.findByUsername("ivana")).willReturn(Optional.empty());
        mvc.perform(post("/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "ivana")
                        .param("password", "tajna123")
                        .param("confirmPassword", "razlicito")
                        .param("enabled", "true")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/user/new.jte"))
                .andExpect(model().attributeExists("errors"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /users validation: duplicate username")
    void createUser_duplicate_username() throws Exception {
        given(userRepository.findByUsername("marko")).willReturn(Optional.of(User.builder().id(1L).username("marko").build()));
        mvc.perform(post("/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "marko")
                        .param("password", "tajna123")
                        .param("confirmPassword", "tajna123")
                        .param("enabled", "true")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/user/new.jte"))
                .andExpect(model().attributeExists("errors"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /users success: saves encoded password and redirects")
    void createUser_success() throws Exception {
        given(userRepository.findByUsername("ana")).willReturn(Optional.empty());
        given(passwordEncoder.encode("lozinka123")).willReturn("ENCODED");

        mvc.perform(post("/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", " ana ") // test trim
                        .param("password", "lozinka123")
                        .param("confirmPassword", "lozinka123")
                        .param("enabled", "true")
                        .param("roles", "ROLE_USER")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("ana");
        assertThat(saved.getPassword()).isEqualTo("ENCODED");
        assertThat(saved.getRoles()).contains(Roles.USER);
        assertThat(saved.isEnabled()).isTrue();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /users/{id}/edit returns view with user")
    void editForm_ok() throws Exception {
        User u = User.builder().id(5L).username("iva").enabled(true).roles(new java.util.HashSet<>(List.of(Roles.USER))).build();
        given(userRepository.findById(5L)).willReturn(Optional.of(u));

        mvc.perform(get("/users/5/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/user/edit.jte"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /users/{id}/edit updates fields and redirects")
    void updateUser_ok() throws Exception {
        User u = User.builder().id(2L).username("old").enabled(false)
                .roles(new java.util.HashSet<>(List.of(Roles.USER)))
                .password("OLD")
                .build();
        given(userRepository.findById(2L)).willReturn(Optional.of(u));
        given(passwordEncoder.encode("newpass")).willReturn("ENC_NEW");

        mvc.perform(post("/users/2/edit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "new")
                        .param("enabled", "true")
                        .param("roles", Roles.USER)
                        .param("newPassword", "newpass")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("new");
        assertThat(saved.isEnabled()).isTrue();
        assertThat(saved.getRoles()).containsExactlyInAnyOrder(Roles.USER);
        assertThat(saved.getPassword()).isEqualTo("ENC_NEW");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /users/{id}/delete deletes and redirects")
    void deleteUser_ok() throws Exception {
        mvc.perform(post("/users/9/delete").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userRepository).deleteById(9L);
    }
}

@TestConfiguration
class TestConfig {
    @org.springframework.context.annotation.Bean
    UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @org.springframework.context.annotation.Bean
    PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }
}
