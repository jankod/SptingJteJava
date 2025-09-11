@param String username
@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(title = "Home",
activePathPage = hr.ja.st.web.Routes.HOME,
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
content = @`
    <div class="row">
        <div class="col-lg-8">
            <h1 class="display-6 mb-3">Bok, ${username}!</h1>
            <p class="lead">Ovo je početna stranica renderirana kroz JTE layout + Bootstrap.</p>
            <a class="btn btn-primary" href="/">Saznaj više</a>
        </div>
    </div>
`,
headExtra = @`
    <meta name="description" content="Početna stranica aplikacije">
`,
scripts = @`
    <script>
        console.log("Home loaded");
    </script>
`
)
