package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.business.versements.VersementAllocation;
import ch.hearc.cafheg.business.versements.VersementParentEnfant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;

import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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
        versementMapper = new VersementMapper();
        allocataireMapper = new AllocataireMapper(versementMapper);
    }

    @Test
    void GetAllocataire(){
        Allocataire allocataire = new Allocataire(
                new NoAVS("123.4567.8910"), "Dupont", "Pierre", "Lausanne", true,
                true, "ACME Corp", "Ingénieur", 80000
        );
        List<Allocataire> allAllocataires = new ArrayList<>();
        allAllocataires = allocataireMapper.findAll(null);
        assertFalse(allAllocataires.isEmpty());
    }

    @Test
    void testSuppression() {
        //ajout data
        Allocataire allocataire = new Allocataire(
                new NoAVS("123.4567.8910"), "Dupont", "Pierre", "Lausanne", true,
                true, "ACME Corp", "Ingénieur", 80000
        );
        Allocataire allocataire2 = new Allocataire(
                new NoAVS("987.6543.2101"), "Martin", "Sophie", "Genève", false,
                true, "Tech Solutions", "Responsable RH", 75000
        );
        VersementParentEnfant versementParentEnfant = new VersementParentEnfant(AllocataireMapper.findByAVS(allocataire), 2L, new Montant(BigDecimal.valueOf(500)));
        versementMapper.add(allocataire,versementParentEnfant);

        //suppression allocataire2 OK
        assertDoesNotThrow(() -> allocataireMapper.delete(allocataire2));
        //suppression allocataire NotOK
        assertThrows(SQLException.class, () -> allocataireMapper.delete(allocataire));
        //Récupérer les data dans la DB
        List<Allocataire> allAllocataires = new ArrayList<>();
        allAllocataires = allocataireMapper.findAll(null);
        //tester les suppressions effectives
        assertFalse(allAllocataires.contains(allocataire2));
        assertTrue(allAllocataires.contains(allocataire));
        //suppression du versement d'allocataire
        versementMapper.delete(allocataire);
        //suppression OK
        assertDoesNotThrow(() -> allocataireMapper.delete(allocataire));
        assertFalse(allAllocataires.contains(allocataire2));


    }

    @Test
    void testUpdateName() {
        //ajout data
        Allocataire allocataire = new Allocataire(
                new NoAVS("123.4567.8910"), "Dupont", "Pierre", "Lausanne", true,
                true, "ACME Corp", "employee", 80000
        );
        allocataireMapper.add(allocataire);
        //Modifier nom/prénom
        Allocataire addedAllocataire = allocataireMapper.findByAVS(allocataire.getNoAVS());
        addedAllocataire.setNom("Jean");
        addedAllocataire.setPrenom("Dujardin");
        allocataireMapper.updateAllocataire(addedAllocataire);
        //Tester si modifier en DB
        Allocataire updatedAllocataire = AllocataireMapper.getAllocataireByAVS(addedAllocataire.getNoAVS());
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
        allocataireMapper.add(allocataire);
        //Update AVS
        NoAVS newNoAVS = new NoAVS("123.4567.8910");
        allocataire.setAVS(newNoAVS);
        //Doit levé une exception, j'ai mis SQLException mais il faut définir comment on va gérer ça
        assertThrows(SQLException.class, () -> allocataireMapper.updateAllocataire(allocataire));
    }

}
