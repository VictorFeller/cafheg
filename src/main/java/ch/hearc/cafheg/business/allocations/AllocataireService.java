package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;

public class AllocataireService {

    private final AllocataireMapper allocataireMapper;
    private final AllocataireDTOMapper allocataireDTOMapper;

    public AllocataireService(AllocataireMapper allocataireMapper, AllocataireDTOMapper allocataireDTOMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocataireDTOMapper = allocataireDTOMapper;
    }

    public String deleteById(int allocataireId) {
        return allocataireMapper.deleteById(allocataireId);
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
