@param hr.ja.st.form.meta.FormMeta meta
@param hr.ja.st.project.web.dto.ProjectForm form
@param org.springframework.validation.BindingResult br
@param java.util.Map<String, String> values
@param Long projectId
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(
title = "Edit Project",
activePathPage = hr.ja.st.web.Routes.PROJECTS,
content = @`
    <h2 class="mb-3">Edit Project</h2>
    <form action="${hr.ja.st.web.Routes.PROJECTS}/${projectId}/edit" method="post" class="${meta.cssClass}">
        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>
        @for (hr.ja.st.form.meta.FieldMeta f : meta.fields)
            @template.components.form.field(f= f, br= br,value= values.get(f.name))
        @endfor
        <div class="mt-3 d-flex align-items-center">
            <button type="submit" class="btn btn-primary me-2">Save</button>
            <a href="${hr.ja.st.web.Routes.PROJECTS}" class="btn btn-secondary me-auto">Cancel</a>
            <form method="post" action="${hr.ja.st.web.Routes.PROJECTS}/${projectId}/delete">
                <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>
                <button type="submit" class="btn btn-outline-danger" onclick="return confirm('Delete project?')">
                    Delete
                </button>
            </form>
        </div>
    </form>
`
)
