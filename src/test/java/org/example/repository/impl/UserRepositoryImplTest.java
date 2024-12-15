package org.example.repository.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.User;
import org.example.repository.UserEntityRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@Testcontainers
class UserRepositoryImplTest {


    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withInitScript("db-migration.SQL")
            .withPassword("postgres");

    private static HikariDataSource dataSource;
    private UserEntityRepository userRepository;

    @BeforeEach
    void setUpTable()  {
        postgreSQLContainer.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.setMaximumPoolSize(10); // Настройка пула соединений - не забудьте изменить
        dataSource = new HikariDataSource(config);

        userRepository = new UserEntityRepositoryImpl(dataSource);
    }

    @AfterEach
    void tearDownTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users RESTART IDENTITY;"); // Более эффективная очистка
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы users: " + e.getMessage());
        }
    }


    @Test
    void findAll()  {
        UUID uuid1 = UUID.randomUUID();
        User user1 = new User(uuid1, "User 1", "user1@example.com");
        UUID uuid2 = UUID.randomUUID();
        User user2 = new User(uuid2, "User 2", "user2@example.com");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void save() throws SQLException {
        User user = new User(UUID.randomUUID(), "Test User", "test@example.com");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());

        // Проверка наличия записи в базе данных

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setObject(1, savedUser.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next()); // проверяем наличие строки в БД
            }
        }
    }

    @Test
    void testSave_nullUser() {
        assertThrows(NullPointerException.class, () -> userRepository.save(null));
    }


    @Test
    void findById() throws SQLException {
        UUID testUUID = UUID.randomUUID();
        User user = new User(testUUID, "Test User", "test@example.com");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            //Создаем временную таблицу
            statement.execute("CREATE TEMP TABLE posts (id UUID PRIMARY KEY, title TEXT, content TEXT, user_id UUID)");

            userRepository.save(user);
            User foundUser = userRepository.findById(testUUID);
            assertNotNull(foundUser);
            assertEquals(user, foundUser); //  Проверка на равенство может потребовать переопределения equals() и hashCode() в классе User

            // Удаляем временную таблицу
            statement.execute("DROP TABLE posts");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Ошибка при создании/удалении временной таблицы posts: " + e.getMessage());
        }
    }

    @Test
    void findById_notFound()  {
        UUID testUUID = UUID.randomUUID();
        assertNull(userRepository.findById(testUUID));
    }

    @Test
    void testFindById_nullId() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.findById(null));
    }


    @Test
    void deleteById() throws SQLException {
        UUID testUUID = UUID.randomUUID();
        User user = new User(testUUID, "Test User", "test@example.com");
        userRepository.save(user);

        assertTrue(userRepository.deleteById(testUUID));

        // Проверка, что запись удалена
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setObject(1, testUUID);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next());
            }
        }
    }

    @Test
    void testDeleteById_nonExistingId()  {
        UUID testUUID = UUID.randomUUID();
        assertFalse(userRepository.deleteById(testUUID));
    }

    @AfterAll
    static void tearDownContainer() {
        postgreSQLContainer.stop();
        postgreSQLContainer.close(); // Закрываем контейнер после всех тестов
    }
}
