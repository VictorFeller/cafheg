package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOToAllocataire;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireToAllocataireDTO;
import ch.hearc.cafheg.infrastructure.persistance.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.*;

public class AllocataireServiceTest {

    private AllocataireService allocataireService;
    private AllocataireToAllocataireDTO allocataireToAllocataireDTO;
    private AllocataireDTOToAllocataire allocataireDTOToAllocataire;
    private VersementMapper versementMapper;
    private AllocataireMapper allocataireMapper;

    @BeforeEach
    void setUp() {
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        allocataireToAllocataireDTO = new AllocataireToAllocataireDTO();
        allocataireDTOToAllocataire = new AllocataireDTOToAllocataire();
        versementMapper = Mockito.mock(VersementMapper.class);
        allocataireService = new AllocataireService(allocataireMapper, allocataireToAllocataireDTO, allocataireDTOToAllocataire, versementMapper);
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

        AllocataireDTO dtoUpdateAllocataire = new AllocataireDTO(updatedNoAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);
        Allocataire allocataire = new Allocataire(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        Mockito.when(allocataireMapper.findByAVS(allocataire.getNoAVS())).thenReturn(allocataire);

        // La lambda ici rend le code un peu plus clair
        Mockito.when(allocataireMapper.update(any(Allocataire.class))).thenAnswer(
            (Answer<Allocataire>) invocation -> {
                //Retourne toujours la même instance que celle donnée en param
                return invocation.getArgument(0);
            });

        //Vérifie qu'une exception est bien lancée si on veut modifier le numéro AVS
        assertThrows(RuntimeException.class, () -> allocataireService.update(dtoUpdateAllocataire));
        Mockito.verify(allocataireMapper, times(0)).update(allocataire);
    }
    @Test
    void updateAllocataire_GivenNewNom_ShouldReturnUpdatedData() {
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

        Allocataire allocataire = new Allocataire(noAVS, nom, prenom, residence, activiteLucrative, autoriteParentale, workplace, worktype, salaire);

        Mockito.when(allocataireMapper.findByAVS(allocataire.getNoAVS())).thenReturn(allocataire);
        Mockito.when(allocataireMapper.update(any(Allocataire.class))).thenAnswer(
            (Answer<Allocataire>) invocation -> {
                //Retourne toujours la même instance que celle donnée en param
                return invocation.getArgument(0);
            });

        AllocataireDTO dtoUpdatedAllocataire = allocataireService.update(dtoUpdateAllocataire);
        //Le nom, prénom retourné doit bien correspondre au nouveau qu'on souhaite attribuer
        assertEquals(updateAllocataire.getNom(), dtoUpdatedAllocataire.getNom());
        assertEquals(updateAllocataire.getPrenom(), dtoUpdatedAllocataire.getPrenom());
        //Vérifie que le NSS n'a pas changé
        assertEquals(allocataireService.update(dtoUpdateAllocataire).getNoAVS().getValue(),dtoUpdateAllocataire.getNoAVS().getValue());

    }
}
