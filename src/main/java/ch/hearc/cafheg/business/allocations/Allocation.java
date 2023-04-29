package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.common.Montant;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class Allocation {

  private final Montant montant;
  private final Canton canton;
  private final LocalDate debut;
  private final LocalDate fin;

  public Allocation(Montant montant, Canton canton, LocalDate debut, LocalDate fin) {
    this.montant = montant;
    this.canton = canton;
    this.debut = debut;
    this.fin = fin;
  }
}
