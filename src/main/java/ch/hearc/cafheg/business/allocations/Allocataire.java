package ch.hearc.cafheg.business.allocations;

import lombok.Getter;

@Getter
public class Allocataire {

  private NoAVS noAVS;
  private String nom;
  private String prenom;
  private final String residence;
  private final boolean activiteLucrative;
  private final boolean autoriteParentale;
  private final String workplace;
  private final String worktype;
  private final Integer salaire;

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

  public void setNoAVS(NoAVS noAVS) {
    this.noAVS = noAVS;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

}
