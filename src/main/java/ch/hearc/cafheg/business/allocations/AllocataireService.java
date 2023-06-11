package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOToAllocataire;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireToAllocataireDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocataireService {

    private final AllocataireMapper allocataireMapper;
    private final AllocataireToAllocataireDTO allocataireToAllocataireDTO;
    private final AllocataireDTOToAllocataire allocataireDTOToAllocataire;
    private final VersementMapper versementMapper;
    private static final Logger logger = LoggerFactory.getLogger(AllocataireService.class);

    public AllocataireService(AllocataireMapper allocataireMapper, AllocataireToAllocataireDTO allocataireToAllocataireDTO, AllocataireDTOToAllocataire allocataireDTOToAllocataire, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocataireToAllocataireDTO = allocataireToAllocataireDTO;
        this.allocataireDTOToAllocataire = allocataireDTOToAllocataire;
        this.versementMapper = versementMapper;
    }

    public String deleteById(int allocataireId) {
        logger.info("Suppression de l'allocataire id : " + allocataireId);
        if (versementMapper.countVersementsByAllocataireId(allocataireId) == 0) {
            allocataireMapper.deleteById(allocataireId);
            return "Allocataire supprimé";
        }
        return "Pas possible de supprimer";
    }

    public AllocataireDTO update(AllocataireDTO allocataireDTO) {
        logger.info("Mise à jour de l'allocataire : " + allocataireDTO.getNoAVS());
        // Pas de tests, pas de code de production

        Allocataire al = allocataireMapper.findByAVS(allocataireDTO.getNoAVS());

        if(al.getNom().equals(allocataireDTO.getNom()) && al.getPrenom().equals(allocataireDTO.getPrenom())){
            //Même nom, prénom pas d'update
            throw new RuntimeException("Aucun changement de nom ni prénom");
        }

        al = allocataireMapper.update(allocataireDTOToAllocataire.apply(allocataireDTO));
        return allocataireToAllocataireDTO.apply(al);
    }
}
