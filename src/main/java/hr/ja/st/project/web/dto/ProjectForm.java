package hr.ja.st.project.web.dto;

import hr.ja.st.form.FieldType;
import hr.ja.st.form.annotations.UiField;
import hr.ja.st.form.annotations.UiForm;
import hr.ja.st.form.annotations.UiOptions;
import hr.ja.st.project.web.options.UserOptions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UiForm(action = "/projects")
public class ProjectForm {

    @UiField(label = "Name", type = FieldType.TEXT, order = 1, placeholder = "Project name")
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @UiField(label = "Description", type = FieldType.TEXTAREA, order = 2, placeholder = "Short description")
    private String description;

    @UiField(label = "Owner", type = FieldType.SELECT, order = 3)
    @UiOptions(provider = UserOptions.class, valueField = "id", labelField = "username")
    @NotNull
    private Long ownerId;
}

