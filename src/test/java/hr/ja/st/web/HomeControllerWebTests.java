package hr.ja.st.web;

import hr.ja.st.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerWebTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void about_is_publicly_accessible() throws Exception {
        // GET /about is publicly accessible and resolves the expected JTE view
        mockMvc.perform(get(Routes.ABOUT))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/about/index.jte"));
    }

    @Test
    void home_redirects_to_login_when_anonymous() throws Exception {
        // Anonymous users are redirected to the login page for the home route
        mockMvc.perform(get(Routes.HOME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void home_renders_view_for_authenticated_user() throws Exception {
        // With an authenticated USER, home renders the expected view and model
        mockMvc.perform(get(Routes.HOME))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/home/index.jte"))
                .andExpect(model().attributeExists("username"));
    }
}
