@import gg.jte.Content

@param String title
@param String activePathPage
@param gg.jte.Content content
@param Content headExtra = null
@param Content scripts = null
@param String csrfParameterName = null
@param String csrfToken = null
@param boolean showNavbar = true


<!DOCTYPE html>
<html lang="hr">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>


    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/@tabler/core@1.4.0/dist/css/tabler.min.css"/>

    <!-- Slot za per-page HEAD (ako postoji) -->
    @if (headExtra != null)
        ${headExtra}
    @endif
</head>
<body class="bg-light">

@if (showNavbar)
    @template.components.navbar(activePath = activePathPage, csrfParameterName = csrfParameterName, csrfToken = csrfToken)
@endif

<main class="container py-4">
    ${content}
</main>

<%--<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"--%>
<%--        integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"--%>
<%--        crossorigin="anonymous"></script>--%>
    <script
            src="https://cdn.jsdelivr.net/npm/@tabler/core@1.4.0/dist/js/tabler.min.js">
    </script>
<!-- Slot za per-page SCRIPTS (ako postoji) -->
@if (scripts != null)
    ${scripts}
@endif

@if (hr.ja.st.dev.DevToolbarConfig.ENABLED && hr.ja.st.dev.DebugContext.current() != null)
    @template.components.devPanel(debug = hr.ja.st.dev.DebugContext.current())
@endif
</body>
</html>
