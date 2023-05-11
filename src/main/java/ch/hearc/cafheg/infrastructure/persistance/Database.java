package ch.hearc.cafheg.infrastructure.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.sql.DataSource;

public class Database {
  /** Pool de connections JDBC */
  private static DataSource dataSource;

  /** Connection JDBC active par utilisateur/thread (ThreadLocal) */
  private static final ThreadLocal<Connection> connection = new ThreadLocal<>();
  private static final Logger logger = LoggerFactory.getLogger(Database.class);

  /**
   * Retourne la transaction active ou throw une Exception si pas de transaction
   * active.
   * @return Connection JDBC active
   */
  static Connection activeJDBCConnection() {
    if(connection.get() == null) {
      throw new RuntimeException("Pas de connection JDBC active");
    }
    return connection.get();
  }

  /**
   * Exécution d'une fonction dans une transaction.
   * @param inTransaction La fonction a éxécuter au travers d'une transaction
   * @param <T> Le type du retour de la fonction
   * @return Le résultat de l'éxécution de la fonction
   */
  public static <T> T inSupplierTransaction(Supplier<T> inTransaction) {
    System.out.println("inTransaction#start");
    try {
      System.out.println("inTransaction#getConnection");
      connection.set(dataSource.getConnection());
      T result = inTransaction.get();
      activeJDBCConnection().commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        System.out.println("inTransaction#closeConnection");
        connection.get().close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      System.out.println("inTransaction#end");
      connection.remove();
    }
  }

  public static void inRunnableTransaction(Runnable inTransaction) {
    System.out.println("inTransaction#start");
    try {
      System.out.println("inTransaction#getConnection");
      connection.set(dataSource.getConnection());
      inTransaction.run();
      activeJDBCConnection().commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        System.out.println("inTransaction#closeConnection");
        connection.get().close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      System.out.println("inTransaction#end");
      connection.remove();
    }
  }

  DataSource dataSource() {
    return dataSource;
  }

  /**
   * Initialisation du pool de connections.
   */
  public void start() {
    System.out.println("Initializing datasource");
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:h2:mem:sample");
    config.setMaximumPoolSize(20);
    config.setDriverClassName("org.h2.Driver");
    dataSource = new HikariDataSource(config);
    System.out.println("Datasource initialized");
  }
}
