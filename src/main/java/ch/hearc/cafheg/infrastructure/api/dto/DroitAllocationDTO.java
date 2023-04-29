package ch.hearc.cafheg.infrastructure.api.dto;

import java.util.Optional;
import lombok.Getter;

@Getter
public class DroitAllocationDTO {
    private Optional<String> enfantResidence;
    private Optional<String> parent1Residence;
    private Optional<String> parent2Residence;
    private Optional<Boolean> parent1ActiviteLucrative;
    private Optional<Boolean> parent2ActiviteLucrative;
    private Optional<Boolean> parent1AutoriteParentale;
    private Optional<Boolean> parent2AutoriteParentale;
    private Optional<String> parent1WorkPlace;
    private Optional<String> parent2WorkPlace;
    private Optional<String> parent1WorkType;
    private Optional<String> parent2WorkType;
    private Optional<Boolean> parentsEnsemble;
    private Optional<Integer> parent1Salaire;
    private Optional<Integer> parent2Salaire;

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
