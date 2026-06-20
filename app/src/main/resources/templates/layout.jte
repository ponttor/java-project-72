@import gg.jte.Content
@import hexlet.code.dto.BasePage
@import hexlet.code.NamedRoutes
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="ru">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Анализатор страниц</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    >
  </head>
  <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="${NamedRoutes.rootPath()}">Анализатор страниц</a>
      </div>
    </nav>
    <main class="container py-5">
      @if (page != null && page.getFlash() != null && !page.getFlash().isBlank())
        <div class="alert alert-${page.getFlashType()}" role="alert">
          ${page.getFlash()}
        </div>
      @endif
      ${content}
    </main>
  </body>
</html>
