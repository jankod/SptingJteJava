@import java.util.List
@import hr.ja.st.user.User

@param List<User> users
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(title = "Users",
activePathPage = "/users",
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
content = @`
    <div class="row">
        <div class="col-12">
            <h1 class="mb-3">Korisnici</h1>
            <div class="card">
                <div class="table-responsive">
                    <table class="table table-vcenter">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Korisničko ime</th>
                            <th>Uloge</th>
                            <th>Aktivan</th>
                            <th>Kreiran</th>
                            <th class="w-1">Akcije</th>
                        </tr>
                        </thead>
                        <tbody>
                        @for (User u : users)
                            <tr>
                                <td>${u.getId()}</td>
                                <td>${u.getUsername()}</td>
                                <td>${hr.ja.st.view.Formatters.rolesToLabel(u.getRoles())}</td>
                                <td>
                                    @if (u.isEnabled())
                                        <span class="badge bg-green">Da</span>
                                    @else
                                        <span class="badge bg-secondary">Ne</span>
                                    @endif
                                </td>
                                <td>${hr.ja.st.view.Formatters.instant(u.getCreatedAt())}</td>
                                <td class="text-nowrap">
                                    <a class="btn btn-sm btn-outline-primary" href="/users/${u.getId()}/edit">Uredi</a>
                                    <form method="post" action="/users/${u.getId()}/delete" style="display:inline-block" onsubmit="return confirm('Obrisati korisnika?');">
                                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Obriši</button>
                                    </form>
                                </td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
`)
