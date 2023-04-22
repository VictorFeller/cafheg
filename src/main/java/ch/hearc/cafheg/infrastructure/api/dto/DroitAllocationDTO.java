package ch.hearc.cafheg.infrastructure.api.dto;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class DroitAllocationDTO {
    private String enfantResidence;
    private String parent1Residence;
    private String parent2Residence;
    private Boolean parent1ActiviteLucrative;
    private Boolean parent2ActiviteLucrative;
    private Boolean parentsEnsemble;
    private Integer parent1Salaire;
    private Integer parent2Salaire;

    public DroitAllocationDTO(String enfantResidence, String parent1Residence, String parent2Residence, Boolean parent1ActiviteLucrative, Boolean parent2ActiviteLucrative, Boolean parentsEnsemble, Integer parent1Salaire, Integer parent2Salaire) {
        this.enfantResidence = enfantResidence;
        this.parent1Residence = parent1Residence;
        this.parent2Residence = parent2Residence;
        this.parent1ActiviteLucrative = parent1ActiviteLucrative;
        this.parent2ActiviteLucrative = parent2ActiviteLucrative;
        this.parentsEnsemble = parentsEnsemble;
        this.parent1Salaire = parent1Salaire;
        this.parent2Salaire = parent2Salaire;
    }

    public String getEnfantResidence() {
        return enfantResidence;
    }

    public String getParent1Residence() {
        return parent1Residence;
    }

    public String getParent2Residence() {
        return parent2Residence;
    }

    public Boolean getParent1ActiviteLucrative() {
        return parent1ActiviteLucrative;
    }

    public Boolean getParent2ActiviteLucrative() {
        return parent2ActiviteLucrative;
    }

    public Boolean getParentsEnsemble() {
        return parentsEnsemble;
    }

    public Number getParent1Salaire() {
        return parent1Salaire;
    }

    public Number getParent2Salaire() {
        return parent2Salaire;
    }

    public Stream<String> toStream() {
        return Stream.of(/*enfantResidence, parent1Residence, parent2Residence, Boolean.toString(parent1ActiviteLucrative),
                Boolean.toString(parent2ActiviteLucrative), Boolean.toString(parentsEnsemble), */Integer.toString(parent1Salaire),
                Integer.toString(parent2Salaire));
    }
}
