package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOToAllocataire;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireToAllocataireDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import javax.sql.DataSource;
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
            //Instanciation
            VersementMapper versementMapper = new VersementMapper();
            AllocataireService allocataireService = new AllocataireService(new AllocataireMapper(versementMapper), null, null, versementMapper);
            //Récup état avant l'action DB
            IDataSet databaseDataSet = connection.createDataSet();
            int rowCount = databaseDataSet.getTable(ALLOCATAIRES).getRowCount(); // retourne le nombre de ligne
            //Action DB
            Database.inRunnableTransaction(() -> allocataireService.deleteById(1));
            //Récupération du nb de lignes post-action
            int rowCountPostAction = databaseDataSet.getTable(ALLOCATAIRES).getRowCount(); // retourne le nombre de ligne
            //Assert
            assertEquals(rowCount-1, rowCountPostAction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update_GivenAllocataireNameX_ShouldBeZ() {
        try {
            //Instanciation
            VersementMapper versementMapper = new VersementMapper();
            AllocataireDTO allocataireDTO = new AllocataireDTO(new NoAVS("756.1558.5343.91"), "UpdatedName", "Kendrick1", null, false, false, null, null, null);
            AllocataireService allocataireService = new AllocataireService(new AllocataireMapper(versementMapper), new AllocataireToAllocataireDTO(), new AllocataireDTOToAllocataire(), versementMapper);
            //Action DB
            AllocataireDTO updateAllocataire = Database.inSupplierTransaction(() -> allocataireService.update(allocataireDTO));
            //Récupération état post-action
            IDataSet databaseDataSet = connection.createDataSet();
            ITable table = databaseDataSet.getTable(ALLOCATAIRES); //récup table
            String updatedName = null;
            for (int i = 0; i < table.getRowCount(); i++) { // itérer sur les rows pour trouver celle avec le bon num_avs
                if (table.getValue(i, "NO_AVS").equals("756.1558.5343.91")) {
                    updatedName = (String) table.getValue(i, "nom");  // retrourner le nom trouver
                    break;
                }
            }
            //Assert
            assertEquals("UpdatedName", updatedName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
