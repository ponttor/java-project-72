@import gg.jte.Content
@param Content content

<!doctype html>
<html lang="ru">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Анализатор страниц</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9Oer+R4lv3a2B6jizoU8yWh5yYDVZ13zZ1r7M"
      crossorigin="anonymous"
    >
  </head>
  <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="/">Анализатор страниц</a>
      </div>
    </nav>
    <main class="container py-5">
      ${content}
    </main>
  </body>
</html>
