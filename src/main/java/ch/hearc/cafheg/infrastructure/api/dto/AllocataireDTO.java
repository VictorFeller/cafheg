package ch.hearc.cafheg.infrastructure.api.dto;

import ch.hearc.cafheg.business.allocations.NoAVS;
import lombok.Getter;
import lombok.Value;

@Value
public class AllocataireDTO {
    NoAVS noAVS;
    String nom;
    String prenom;
    String residence;
    boolean activiteLucrative;
    boolean autoriteParentale;
    String workplace;
    String worktype;
    Integer salaire;

    public AllocataireDTO(NoAVS noAVS, String nom, String prenom, String residence, boolean activiteLucrative, boolean autoriteParentale, String workplace, String worktype, Integer salaire) {
        this.noAVS = noAVS;
        this.nom = nom;
        this.prenom = prenom;
        this.residence = residence;
        this.activiteLucrative = activiteLucrative;
        this.autoriteParentale = autoriteParentale;
        this.workplace = workplace;
        this.worktype = worktype;
        this.salaire = salaire;
    }
}
