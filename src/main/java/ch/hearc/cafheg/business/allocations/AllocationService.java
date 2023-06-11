package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.DroitAllocationDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllocationService {

    private static final String PARENT_1 = "Parent1";
    private static final String PARENT_2 = "Parent2";
    public static final String WORK_TYPE_INDEPENDANT = "independant";
    public static final String WORK_TYPE_EMPLOYE = "employe";

    private final AllocataireMapper allocataireMapper;
    private final AllocationMapper allocationMapper;
    private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);

    public AllocationService(
            AllocataireMapper allocataireMapper,
            AllocationMapper allocationMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocationMapper = allocationMapper;
    }

    public List<Allocataire> findAllAllocataires(String likeNom) {
        logger.info("Rechercher tous les allocataires");
        return allocataireMapper.findAllWhereNomLike(likeNom);
    }

    public List<Allocation> findAllocationsActuelles() {
        logger.info("Rechercher toutes les allocations");
        return allocationMapper.findAll();
    }

    public String getParentDroitAllocation(DroitAllocationDTO droitAllocationDTO) {
        // J'ai retiré dans cette classe tout le code qui n'était pas couvert par des tests.
        logger.info("Déterminer quel parent a le droit aux allocations");

        // Ca semble un peu bizarre de devoir remplir des objets de manière partielles, non?
        // On tombe ici typiquement dans le code smell du "god object". Allocataire va à terme disposer
        // de XX'XXX attributs pour pouvoir couvrir tous les cas d'usage.
        // Je comprends pourquoi vous avez tenter de faire ça de cette manière.. Mon DTO est censée
        // être un objet de transfert alors que je veux le convertir en objet métier, juste?
        // Pourquoi pas, si vraiment vous pensez qu'il y a un besoin de découplage.
        // Mais l'objet métier ne devrait pas être Allocataire à mon sens.. Mais quelque chose du style "AllocatairePourDroitAllocation".
        // De cette manière, vous avez votre DTO qui est votre contrat d'API et votre objet métier
        // qui correspond à ce que vous avez vraiment besoin de manipuler pour traiter une demande d'allocation.
        Allocataire allocataireParent1 = new Allocataire(
                new NoAVS("756"),
                "Dubois",
                "Jeanne",
                droitAllocationDTO.getParent1Residence().orElse(""),
                droitAllocationDTO.getParent1ActiviteLucrative().orElseThrow(() -> new RuntimeException("Merci de renseigner...")),
                droitAllocationDTO.getParent1AutoriteParentale().orElse(false),
                droitAllocationDTO.getParent1WorkPlace().orElse(""),
                droitAllocationDTO.getParent1WorkType().orElse(""),
                droitAllocationDTO.getParent1Salaire().orElse(0)
        );

        Allocataire allocataireParent2 = new Allocataire(
                new NoAVS("756"),
                "Dubois",
                "Marc",
                droitAllocationDTO.getParent2Residence().orElse(""),
                droitAllocationDTO.getParent2ActiviteLucrative().orElseThrow(() -> new RuntimeException("Merci de renseigner...")),
                droitAllocationDTO.getParent2AutoriteParentale().orElse(false),
                droitAllocationDTO.getParent2WorkPlace().orElse(""),
                droitAllocationDTO.getParent2WorkType().orElse(""),
                droitAllocationDTO.getParent2Salaire().orElse(0)
        );


        String eR = droitAllocationDTO.getEnfantResidence().orElse("");
        boolean pEnsemble = droitAllocationDTO.getParentsEnsemble().orElse(false);

        //Seul le parent 1 a une activité lucrative
        if(allocataireParent1.isActiviteLucrative() && !allocataireParent2.isActiviteLucrative()){
            return PARENT_1;
        }

        //Seul le parent 1 a l'autorité parentale
        if (allocataireParent1.isAutoriteParentale() && !allocataireParent2.isAutoriteParentale()) {
            return PARENT_1;
        }

        //Seul le parent 2 a l'autorité parentale
        if (!allocataireParent1.isAutoriteParentale() && allocataireParent2.isAutoriteParentale()) {
            return PARENT_2;
        }

        //Les parents ne vivent pas ensemble
        if(!pEnsemble){
            //Le parent 1 vit avec l'enfant
            if(allocataireParent1.getResidence().equals(eR))
                return PARENT_1;
                return PARENT_2;
        }

        //Les parents habitent ensemble
        if (pEnsemble) {
            //Le parent 1 travaille dans le canton de l'enfant mais le parent 2 non
            if (allocataireParent1.getWorkplace().equals(eR) && !allocataireParent2.getWorkplace().equals(eR))
                return PARENT_1;
        }


        //Contrôle le worktype
        //Les deux parents sont indépendants
        if(allocataireParent1.getWorktype().equals(WORK_TYPE_INDEPENDANT) && allocataireParent2.getWorktype().equals(WORK_TYPE_INDEPENDANT)) {
            return allocataireParent1.getSalaire().doubleValue() > allocataireParent2.getSalaire().doubleValue() ? PARENT_1 : PARENT_2;
        }

        //Si les deux parents sont employés, on compare les salaires
        if (allocataireParent1.getWorktype().equals(WORK_TYPE_EMPLOYE) && allocataireParent2.getWorktype().equals(WORK_TYPE_EMPLOYE)) {
            return allocataireParent1.getSalaire().doubleValue() > allocataireParent2.getSalaire().doubleValue() ? PARENT_1 : PARENT_2;
        }
        return PARENT_1;
    }
}
