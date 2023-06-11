package ch.hearc.cafheg.infrastructure.api.dto;

import java.util.Optional;
import lombok.Getter;
import lombok.Value;

@Value
public class DroitAllocationDTO {
    Optional<String> enfantResidence;
    Optional<String> parent1Residence;
    Optional<String> parent2Residence;
    Optional<Boolean> parent1ActiviteLucrative;
    Optional<Boolean> parent2ActiviteLucrative;
    Optional<Boolean> parent1AutoriteParentale;
    Optional<Boolean> parent2AutoriteParentale;
    Optional<String> parent1WorkPlace;
    Optional<String> parent2WorkPlace;
    Optional<String> parent1WorkType;
    Optional<String> parent2WorkType;
    Optional<Boolean> parentsEnsemble;
    Optional<Integer> parent1Salaire;
    Optional<Integer> parent2Salaire;

    public DroitAllocationDTO(String enfantResidence, String parent1Residence, String parent2Residence, Boolean parent1ActiviteLucrative, Boolean parent2ActiviteLucrative, Boolean parent1AutoriteParentale, Boolean parent2AutoriteParentale, String parent1WorkPlace, String parent2WorkPlace, String parent1WorkType, String parent2WorkType, Boolean parentsEnsemble, Integer parent1Salaire, Integer parent2Salaire) {
        this.enfantResidence = Optional.ofNullable(enfantResidence);
        this.parent1Residence = Optional.ofNullable(parent1Residence);
        this.parent2Residence = Optional.ofNullable(parent2Residence);
        this.parent1ActiviteLucrative = Optional.ofNullable(parent1ActiviteLucrative);
        this.parent2ActiviteLucrative = Optional.ofNullable(parent2ActiviteLucrative);
        this.parent1AutoriteParentale = Optional.ofNullable(parent1AutoriteParentale);
        this.parent2AutoriteParentale = Optional.ofNullable(parent2AutoriteParentale);
        this.parent1WorkPlace = Optional.ofNullable(parent1WorkPlace);
        this.parent2WorkPlace = Optional.ofNullable(parent2WorkPlace);
        this.parent1WorkType = Optional.ofNullable(parent1WorkType);
        this.parent2WorkType = Optional.ofNullable(parent2WorkType);
        this.parentsEnsemble = Optional.ofNullable(parentsEnsemble);
        this.parent1Salaire = Optional.ofNullable(parent1Salaire);
        this.parent2Salaire = Optional.ofNullable(parent2Salaire);
    }
}