package hr.ja.st.project.web;

import hr.ja.st.form.FormMetaBuilder;
import hr.ja.st.form.meta.FormMeta;
import hr.ja.st.form.options.OptionsProvider;
import hr.ja.st.project.domain.Project;
import hr.ja.st.project.repo.ProjectRepository;
import hr.ja.st.project.web.dto.ProjectForm;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.repo.UserRepository;
import hr.ja.st.web.Routes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(Routes.PROJECTS)
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final List<OptionsProvider<?>> optionsProviders;

    @GetMapping
    public String list(Model model) {
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        return "pages/project/list.jte";
    }

    @GetMapping("/new")
    public String newProject(Model model, Locale locale) {
        ProjectForm form = new ProjectForm();
        FormMeta meta = new FormMetaBuilder(messageSource, locale, optionsProviders).build(form);
        model.addAttribute("form", form);
        model.addAttribute("meta", meta);
        model.addAttribute("values", extractValues(form));
        return "pages/project/new.jte";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ProjectForm form,
                         BindingResult br,
                         Model model,
                         Locale locale) {

        if(br.hasErrors()) {
            return  "pages/project/new.jte";
        }

        // name unique
        if (!br.hasFieldErrors("name") && projectRepository.existsByName(form.getName())) {
            br.rejectValue("name", "exists", "Name already exists");
        }
        // owner exists
        Optional<User> owner = Optional.empty();
        if (!br.hasFieldErrors("ownerId")) {
            owner = userRepository.findById(form.getOwnerId());
            if (owner.isEmpty()) br.rejectValue("ownerId", "notfound", "Owner not found");
        }

        if (br.hasErrors()) {
            FormMeta meta = new FormMetaBuilder(messageSource, locale, optionsProviders).build(form);
            model.addAttribute("meta", meta);
            model.addAttribute("values", extractValues(form));
            return "pages/project/new.jte";
        }

        Project p = Project.builder()
                .name(form.getName().trim())
                .description(form.getDescription())
                .build();
        owner.ifPresent(p::setOwner);
        projectRepository.save(p);
        return "redirect:" + Routes.PROJECTS;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, Locale locale) {
        Project p = projectRepository.findById(id).orElseThrow();
        ProjectForm form = new ProjectForm();
        form.setName(p.getName());
        form.setDescription(p.getDescription());
        p.owner().ifPresent(u -> form.setOwnerId(u.getId()));
        FormMeta meta = new FormMetaBuilder(messageSource, locale, optionsProviders).build(form);
        model.addAttribute("form", form);
        model.addAttribute("meta", meta);
        model.addAttribute("values", extractValues(form));
        model.addAttribute("projectId", id);
        return "pages/project/edit.jte";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ProjectForm form,
                         BindingResult br,
                         Model model,
                         Locale locale) {
        Project p = projectRepository.findById(id).orElseThrow();

        // uniqueness check if name changed
        if (!p.getName().equals(form.getName()) && projectRepository.existsByName(form.getName())) {
            br.rejectValue("name", "exists", "Name already exists");
        }
        Optional<User> owner = Optional.empty();
        if (!br.hasFieldErrors("ownerId")) {
            owner = userRepository.findById(form.getOwnerId());
            if (owner.isEmpty()) br.rejectValue("ownerId", "notfound", "Owner not found");
        }
        if (br.hasErrors()) {
            FormMeta meta = new FormMetaBuilder(messageSource, locale, optionsProviders).build(form);
            model.addAttribute("meta", meta);
            model.addAttribute("values", extractValues(form));
            model.addAttribute("projectId", id);
            return "pages/project/edit.jte";
        }
        p.setName(form.getName().trim());
        p.setDescription(form.getDescription());
        owner.ifPresent(p::setOwner);
        projectRepository.save(p);
        return "redirect:" + Routes.PROJECTS;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {
        Project p = projectRepository.findById(id).orElseThrow();
        String username = auth.getName();
        Long currentUserId = userRepository.findByUsername(username).map(User::getId).orElse(null);
        if (currentUserId == null || !p.isOwner(currentUserId)) {
            throw new AccessDeniedException("Only owner can delete project");
        }
        projectRepository.delete(p);
        return "redirect:" + Routes.PROJECTS;
    }

    private Map<String, String> extractValues(Object form) {
        Map<String, String> values = new HashMap<>();
        for (var fld : form.getClass().getDeclaredFields()) {
            try {
                fld.setAccessible(true);
                Object v = fld.get(form);
                values.put(fld.getName(), v == null ? "" : String.valueOf(v));
            } catch (Exception ignored) {}
        }
        return values;
    }
}
