@import hr.ja.st.user.domain.User

@param User user
@param String[] allRoles
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(title = "Uredi korisnika",
activePathPage = "/users",
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
content = @`
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title mb-4">Uredi korisnika</h2>
                    <form method="post" action="/users/${user.getId()}/edit">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>

                        <div class="mb-3">
                            <label class="form-label" for="username">Korisničko ime</label>
                            <input class="form-control" id="username" name="username" type="text" value="${user.getUsername()}" required />
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Uloge</label>
                            @for (String r : allRoles)
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="role_${r}" name="roles" value="${r}" checked="${user.getRoles().contains(r)}" />
                                    <label class="form-check-label" for="role_${r}">${hr.ja.st.common.view.Formatters.roleLabel(r)}</label>
                                </div>
                            @endfor
                        </div>

                        <div class="mb-3 form-check">
                            <input class="form-check-input" type="checkbox" id="enabled" name="enabled" checked="${user.isEnabled()}" />
                            <label class="form-check-label" for="enabled">Aktivan</label>
                        </div>

                        <div class="mb-3">
                            <label class="form-label" for="newPassword">Nova lozinka (opcionalno)</label>
                            <input class="form-control" id="newPassword" name="newPassword" type="password" autocomplete="new-password" />
                        </div>

                        <div class="d-flex gap-2">
                            <button class="btn btn-primary" type="submit">Spremi</button>
                            <a class="btn btn-secondary" href="/users">Odustani</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
`)
