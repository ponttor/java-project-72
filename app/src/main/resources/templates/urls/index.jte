@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.NamedRoutes
@param UrlsPage page

@template.layout(page = page, content = @`
  <h1>Сайты</h1>

  <table class="table table-bordered table-hover mt-4">
    <thead>
      <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Дата создания</th>
      </tr>
    </thead>
    <tbody>
      @for (var url : page.getUrls())
        <tr>
          <td>${url.getId()}</td>
          <td>
            <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
          </td>
          <td>${url.getCreatedAt().toString()}</td>
        </tr>
      @endfor
    </tbody>
  </table>
`)
