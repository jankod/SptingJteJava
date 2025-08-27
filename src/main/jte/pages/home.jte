@param String username

@template.layout.base(title = "Home",
activePath = "/",
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
