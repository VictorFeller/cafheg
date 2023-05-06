package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;

public class AllocataireService {

    private final AllocataireMapper allocataireMapper;
    private final AllocataireDTOMapper allocataireDTOMapper;
    private final VersementMapper versementMapper;

    public AllocataireService(AllocataireMapper allocataireMapper, AllocataireDTOMapper allocataireDTOMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocataireDTOMapper = allocataireDTOMapper;
        this.versementMapper = versementMapper;
    }

    public String deleteById(int allocataireId) {
        if (versementMapper.countVersementsByAllocataireId(allocataireId) == 0) {
            allocataireMapper.deleteById(allocataireId);
            return "Allocataire supprimé";
        }
        return "Pas possible de supprimer";
    }

    public AllocataireDTO update(AllocataireDTO allocataireDTO) {
        Allocataire allocataire = new Allocataire(allocataireDTO.getNoAVS(),
                allocataireDTO.getNom(),
                allocataireDTO.getPrenom(),
                allocataireDTO.getResidence(),
                null,
                null,
                allocataireDTO.getWorkplace(),
                allocataireDTO.getWorktype(),
                allocataireDTO.getSalaire());
        //Utilise le DTOMapper pour convertir en dto
        return allocataireDTOMapper.apply(allocataireMapper.update(allocataire));
    }
}
