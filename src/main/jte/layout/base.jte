@import gg.jte.Content

@param String title
@param String activePath
@param gg.jte.Content content
@param Content headExtra = null
@param Content scripts = null


<!DOCTYPE html>
<html lang="hr">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">


    <!-- Slot za per-page HEAD (ako postoji) -->
    @if (headExtra != null)
        ${headExtra}
    @endif
</head>
<body class="bg-light">

@template.components.navbar(activePath = activePath)

<main class="container py-4">
    ${content}
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous"></script>

<!-- Slot za per-page SCRIPTS (ako postoji) -->
@if (scripts != null)
    ${scripts}
@endif
</body>
</html>
