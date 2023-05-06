package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOMapper;
import ch.hearc.cafheg.infrastructure.persistance.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AllocataireServiceTest {

    private AllocataireService allocataireService;
    private AllocataireDTOMapper allocataireDTOMapper;
    private AllocataireMapper allocataireMapper;
    private VersementMapper versementMapper;

    @BeforeEach
    void setUp() {
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        allocataireDTOMapper = Mockito.mock(AllocataireDTOMapper.class);
        versementMapper = Mockito.mock(VersementMapper.class);
        allocataireService = new AllocataireService(allocataireMapper, allocataireDTOMapper, versementMapper);
    }

    @Test
    void deleteAllocataire_GivenAllocataireWithVersements_ShouldBeFalse() {
        String resultNOK = "Pas possible de supprimer";
        Mockito.when(versementMapper.countVersementsByAllocataireId(1)).thenReturn(1);
        assertEquals(allocataireService.deleteById(1),resultNOK);
        Mockito.verify(allocataireMapper, times(0)).deleteById(1);
    }

    @Test
    void deleteAllocataire_GivenAllocataireWithoutVersements_ShouldBeTrue() {
        String resultOK = "Allocataire supprimé";
        Mockito.when(versementMapper.countVersementsByAllocataireId(1)).thenReturn(0);
        assertEquals(allocataireService.deleteById(1),resultOK);
        Mockito.verify(allocataireMapper, times(1)).deleteById(1);
    }

    @Test
    void updateAllocataire_GivenNewAVS_ShouldReturnOldData(){
        NoAVS noAVS = new NoAVS("756.1234.5674.42");
        NoAVS updatedNoAVS = new NoAVS("756.1234.5674.42"); // Remplacez ceci par une instance réelle de la classe NoAVS si nécessaire
        String nom = "Dupont";
        String prenom = "Pierre";
        String residence = "Lausanne";
        boolean activiteLucrative = true;
        boolean autoriteParentale = true;
        String workplace = "XYZ Company";
        String worktype = "Ingénieur";
        Integer salaire = 80000;

        AllocataireDTO dtoAllocataire = new AllocataireDTO(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire allocataire = new Allocataire(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        AllocataireDTO dtoUpdateAllocataire = new AllocataireDTO(updatedNoAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire updateAllocataire = new Allocataire(updatedNoAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        Mockito.when(allocataireMapper.update(updateAllocataire)).thenReturn(updateAllocataire);
        Mockito.when(allocataireMapper.update(allocataire)).thenReturn(updateAllocataire);

        assertNotEquals(allocataireService.update(dtoUpdateAllocataire),dtoUpdateAllocataire);
        assertEquals(allocataireService.update(dtoUpdateAllocataire),dtoAllocataire);
        Mockito.verify(allocataireMapper, times(0)).update(allocataire);
    }
    @Test
    void updateAllocataire_GivenNewNom_ShouldReturnUpdatedData(){
        NoAVS noAVS = new NoAVS("756.1234.5678.97"); // Remplacez ceci par une instance réelle de la classe NoAVS si nécessaire
        String nom = "Dupont";
        String updatedNom = "Updated";
        String prenom = "Pierre";
        String residence = "Lausanne";
        boolean activiteLucrative = true;
        boolean autoriteParentale = true;
        String workplace = "XYZ Company";
        String worktype = "Ingénieur";
        Integer salaire = 80000;


        AllocataireDTO dtoUpdateAllocataire = new AllocataireDTO(noAVS, updatedNom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire updateAllocataire = new Allocataire(noAVS, updatedNom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        AllocataireDTO dtoAllocataire = new AllocataireDTO(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire allocataire = new Allocataire(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        Mockito.when(allocataireMapper.update(updateAllocataire)).thenReturn(updateAllocataire);
        assertNotEquals(allocataireService.update(dtoUpdateAllocataire),dtoAllocataire);
        assertEquals(allocataireService.update(dtoUpdateAllocataire),dtoUpdateAllocataire);
        Mockito.verify(allocataireMapper, times(1)).update(updateAllocataire);

    }

    @Test
    void updateAllocataire_GivenNewPrenom_ShouldReturnUpdatedData(){
        NoAVS noAVS = new NoAVS("756.1234.5678.97"); // Remplacez ceci par une instance réelle de la classe NoAVS si nécessaire
        String nom = "Dupont";
        String updatedPrenom = "Updated";
        String prenom = "Pierre";
        String residence = "Lausanne";
        boolean activiteLucrative = true;
        boolean autoriteParentale = true;
        String workplace = "XYZ Company";
        String worktype = "Ingénieur";
        Integer salaire = 80000;


        AllocataireDTO dtoUpdateAllocataire = new AllocataireDTO(noAVS, nom, updatedPrenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire updateAllocataire = new Allocataire(noAVS, nom, updatedPrenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        AllocataireDTO dtoAllocataire = new AllocataireDTO(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire allocataire = new Allocataire(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        Mockito.when(allocataireMapper.update(updateAllocataire)).thenReturn(updateAllocataire);
        assertNotEquals(allocataireService.update(dtoUpdateAllocataire),dtoAllocataire);
        assertEquals(allocataireService.update(dtoUpdateAllocataire),dtoUpdateAllocataire);
        Mockito.verify(allocataireMapper, times(1)).update(updateAllocataire);

    }
}
