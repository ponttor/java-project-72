package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static Url save(Url url) {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, url.getName());
            statement.setTimestamp(2, Timestamp.valueOf(url.getCreatedAt()));
            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    url.setId(generatedKeys.getLong(1));
                }
            }

            return url;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save URL", e);
        }
    }

    public static Optional<Url> find(Long id) {
        var sql = "SELECT id, name, created_at FROM urls WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUrl(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find URL", e);
        }
    }

    public static List<Url> findAll() {
        var sql = "SELECT id, name, created_at FROM urls ORDER BY id DESC";
        var urls = new ArrayList<Url>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                urls.add(buildUrl(resultSet));
            }

            return urls;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find URLs", e);
        }
    }

    public static Optional<Url> findByName(String name) {
        var sql = "SELECT id, name, created_at FROM urls WHERE name = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUrl(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find URL by name", e);
        }
    }

    private static Url buildUrl(java.sql.ResultSet resultSet) throws SQLException {
        var url = new Url(resultSet.getString("name"));
        url.setId(resultSet.getLong("id"));
        url.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return url;
    }
}
