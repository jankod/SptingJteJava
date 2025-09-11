@param String csrfParameterName = null
@param String csrfToken = null

@template.layout.base(title = "About",
activePathPage = hr.ja.st.web.Routes.ABOUT,
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
content = @`
    <div class="row">
        <div class="col-lg-8">
            <h1 class="display-6 mb-3">About stranica</h1>
            <p class="lead">Ovo je početna stranica renderirana kroz JTE layout + Bootstrap.</p>

            Job run by <a href="http://localhost:8000/">dashboard</a>
        </div>
    </div>
`,
headExtra = @`
    <meta name="description" content="About">
`,
scripts = @`
    <script>
        console.log("About loaded");

    </script>
`
)
