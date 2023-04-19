package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
            LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(0).getFin()).isNull(),
        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(1).getFin()).isNull());
  }

  @ParameterizedTest
  @MethodSource("hashMapProviderParent1")
  void getParentDroitAllocation_GivenXXX_SouldBeParent1(Map<String, Object> parameters){
    assertThat(allocationService.getParentDroitAllocation(parameters)).isEqualTo("Parent1");
  }

  @ParameterizedTest
  @MethodSource("hashMapProviderParent2")
  void getParentDroitAllocation_GivenXXX_SouldBeParent2(Map<String, Object> parameters){
    assertThat(allocationService.getParentDroitAllocation(parameters)).isEqualTo("Parent2");
  }

  static Stream<Map<String, Object>> hashMapProviderParent1() {
    return Stream.of(
            //Scenario a
            Map.of("parent1ActiviteLucrative",true,
                    "parent1Residence","Neuchâtel",
                    "enfantResidence", "Neuchâtel"
                    ),
            //Scenario b
            //Comment gérer l'autorité parentale ?
            //On ne gère pas le salaire alors ça sera tjs le Parent2 ce qui n'est pas forcément juste
            Map.of("parent1ActiviteLucrative",true,
                    "parent2ActiviteLucrative", true,
                    "enfantResidence", "Neuchâtel",
                    "parent1Residence","Neuchâtel"

                    ),
            //Scenario C
            //Comment gérer l'autorité parentale ?
            //On ne gère pas le salaire alors ça sera tjs le Parent2 ce qui n'est pas forcément juste
            Map.of("parent1ActiviteLucrative",true,
                    "parent2ActiviteLucrative", true,
                    "enfantResidence", "Neuchâtel",
                    "parent1Residence","Neuchâtel",
                    "parent2Residence","Bienne"/*,
                    "parent1Salaire", 3500,
                    "parent2Salaire", 3000*/
            )
    );
  }

  static Stream<Map<String, Object>> hashMapProviderParent2() {
    return Stream.of(
            //Scenario d'exemple
            Map.of("enfantResidence", "Neuchâtel",
                    "parent1Residence","Neuchâtel",
                    "parent2Residence", "Bienne",
                    "parent1ActiviteLucrative",true,
                    "parent2ActiviteLucrative", true,
                    "parent1Salaire", 2500,
                    "parent2Salaire", 3000)
    );
  }

}