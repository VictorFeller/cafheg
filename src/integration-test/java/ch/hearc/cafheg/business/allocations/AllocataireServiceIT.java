package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.Database;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;


import javax.sql.DataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AllocataireServiceIT {

    public static final String ALLOCATAIRES = "ALLOCATAIRES";
    DataSource dataSource;
    IDatabaseConnection connection;
    IDataSet dataset;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        database.start();
        dataSource = database.dataSource();
        try {
            connection = new DatabaseConnection(dataSource.getConnection());
            dataset = new FlatXmlDataSetBuilder().build(getClass()
                    .getClassLoader().getResourceAsStream("default-data.xml"));
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete_Given5Allocataires_ShouldHave4AllocatairesAfterRemoval() {
        try {
            //DatabaseOperation.DELETE.execute(connection, dataset.getTable(ALLOCATAIRES).g);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
