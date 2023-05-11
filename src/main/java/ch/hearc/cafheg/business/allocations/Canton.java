package ch.hearc.cafheg.business.allocations;

import java.util.stream.Stream;

public enum Canton {
  AG, // Argovie (Aargau)
  AI, // Appenzell Rhodes-Intérieures (Appenzell Innerrhoden)
  AR, // Appenzell Rhodes-Extérieures (Appenzell Ausserrhoden)
  BE, // Berne (Bern)
  BL, // Bâle-Campagne (Basel-Landschaft)
  BS, // Bâle-Ville (Basel-Stadt)
  FR, // Fribourg (Freiburg)
  GE, // Genève (Genf)
  GL, // Glaris (Glarus)
  GR, // Grisons (Graubünden)
  JU, // Jura
  LU, // Lucerne
  NE, // Neuchâtel
  NW, // Nidwald (Nidwalden)
  OW, // Obwald (Obwalden)
  SG, // Saint-Gall (St. Gallen)
  SH, // Schaffhouse (Schaffhausen)
  SO, // Soleure (Solothurn)
  SZ, // Schwytz
  TG, // Thurgovie (Thurgau)
  TI, // Tessin (Ticino)
  UR, // Uri
  VD, // Vaud
  VS, // Valais (Wallis)
  ZG, // Zoug (Zug)
  ZH;  // Zurich (Zürich)

  public static Canton fromValue(String value) {
    return Stream.of(Canton.values())
        .filter(c -> c.name().equals(value))
        .findAny()
        .orElse(null);
  }
}
