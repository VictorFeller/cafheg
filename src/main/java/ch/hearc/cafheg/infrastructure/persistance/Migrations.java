package ch.hearc.cafheg.infrastructure.persistance;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gestion des scripts de migration sur la base de données.
 */
public class Migrations {

  private final Database database;
  private final boolean forTest;
  private static final Logger logger = LoggerFactory.getLogger(Migrations.class);

  public Migrations(Database database) {
    this.database = database;
    this.forTest = false;
  }

  public Migrations(Database database, boolean forTest) {
    this.database = database;
    this.forTest = forTest;
  }

  /**
   * Exécution des migrations
   */
  public void start() {
    logger.debug("Doing migrations");

    String location;
    // Pour les tests, on exécute seulement les scripts DDL (création de tables)
    // et pas les scripts d'insertion de données.
    if (forTest) {
      location = "classpath:db/ddl";
    } else {
      location = "classpath:db";
    }

    Flyway flyway = Flyway.configure()
            .dataSource(database.dataSource())
            .locations(location)
            .load();

    flyway.migrate();
    logger.debug("Migrations done");
  }
}
