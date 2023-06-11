package ch.hearc.cafheg.infrastructure.api;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.AllocataireService;
import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.versements.VersementService;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireDTOToAllocataire;
import ch.hearc.cafheg.infrastructure.api.dto.AllocataireToAllocataireDTO;
import ch.hearc.cafheg.infrastructure.api.dto.DroitAllocationDTO;
import ch.hearc.cafheg.infrastructure.pdf.PDFExporter;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.EnfantMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class RESTController {

  private final EnfantMapper enfantMapper = new EnfantMapper();
  private final PDFExporter pdfExporter = new PDFExporter(enfantMapper);
  private final VersementMapper versementMapper = new VersementMapper();
  private final AllocationMapper allocationMapper = new AllocationMapper();
  private final AllocataireMapper allocataireMapper = new AllocataireMapper(versementMapper);
  private final AllocataireToAllocataireDTO allocataireToAllocataireDTO = new AllocataireToAllocataireDTO();
  private final AllocataireDTOToAllocataire allocataireDTOToAllocataire = new AllocataireDTOToAllocataire();
  private final AllocationService allocationService;
  private final VersementService versementService;
  private final AllocataireService allocataireService;

  public RESTController() {
    this.allocataireService = new AllocataireService(allocataireMapper, allocataireToAllocataireDTO, allocataireDTOToAllocataire, versementMapper);
    this.allocationService = new AllocationService(allocataireMapper, allocationMapper);
    this.versementService = new VersementService(versementMapper, allocataireMapper, pdfExporter);
  }

  /*
  // Headers de la requête HTTP doit contenir "Content-Type: application/json"
  // BODY de la requête HTTP à transmettre afin de tester le endpoint
  {
      "enfantResidence" : "Neuchâtel",
      "parent1Residence" : "Neuchâtel",
      "parent2Residence" : "Bienne",
      "parent1ActiviteLucrative" : true,
      "parent2ActiviteLucrative" : true,
      "parent1Salaire" : 2500,
      "parent2Salaire" : 3000
  }
   */
  @PostMapping("/droits/quel-parent")
  public ResponseEntity<String> getParentDroitAllocation(@RequestBody DroitAllocationDTO droitAllocationDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(inTransaction(() -> allocationService.getParentDroitAllocation(droitAllocationDTO)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/allocataires")
  public List<Allocataire> allocataires(
      @RequestParam(value = "startsWith", required = false) String start) {
    // Vous n'avez pas appelé le bon service.
    return inTransaction(() -> allocationService.findAllAllocataires(start));
  }

  @GetMapping("/allocations")
  public List<Allocation> allocations() {
    return inTransaction(allocationService::findAllocationsActuelles);
  }

  @GetMapping("/allocations/{year}/somme")
  public BigDecimal sommeAs(@PathVariable("year") int year) {
    return inTransaction(() -> versementService.findSommeAllocationParAnnee(year).getValue());
  }

  @GetMapping("/allocations-naissances/{year}/somme")
  public ResponseEntity sommeAns(@PathVariable("year") int year) {
    try{
      return ResponseEntity.status(HttpStatus.OK).body(inTransaction(
              () -> versementService.findSommeAllocationNaissanceParAnnee(year).getValue()));
    }
    catch (Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  //FIXME Exception pas bien remontée
  @GetMapping(value = "/allocataires/{allocataireId}/allocations", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity pdfAllocations(@PathVariable("allocataireId") int allocataireId) {
    try{
      return ResponseEntity.status(HttpStatus.OK).body(inTransaction(() -> versementService.exportPDFAllocataire(allocataireId)));
    }
    catch (Exception e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  //FIXME Exception pas bien remontée
  @GetMapping(value = "/allocataires/{allocataireId}/versements", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfVersements(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFVersements(allocataireId));
  }

  @DeleteMapping("/allocataire")
  public ResponseEntity<String> deleteById(@RequestParam int allocataireId) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(inTransaction(() -> allocataireService.deleteById(allocataireId)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PutMapping("/allocataire")
  public ResponseEntity updateAllocataire(@RequestBody AllocataireDTO allocataireDTO){
    try {
      return ResponseEntity.status(HttpStatus.OK).body(inTransaction(() -> allocataireService.update(allocataireDTO)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
