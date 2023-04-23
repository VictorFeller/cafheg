package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.api.dto.DroitAllocationDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);
    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(
                new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
                new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie"))
        );
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(
            () -> assertThat(all.size()).isEqualTo(2),
            () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
            () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
            () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
            () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
            () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
            () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie")
    );
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(
                new Allocation(new Montant(new BigDecimal(1000)), Canton.NE, LocalDate.now(), null),
                new Allocation(new Montant(new BigDecimal(2000)), Canton.FR, LocalDate.now(), null))
        );
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(
            () -> assertThat(all.size()).isEqualTo(2),
            () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
            () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
            () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
            () -> assertThat(all.get(0).getFin()).isNull(),
            () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
            () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
            () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
            () -> assertThat(all.get(1).getFin()).isNull()
    );
  }

//  @ParameterizedTest
//  @MethodSource("hashMapProviderParent")
//  void getParentDroitAllocation_GivenRESTControllerExample_ShouldBeParent2(Map<String, Object> parameters) {
//    assertAll(
//            () -> assertThat(parameters.get("enfantResidence")).isEqualTo("Neuchâtel"),
//            () -> assertThat(parameters.get("parent1Residence")).isEqualTo("Neuchâtel"),
//            () -> assertThat(parameters.get("parent2Residence")).isEqualTo("Bienne"),
//            () -> assertThat(parameters.get("parent1ActiviteLucrative")).isEqualTo(true),
//            () -> assertThat(parameters.get("parent2ActiviteLucrative")).isEqualTo(true),
//            () -> assertThat(parameters.get("parent1Salaire")).isEqualTo(2500),
//            () -> assertThat(parameters.get("parent2Salaire")).isEqualTo(3000),
//            () -> assertThat(allocationService.getParentDroitAllocation(parameters)).isEqualTo("Parent2")
//    );
//  }

  @ParameterizedTest
  @MethodSource("hashMapProviderParent")
  void getParentDroitAllocation_GivenRESTControllerExample_ShouldBeParent2(DroitAllocationDTO droitAllocationDTO, String expected) {
//    assertEquals(allocationService.getParentDroitAllocation(droitAllocationDTO), "Parent2");
    assertEquals(allocationService.getParentDroitAllocation(droitAllocationDTO), expected);
  }

//  static Stream<Map<String, Object>> hashMapProviderParent() {
//    return Stream.of(
//            //Scenario RESTController
//            Map.of("enfantResidence", "Neuchâtel",
//                    "parent1Residence","Neuchâtel",
//                    "parent2Residence", "Bienne",
//                    "parent1ActiviteLucrative",true,
//                    "parent2ActiviteLucrative", true,
//                    "parent1Salaire", 2500,
//                    "parent2Salaire", 3000)
//    );
//  }
  static Stream<Arguments> hashMapProviderParent() {
    return Stream.of(
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
            "Bienne", true, true, false, false, "Neuchâtel", "Neuchâtel", "independant", "independant",  true, 2500, 3000), "Parent2"),
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
            "Bienne", true, true, false, false, "Neuchâtel", "Neuchâtel", "independant", "independant", true, 3500, 3000), "Parent1"),
            //Scenario A
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
                    "Neuchâtel", true, false, false, false, null, null, null, null, null, 2500, 3500), "Parent1"),
            //Scenario B
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
                    "Neuchâtel", true, true, true, false, null, null, null, null,null, 2500, 3500), "Parent1"),
            //Scenario C
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
                    "Bienne", true, true, true, true, null, null, null, null, false, 2500, 3500), "Parent1"),
            //Scenario D
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
                    "Bienne", true, true, true, true, "Neuchâtel", "Bienne", null, null,true, 2500, 3500), "Parent1"),
            //Scenario E
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
                    "Bienne", true, true, true, true, "Neuchâtel", "Bienne", "employe", "employe",true, 2500, 3500), "Parent1"),
            //Scenario F
            Arguments.of(new DroitAllocationDTO("Neuchâtel", "Neuchâtel",
                    "Bienne", true, true, true, true, "Neuchâtel", "Neuchâtel", "independant", "independant", true, 2500, 3500), "Parent2"));

  }

}