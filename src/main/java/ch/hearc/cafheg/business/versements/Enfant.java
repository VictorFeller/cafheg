package ch.hearc.cafheg.business.versements;

import ch.hearc.cafheg.business.allocations.NoAVS;
import lombok.Getter;
import lombok.Value;

@Value
public class Enfant {

  NoAVS noAVS;
  String nom;
  String prenom;

  public Enfant(NoAVS noAVS, String nom, String prenom) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
  }
}
