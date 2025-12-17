@param boolean error
@param boolean logout
@param String csrfParameterName
@param String csrfToken

@template.layout.base(title = "Prijava",
activePathPage = hr.ja.st.web.Routes.HOME,
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
showNavbar = false,
content = @`
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4 mt-5">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title text-center mb-4">Prijava</h2>

                    @if (logout)
                        <div class="alert alert-success" role="alert">
                            Uspješno ste se odjavili.
                        </div>
                    @endif

                    @if (error)
                        <div class="alert alert-danger" role="alert">
                            Neispravno korisničko ime ili lozinka.
                        </div>
                    @endif

                    <form method="post" action="/login">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}"/>
                        <div class="mb-3">
                            <label class="form-label" for="username">Korisničko ime</label>
                            <input class="form-control" id="username" name="username" type="text" required autofocus/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="password">Lozinka</label>
                            <input class="form-control" id="password" name="password" type="password" required/>
                        </div>
                        <div class="mb-3 form-check">
                            <input class="form-check-input" type="checkbox" id="remember-me" name="remember-me"/>
                            <label class="form-check-label" for="remember-me">Zapamti me</label>
                        </div>
                        <div class="d-grid gap-2">
                            <button class="btn btn-primary" type="submit">Prijavi se</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
`,
headExtra = @`
    <meta name="robots" content="noindex">
`)

