package database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class JdbcSession {

    private static final PGSimpleDataSource PG_DATASOURCE = new PGSimpleDataSource();
    private static final Lock LOCK = new ReentrantLock();

    private static volatile Connection connection;

    static {
        PG_DATASOURCE.setURL("jdbc:postgresql://localhost:5432/jakartadb");
        PG_DATASOURCE.setCurrentSchema("bookstore");
        PG_DATASOURCE.setUser("postgres");
        PG_DATASOURCE.setPassword("postgres");
    }

    public static void deleteInBatch(final Table... tables) {
        withTransaction(statement -> {
            for (var table : tables) {
                statement.addBatch("DELETE FROM " + table.name);
            }
            statement.executeBatch();
        });
    }

    private static void withTransaction(final StatementCallback callback) {
        withConnection(conn -> {
            try (Statement statement = conn.createStatement()) {
                callback.call(statement);
            }
        }, true);
    }

    private static void withConnection(final ConnectionCallback callback,
                                       final boolean transactional) {
        try {
            LOCK.lock();
            if (connection == null || connection.isClosed()) {
                open();
            }
            if (transactional) {
                connection.setAutoCommit(false);
            }
            callback.call(connection);
            if (transactional) {
                connection.commit();
            }
        } catch (SQLException ex) {
            if (transactional) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {}
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setSchema(PG_DATASOURCE.getCurrentSchema());
            } catch (SQLException ignored) {}

            LOCK.unlock();
        }
    }

    @FunctionalInterface
    interface StatementCallback {
        void call(Statement statement) throws SQLException;
    }

    @FunctionalInterface
    interface ConnectionCallback {
        void call(Connection connection) throws SQLException;
    }

    static void open() {
        try {
            if ((connection != null && !connection.isClosed()) || connection != null) {
                throw new IllegalStateException("The connection is already open.");
            }
            connection = PG_DATASOURCE.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    static void close() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException(
                    "The session must be open to be closed."
                );
            }
            connection.close();
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            connection = null;
        }
    }
}
