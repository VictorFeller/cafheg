package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;

public class AllocataireService {

    private final AllocataireMapper allocataireMapper;

    public AllocataireService(AllocataireMapper allocataireMapper) {
        this.allocataireMapper = allocataireMapper;
    }

    public String deleteById(int allocataireId) {
        return allocataireMapper.deleteById(allocataireId);
    }

}
