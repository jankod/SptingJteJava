package hr.ja.st.security;

import hr.ja.st.security.web.LogoutController;
import hr.ja.st.security.web.SecurityController;
import hr.ja.st.user.repo.UserRepository;
import hr.ja.st.user.web.UserController;
import hr.ja.st.web.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {HomeController.class, SecurityController.class, LogoutController.class, UserController.class})
@Import(SecurityConfig.class)
class WebSecurityTests {

    @Autowired
    MockMvc mvc;

    // Slice provides only selected controllers; provide mocks for their collaborators
    @MockBean
    UserRepository userRepository;
    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Unauthenticated GET / is redirected to /login (method security)")
    void home_requires_authentication() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("/about is public")
    void about_is_public() throws Exception {
        mvc.perform(get("/about"))
              .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/users requires ADMIN role")
    @WithMockUser(roles = "USER") // not admin
    void users_requires_admin() throws Exception {
        mvc.perform(get("/users"))
              .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /login is accessible")
    void login_is_accessible() throws Exception {
        mvc.perform(get("/login"))
              .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /logout without CSRF is 403; with CSRF redirects")
    @WithMockUser
    void logout_csrf_enforced() throws Exception {
        mvc.perform(post("/logout"))
              .andExpect(status().isForbidden());

        mvc.perform(post("/logout").with(SecurityMockMvcRequestPostProcessors.csrf()))
              .andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/login?logout"));
    }
}
