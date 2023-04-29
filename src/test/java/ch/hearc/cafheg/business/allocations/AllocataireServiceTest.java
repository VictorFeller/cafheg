package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.VersementAllocation;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;

import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class AllocataireServiceTest {

    private Allocataire allocataire;
    private AllocataireMapper allocataireMapper;
    private VersementMapper versementMapper;

    @BeforeEach
    void setUp() {
        allocataire = Mockito.mock(Allocataire.class);
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        //versementMapper = Mockito.mock(VersementMapper.class);
        //versementMapper = new VersementMapper();
    }

    @Test
    void testSuppression() {
        Mockito.when(allocataireMapper.findById(1)).thenReturn(new Allocataire(new NoAVS("756.1558.5343.97"), "Deguzman", "Kendrick", null, null, null, null, null, null));
        allocataire = allocataireMapper.findById(1);
        System.out.println(allocataire.getPrenom());
    }

}
