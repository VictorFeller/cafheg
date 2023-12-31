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
  public static <T> T inTransaction(Supplier<T> inTransaction) {
    logger.debug("inTransaction#start");
    try {
      logger.debug("inTransaction#getConnection");
      connection.set(dataSource.getConnection());
      T result = inTransaction.get();
      activeJDBCConnection().commit();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        logger.debug("inTransaction#closeConnection");
        connection.get().close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      logger.debug("inTransaction#end");
      connection.remove();
    }
  }

  // C'est un peu du détail, mais je pense pas que spécifier le type du paramètre soit une pratique
  // usuelle. On a déjà la signature qui le spécifie.
  public static void inTransactionNoReturn(Runnable inTransaction) {
    logger.debug("inTransaction#start");
    try {
      logger.debug("inTransaction#getConnection");
      connection.set(dataSource.getConnection());
      inTransaction.run();
      activeJDBCConnection().commit();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        logger.debug("inTransaction#closeConnection");
        connection.get().close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      logger.debug("inTransaction#end");
      connection.remove();
    }
  }

  public DataSource dataSource() {
    return dataSource;
  }

  /**
   * Initialisation du pool de connections.
   */
  public void start() {
    logger.debug("Initializing datasource");
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:h2:mem:sample");
    config.setMaximumPoolSize(20);
    config.setDriverClassName("org.h2.Driver");
    dataSource = new HikariDataSource(config);
    logger.debug("Datasource initialized");
  }
}
