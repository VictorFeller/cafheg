package ch.hearc.cafheg.business.versements;

import ch.hearc.cafheg.business.common.Montant;
import lombok.Value;

@Value
public class VersementParentEnfant {

  long parentId;
  long enfantId;
  Montant montant;

  public VersementParentEnfant(long parentId, long enfantId,
      Montant montant) {
    this.parentId = parentId;
    this.enfantId = enfantId;
    this.montant = montant;
  }

  public long getParentId() {
    return parentId;
  }

  public long getEnfantId() {
    return enfantId;
  }

  public Montant getMontant() {
    return montant;
  }
}
