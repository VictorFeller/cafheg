package ch.hearc.cafheg.business.allocations;

import lombok.Getter;
import lombok.Value;

// Le @Value rend implicitement tous vos attributs final!
@Value
public class Allocataire {

  // Essayons de rester immutable si ce n'est pas nécessaire
  NoAVS noAVS;
  String nom;
  String prenom;
  String residence;
  boolean activiteLucrative;
  boolean autoriteParentale;
  String workplace;
  String worktype;
  Integer salaire;

  public Allocataire(NoAVS noAVS,
                     String nom,
                     String prenom,
                     String residence,
                     Boolean activiteLucrative,
                     Boolean autoriteParentale,
                     String workplace,
                     String worktype,
                     Integer salaire) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
    this.residence = residence;
    this.activiteLucrative = activiteLucrative != null ? activiteLucrative : false;
    this.autoriteParentale = autoriteParentale != null ? autoriteParentale : false;
    this.workplace = workplace;
    this.worktype = worktype;
    this.salaire = salaire;
  }
}
