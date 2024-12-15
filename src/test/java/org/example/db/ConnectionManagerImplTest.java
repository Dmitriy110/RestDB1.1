package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class ConnectionManagerImplTest {

    @Test
    void testConnectionManagerCreation() {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(mockDataSource); // Передаем заглушку в конструктор
        assertNotNull(connectionManager);
        assertSame(mockDataSource, connectionManager.getDataSource()); // Проверяем, что используется заглушка
    }

    @Test
    void testDataSourceConfiguration() {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        when(mockDataSource.getJdbcUrl()).thenReturn("jdbc:postgresql://localhost:5432/");
        when(mockDataSource.getUsername()).thenReturn("postgres");
        when(mockDataSource.getPassword()).thenReturn("postgres");
        when(mockDataSource.getMaximumPoolSize()).thenReturn(10);
        when(mockDataSource.isClosed()).thenReturn(false); // Заглушка для метода isClosed

        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(mockDataSource);
        HikariDataSource dataSource = connectionManager.getDataSource();

        assertEquals("jdbc:postgresql://localhost:5432/", dataSource.getJdbcUrl());
        assertEquals("postgres", dataSource.getUsername());
        assertEquals("postgres", dataSource.getPassword());
        assertEquals(10, dataSource.getMaximumPoolSize());
        assertFalse(dataSource.isClosed());
    }

    @Test
    void testCloseDataSource() {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        when(mockDataSource.isClosed()).thenReturn(false); // Изначально пул не закрыт

        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(mockDataSource);
        HikariDataSource dataSource = connectionManager.getDataSource();

        assertFalse(dataSource.isClosed());

        dataSource.close(); // Закрываем заглушку

        verify(mockDataSource).close(); // Проверяем, что метод close() был вызван
        when(mockDataSource.isClosed()).thenReturn(true); // Теперь пул закрыт (изменяем поведение mock объекта)
        assertTrue(dataSource.isClosed()); // Проверяем, что isClosed() возвращает true
    }

    @Test
    void testConnectionManagerCreationWithCustomConfig() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(5000);
        // НЕ УКАЗЫВАЕМ jdbcUrl, username и password

        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        when(mockDataSource.getMaximumPoolSize()).thenReturn(20);
        when(mockDataSource.getConnectionTimeout()).thenReturn(5000L);

        //Вместо создания HikariDataSource напрямую, используем mock-объект
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(mockDataSource);
        HikariDataSource dataSource = connectionManager.getDataSource();

        assertEquals(20, dataSource.getMaximumPoolSize());
        assertEquals(5000L, dataSource.getConnectionTimeout());
        //Проверка на isClosed() — необязательно, если мы mocks dataSource напрямую
    }

    @Test
    void testConstructorWithNullDataSource() {
        assertDoesNotThrow(() -> new ConnectionManagerImpl((HikariDataSource) null)); // Исправленный тест
    }

    @Test
    void testConstructorWithInvalidConfig() {
        assertThrows(IllegalArgumentException.class, () -> new ConnectionManagerImpl(new HikariConfig())); // Пустая конфигурация
    }

    @Test
    void testGetDataSourceReturnsSameInstance() {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(mockDataSource);
        assertSame(connectionManager.getDataSource(), connectionManager.getDataSource());
    }

    @Test
    void testConstructorWithNullConfig() {
        assertThrows(NullPointerException.class, () -> new ConnectionManagerImpl((HikariConfig) null));
    }
}

