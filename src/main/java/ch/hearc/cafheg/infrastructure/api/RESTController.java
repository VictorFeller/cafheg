package ch.hearc.cafheg.infrastructure.api;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inSupplierTransaction;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger logger = LoggerFactory.getLogger(RESTController.class);

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
  public String getParentDroitAllocation(@RequestBody DroitAllocationDTO droitAllocationDTO) {
    return inSupplierTransaction(() -> {
      try {
        return allocationService.getParentDroitAllocation(droitAllocationDTO);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  @GetMapping("/allocataires")
  public List<Allocataire> allocataires(
      @RequestParam(value = "startsWith", required = false) String start) {
    return inSupplierTransaction(() -> allocationService.findAllAllocataires());
  }

  @GetMapping("/allocations")
  public List<Allocation> allocations() {
    return inSupplierTransaction(allocationService::findAllocationsActuelles);
  }

  @GetMapping("/allocations/{year}/somme")
  public BigDecimal sommeAs(@PathVariable("year") int year) {
    return inSupplierTransaction(() -> versementService.findSommeAllocationParAnnee(year).getValue());
  }

  @GetMapping("/allocations-naissances/{year}/somme")
  public BigDecimal sommeAns(@PathVariable("year") int year) {
    return inSupplierTransaction(
        () -> versementService.findSommeAllocationNaissanceParAnnee(year).getValue());
  }

  @GetMapping(value = "/allocataires/{allocataireId}/allocations", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfAllocations(@PathVariable("allocataireId") int allocataireId) {
    return inSupplierTransaction(() -> versementService.exportPDFAllocataire(allocataireId));
  }

  @GetMapping(value = "/allocataires/{allocataireId}/versements", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfVersements(@PathVariable("allocataireId") int allocataireId) {
    return inSupplierTransaction(() -> versementService.exportPDFVersements(allocataireId));
  }

  @DeleteMapping("/allocataire")
  public ResponseEntity<String> deleteById(@RequestParam int allocataireId) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(inSupplierTransaction(() -> allocataireService.deleteById(allocataireId)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PutMapping("/allocataire")
  public ResponseEntity updateAllocataire(@RequestBody AllocataireDTO allocataireDTO){
    try {
      return ResponseEntity.status(HttpStatus.OK).body(inSupplierTransaction(() -> allocataireService.update(allocataireDTO)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
