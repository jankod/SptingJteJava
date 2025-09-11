@import hr.ja.st.user.web.dto.NewUserForm

@param NewUserForm form
@param String[] allRoles
@param java.util.List<String> errors = null
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(title = "Novi korisnik",
activePathPage = "/users",
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
content = @`
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title mb-4">Dodaj korisnika</h2>

                    @if (errors != null && !errors.isEmpty())
                        <div class="alert alert-danger" role="alert">
                            @for (String err : errors)
                                <div>${err}</div>
                            @endfor
                        </div>
                    @endif

                    <form method="post" action="/users">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>

                        <div class="mb-3">
                            <label class="form-label" for="username">Korisničko ime</label>
                            <input class="form-control" id="username" name="username" type="text" value="${form.getUsername() != null ? form.getUsername() : ""}" required />
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Uloge</label>
                            @for (String r : allRoles)
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="role_${r}" name="roles" value="${r}" checked?="${form.getRoles() != null && form.getRoles().contains(r)}" />
                                    <label class="form-check-label" for="role_${r}">${hr.ja.st.common.view.Formatters.roleLabel(r)}</label>
                                </div>
                            @endfor
                        </div>

                        <div class="mb-3 form-check">
                            <input class="form-check-input" type="checkbox" id="enabled" name="enabled" checked?="${form.isEnabled()}" />
                            <label class="form-check-label" for="enabled">Aktivan</label>
                        </div>

                        <div class="mb-3">
                            <label class="form-label" for="password">Lozinka</label>
                            <input class="form-control" id="password" name="password" type="password" minlength="8" autocomplete="new-password" required />
                            <div class="form-text">Minimalno 8 znakova. Koristi slova, brojeve i znakove.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label" for="confirmPassword">Potvrdi lozinku</label>
                            <input class="form-control" id="confirmPassword" name="confirmPassword" type="password" minlength="8" autocomplete="new-password" required />
                        </div>

                        <div class="d-flex gap-2">
                            <button class="btn btn-primary" type="submit">Kreiraj</button>
                            <a class="btn btn-secondary" href="/users">Odustani</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
`)
