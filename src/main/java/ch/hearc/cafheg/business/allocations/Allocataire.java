package ch.hearc.cafheg.business.allocations;

import java.util.Optional;

public class Allocataire {

  private final NoAVS noAVS;
  private final String nom;
  private final String prenom;
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
    this.activiteLucrative = activiteLucrative;
    this.autoriteParentale = autoriteParentale;
    this.workplace = workplace;
    this.worktype = worktype;
    this.salaire = salaire;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public NoAVS getNoAVS() {
    return noAVS;
  }

  public String getResidence() {
    return residence;
  }

  public boolean getActiviteLucrative() {
    return activiteLucrative;
  }

  public boolean getAutoriteParentale() {
    return autoriteParentale;
  }

  public String getWorkplace() {
    return workplace;
  }

  public String getWorktype() {
    return worktype;
  }

  public Integer getSalaire() {
    return salaire;
  }
}
