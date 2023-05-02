package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.business.versements.VersementAllocation;
import ch.hearc.cafheg.business.versements.VersementParentEnfant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;

import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
        List<Allocataire> allAllocataires = Database.inTransaction(() -> allocataireMapper.findAll(null));
        assertFalse(allAllocataires.isEmpty());
    }


    @Test
    void testSuppression() {
        //Récup donnée test
        Allocataire allocAvecVers = Database.inTransaction(() -> allocataireMapper.findById(1));
        Allocataire allocSansVers = Database.inTransaction(() -> allocataireMapper.findById(21));
        //suppression allocataire2 OK
        assertDoesNotThrow(() -> Database.inTransaction(() -> allocataireMapper.delete(allocSansVers)));
        // No data found
        assertThrows(SQLException.class, () -> Database.inTransaction(() ->allocataireMapper.findById(21)));
        //suppression allocataire NotOK
        assertThrows(SQLException.class, () -> Database.inTransaction(() ->allocataireMapper.delete(allocAvecVers)));
        // Data found
        assertDoesNotThrow(() -> Database.inTransaction(() ->allocataireMapper.findById(21)));

        //Récupérer les data dans la DB
        List<Allocataire> allAllocataires = new ArrayList<>();
        allAllocataires = Database.inTransaction(() ->allocataireMapper.findAll(null));
        //tester les suppressions effectives
        assertFalse(allAllocataires.contains(allocSansVers));
        assertTrue(allAllocataires.contains(allocAvecVers));

    }

    @Test
    void testUpdateName() {
        //ajout data
        Allocataire allocataire = new Allocataire(
                new NoAVS("123.4567.8910"), "Dupont", "Pierre", "Lausanne", true,
                true, "ACME Corp", "employee", 80000
        );
        Database.inTransaction(() -> allocataireMapper.add(allocataire));
        //Modifier nom/prénom
        Allocataire addedAllocataire = Database.inTransaction(() -> allocataireMapper.findByNoAVS(allocataire.getNoAVS().getValue()));
        addedAllocataire.setNom("Jean");
        addedAllocataire.setPrenom("Dujardin");
        Database.inTransaction(() -> allocataireMapper.update(addedAllocataire));
        //Tester si modifier en DB
        Allocataire updatedAllocataire = Database.inTransaction(() -> allocataireMapper.findByNoAVS(addedAllocataire.getNoAVS().getValue()));
        assertEquals("Jean", updatedAllocataire.getPrenom());
        assertEquals("Dujardin", updatedAllocataire.getNom());

    }

    @Test
    void testUpdateAVS(){
        // ajout data
        Allocataire allocataire = new Allocataire(
                new NoAVS("987.6543.2101"), "Martin", "Sophie", "Genève", false,
                true, "Tech Solutions", "independant", 75000
        );
        Database.inTransaction(() -> allocataireMapper.add(allocataire));
        //Update AVS
        NoAVS newNoAVS = new NoAVS("123.4567.8910");
        allocataire.setNoAVS(newNoAVS);
        //Doit levé une exception, j'ai mis SQLException mais il faut définir comment on va gérer ça
        assertThrows(SQLException.class, () -> Database.inTransaction(() -> allocataireMapper.update(allocataire)));
    }

}
