@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.NamedRoutes
@param UrlPage page

@template.layout(page = page, content = @`
  <h1>Сайт: ${page.getUrl().getName()}</h1>

  <form method="post" action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" class="my-4">
    <input type="submit" class="btn btn-primary" value="Запустить проверку">
  </form>

  <table class="table table-bordered table-hover mt-4">
    <tbody>
      <tr>
        <th>ID</th>
        <td>${page.getUrl().getId()}</td>
      </tr>
      <tr>
        <th>Имя</th>
        <td>${page.getUrl().getName()}</td>
      </tr>
      <tr>
        <th>Дата создания</th>
        <td>${page.getUrl().getCreatedAt().toString()}</td>
      </tr>
    </tbody>
  </table>

  <table data-test="checks" class="table table-bordered table-hover mt-4">
    <thead>
      <tr>
        <th>ID</th>
        <th>Код ответа</th>
        <th>h1</th>
        <th>title</th>
        <th>description</th>
        <th>Дата создания</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
`)
