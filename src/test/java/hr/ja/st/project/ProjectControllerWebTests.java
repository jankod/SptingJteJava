package hr.ja.st.project;

import hr.ja.st.project.domain.Project;
import hr.ja.st.project.domain.ProjectRole;
import hr.ja.st.project.repo.ProjectRepository;
import hr.ja.st.project.web.ProjectController;
import hr.ja.st.security.SecurityConfig;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(controllers = ProjectController.class)
@Import(SecurityConfig.class)
class ProjectControllerWebTests {

    @Autowired MockMvc mockMvc;

    @MockitoBean ProjectRepository projectRepository;
    @MockitoBean UserRepository userRepository;
    @MockitoBean MessageSource messageSource;

    @Test
    @WithMockUser(roles = "USER")
    void new_project_form_renders() throws Exception {
        when(messageSource.getMessage(any(), any(), any(), any(Locale.class))).then(inv -> inv.getArgument(2));
        mockMvc.perform(get("/projects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/project/new.jte"))
                .andExpect(model().attributeExists("meta"));
    }

    @Test
    @WithMockUser(username = "marko", roles = "USER")
    void delete_allowed_for_owner() throws Exception {
        User owner = User.builder().id(1L).username("marko").password("x").enabled(true).roles(new java.util.HashSet<>(List.of("USER"))).build();
        when(userRepository.findByUsername("marko")).thenReturn(Optional.of(owner));
        Project p = Project.builder().id(5L).name("P1").build();
        p.setOwner(owner);
        when(projectRepository.findById(5L)).thenReturn(Optional.of(p));

        mockMvc.perform(post("/projects/5/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    @WithMockUser(username = "anna", roles = "USER")
    void delete_forbidden_for_non_owner() throws Exception {
        User owner = User.builder().id(1L).username("marko").password("x").enabled(true).roles(new java.util.HashSet<>(List.of("USER"))).build();
        User anna = User.builder().id(2L).username("anna").password("x").enabled(true).roles(new java.util.HashSet<>(List.of("USER"))).build();
        when(userRepository.findByUsername("anna")).thenReturn(Optional.of(anna));
        Project p = Project.builder().id(6L).name("P2").build();
        p.setOwner(owner);
        when(projectRepository.findById(6L)).thenReturn(Optional.of(p));

        mockMvc.perform(post("/projects/6/delete").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
