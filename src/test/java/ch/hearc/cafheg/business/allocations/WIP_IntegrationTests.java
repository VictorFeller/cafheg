
package ch.hearc.cafheg.business.allocations;


public class WIP_IntegrationTests {

}
/*

OLD TEST TO KEEP FOR INTEGRATION EXERCICE
package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;

import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

public class AllocataireServiceTest {

    private Allocataire allocataire;
    private VersementMapper versementMapper;
    private AllocataireMapper allocataireMapper;

    @BeforeEach
    void setUp() {
        //Simuler data de prod
        Database testDatabase = new Database();
        Migrations testMigrations = new Migrations(testDatabase);
        testDatabase.start();
        testMigrations.start();
        //Instenciation des mappers
        versementMapper = new VersementMapper();
        allocataireMapper = new AllocataireMapper(versementMapper);
    }

    @Test
    void GetAllocataire() {
        List<Allocataire> allAllocataires = Database.inSupplierTransaction(() -> allocataireMapper.findAll());
        assertFalse(allAllocataires.isEmpty());
    }


    @Test
    void testSuppression() {

        //suppression allocataire2 OK
        assertEquals("Allocataire supprimé", Database.inSupplierTransaction(() -> allocataireMapper.deleteById(21)));
        // No data found
        assertThrows(RuntimeException.class, () -> Database.inSupplierTransaction(() -> allocataireMapper.findById(21)));

        //suppression allocataire NotOK
        assertEquals("Pas possible de supprimer", Database.inSupplierTransaction(() -> allocataireMapper.deleteById(1)));
        // Data found
        assertDoesNotThrow(() -> Database.inSupplierTransaction(() ->allocataireMapper.findById(1)));

        //Récupérer les data dans la DB
        List<Allocataire> allAllocataires = Database.inSupplierTransaction(() -> allocataireMapper.findAll());
        Allocataire allocNonSupp = Database.inSupplierTransaction(() -> allocataireMapper.findById(1));

        //tester les suppressions effectives
        assertThrows(RuntimeException.class, () -> Database.inSupplierTransaction(() -> allocataireMapper.findById(21)));
        assertTrue(allAllocataires.stream().anyMatch(a -> a.getNoAVS().getValue() == allocNonSupp.getNoAVS().value));

    }

    @Test
    void testUpdateName() {

        //Modifier nom/prénom
        Allocataire allocataire = Database.inSupplierTransaction(() -> allocataireMapper.findById(1));
        allocataire.setNom("Dujardin");
        allocataire.setPrenom("Jean");
        Database.inRunnableTransaction(() -> allocataireMapper.update(allocataire));

        //Tester si modifier en DB
        Allocataire updatedAllocataire = Database.inSupplierTransaction(() -> allocataireMapper.findById(1));
        assertEquals("Jean", updatedAllocataire.getPrenom());
        assertEquals("Dujardin", updatedAllocataire.getNom());

    }

    /*@Test
    void testUpdateAVS(){

        // récupérer allocataire dans la db
        Allocataire allocataire = Database.inSupplierTransaction(() -> allocataireMapper.findById(1));

        //Update AVS
        NoAVS newNoAVS = new NoAVS("123.4567.8910");
        allocataire.setNoAVS(newNoAVS);

        //Doit lever une exception, j'ai mis SQLException mais il faut définir comment on va gérer ça
        assertThrows(SQLException.class, () -> Database.inRunnableTransaction(() -> allocataireMapper.update(allocataire)));

    }*/
    /*

}

 */
