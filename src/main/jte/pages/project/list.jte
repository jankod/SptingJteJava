@param java.util.List<hr.ja.st.domain.Project> projects
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(
title = "Projects",
activePathPage = hr.ja.st.web.Routes.PROJECTS,
content = @`
    <div class="d-flex align-items-center mb-3">
        <h2 class="me-auto">Projects</h2>
        <a href="${hr.ja.st.web.Routes.PROJECTS}/new" class="btn btn-primary">New Project</a>
    </div>
    <div class="card">
        <div class="table-responsive">
            <table class="table table-vcenter">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Owner</th>
                    <th>Created</th>
                    <th class="w-1"></th>
                </tr>
                </thead>
                <tbody>
                @for (hr.ja.st.project.domain.Project p : projects)
                    <tr>
                        <td>${p.getName()}</td>
                        <td>${p.owner().map(u->u.getUsername()).orElse("-")}</td>
                        <td>${String.valueOf(p.getCreatedAt())}</td>
                        <td class="text-nowrap">
                            <a class="btn  btn-sm btn-outline-primary" href="${hr.ja.st.web.Routes.PROJECTS}/${p.getId()}/edit">Edit</a>
                            <form method="post" action="${hr.ja.st.web.Routes.PROJECTS}/${p.getId()}/delete"
                                  class="d-inline">
                                <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>
                                <button type="submit" class="btn btn-sm btn-outline-danger"
                                        onclick="return confirm('Delete project?')">Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
`
)
