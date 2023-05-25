package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOToAllocataire;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireToAllocataireDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
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
        Migrations migrations = new Migrations(database, true);
        database.start();
        migrations.start();
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
            VersementMapper versementMapper = new VersementMapper();
            AllocataireService allocataireService = new AllocataireService(new AllocataireMapper(versementMapper), null, null, versementMapper);
            Database.inRunnableTransaction(() -> allocataireService.deleteById(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update_GivenAllocataireNameX_ShouldBeZ() {
        try {
            VersementMapper versementMapper = new VersementMapper();
            AllocataireDTO allocataireDTO = new AllocataireDTO(new NoAVS("756.1558.5343.91"), "DeguzmanUpdated", "Kendrick1", null, false, false, null, null, null);
            AllocataireService allocataireService = new AllocataireService(new AllocataireMapper(versementMapper), new AllocataireToAllocataireDTO(), new AllocataireDTOToAllocataire(), versementMapper);
            AllocataireDTO updateAllocataire = Database.inSupplierTransaction(() -> allocataireService.update(allocataireDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
