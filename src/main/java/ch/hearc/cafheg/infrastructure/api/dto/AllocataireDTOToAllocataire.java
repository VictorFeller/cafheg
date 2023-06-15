package ch.hearc.cafheg.infrastructure.api.dto;

import ch.hearc.cafheg.business.allocations.Allocataire;
import org.springframework.stereotype.Service;

import java.util.function.Function;


// Vous instanciez cette classe vous-même, le @Service ne sert à rien ici
public class AllocataireDTOToAllocataire implements Function<AllocataireDTO, Allocataire> {
    @Override
    public Allocataire apply(AllocataireDTO allocataireDTO){
        return new Allocataire(allocataireDTO.getNoAVS(), allocataireDTO.getNom(), allocataireDTO.getPrenom(), allocataireDTO.getResidence(), allocataireDTO.isActiviteLucrative(), allocataireDTO.isAutoriteParentale(),
                allocataireDTO.getWorkplace(), allocataireDTO.getWorktype(), allocataireDTO.getSalaire());
    }
}
