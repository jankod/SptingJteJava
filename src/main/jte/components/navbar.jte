@import hr.ja.st.PageController
@param String activePath


<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
        <a class="navbar-brand" href="/">MojaApp</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                @template.components.navItem(
                label = "Home",
                currentPath = PageController.HOME,
                activePath = activePath
                )
                @template.components.navItem(
                label = "O nama",
                currentPath  = PageController.ABOUT,
                activePath = activePath
                )

            </ul>
        </div>
    </div>
</nav>
