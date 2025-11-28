@param hr.ja.st.form.meta.FormMeta meta
@param hr.ja.st.project.web.dto.ProjectForm form
@param org.springframework.validation.BindingResult br
@param java.util.Map<String, String> values
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(
title = "New Project",
activePathPage = hr.ja.st.web.Routes.PROJECTS,
content = @`
    <h2 class="mb-3">New Project</h2>
    <form action="${meta.action}" method="${meta.method}" class="${meta.cssClass}">
        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>
        @for (hr.ja.st.form.meta.FieldMeta f : meta.fields)
            @template.components.form.field(f=f, br= br, value= values.get(f.name))

        @endfor
        <div class="mt-3">
            <button type="submit" class="btn btn-primary">Save</button>
            <a href="${hr.ja.st.web.Routes.PROJECTS}" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
`
)
