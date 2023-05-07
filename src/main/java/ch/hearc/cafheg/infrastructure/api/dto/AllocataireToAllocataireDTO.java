package ch.hearc.cafheg.infrastructure.api.dto;

import ch.hearc.cafheg.business.allocations.Allocataire;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
public class AllocataireToAllocataireDTO implements Function<Allocataire, AllocataireDTO> {
@Override
    public AllocataireDTO apply(Allocataire allocataire){
        return new AllocataireDTO(allocataire.getNoAVS(), allocataire.getNom(), allocataire.getPrenom(), allocataire.getResidence(), allocataire.isActiviteLucrative(), allocataire.isAutoriteParentale(),
                allocataire.getWorkplace(), allocataire.getWorktype(), allocataire.getSalaire());
    }
}
