@template.layout.base(title = "About",
activePathPage = hr.ja.st.PageController.ABOUT,
content = @`
    <div class="row">
        <div class="col-lg-8">
            <h1 class="display-6 mb-3">About stranica</h1>
            <p class="lead">Ovo je početna stranica renderirana kroz JTE layout + Bootstrap.</p>
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

