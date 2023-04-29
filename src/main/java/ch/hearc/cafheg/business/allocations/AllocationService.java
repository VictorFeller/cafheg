//TODO utiliser NE au lien de Neuchâtel
//TODO clean code
package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.DroitAllocationDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.util.List;

public class AllocationService {

    private static final String PARENT_1 = "Parent1";
    private static final String PARENT_2 = "Parent2";
    public static final String WORK_TYPE_INDEPENDANT = "independant";
    public static final String WORK_TYPE_EMPLOYE = "employe";

    private final AllocataireMapper allocataireMapper;
    private final AllocationMapper allocationMapper;

    public AllocationService(
            AllocataireMapper allocataireMapper,
            AllocationMapper allocationMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocationMapper = allocationMapper;
    }

    public List<Allocataire> findAllAllocataires(String likeNom) {
        System.out.println("Rechercher tous les allocataires");
        return allocataireMapper.findAll(likeNom);
    }

    public List<Allocation> findAllocationsActuelles() {
        return allocationMapper.findAll();
    }

    public String getParentDroitAllocation(DroitAllocationDTO droitAllocationDTO) throws Exception {
        Allocataire allocataireParent1 = new Allocataire(
                new NoAVS("756"),
                "Dubois",
                "Jeanne",
                droitAllocationDTO.getParent1Residence().orElse(""),
                droitAllocationDTO.getParent1ActiviteLucrative().orElseThrow(() -> new RuntimeException("Merci de renseigner...")), //TODO create AllocationServiceExcetion
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
                droitAllocationDTO.getParent2ActiviteLucrative().orElseThrow(() -> new Exception("Merci de renseigner...")), //TODO create AllocationServiceExcetion
                droitAllocationDTO.getParent2AutoriteParentale().orElse(false),
                droitAllocationDTO.getParent2WorkPlace().orElse(""),
                droitAllocationDTO.getParent2WorkType().orElse(""),
                droitAllocationDTO.getParent2Salaire().orElse(0)
        );


        System.out.println("Déterminer quel parent a le droit aux allocations");
        String eR = droitAllocationDTO.getEnfantResidence().orElse("");
        boolean pEnsemble = droitAllocationDTO.getParentsEnsemble().orElse(false);



        //Au moins un des deux parents a une activité lucrative
        if(!allocataireParent1.isActiviteLucrative() && !allocataireParent2.isActiviteLucrative())
            throw new RuntimeException("Aucun parent n'exerce d'activité lucrative");
        //Seul le parent 1 a une activité lucrative
        if(allocataireParent1.isActiviteLucrative() && !allocataireParent2.isActiviteLucrative()){
            return PARENT_1;
        }

        //Seul le parent 2 a une activité lucrative
        if(!allocataireParent1.isActiviteLucrative() && allocataireParent2.isActiviteLucrative()) {
            return PARENT_2;
        }

        //Contrôle qu'au moins un des deux parents ait l'autorité parentale
        if(!allocataireParent1.isAutoriteParentale() && !allocataireParent2.isAutoriteParentale())
            throw new RuntimeException("Aucun des deux parents n'a l'autorité parentale");
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
            //Le parent 2 vit avec l'enfant
            if(allocataireParent2.getResidence().equals(eR))
                return PARENT_2;
            throw new RuntimeException("Impossible de déterminer l'allocation, aucun parent n'habite avec l'enfant");
        }

        //Les parents habitent ensemble
        if (pEnsemble) {
            //Le parent 1 travaille dans le canton de l'enfant mais le parent 2 non
            if (allocataireParent1.getWorkplace().equals(eR) && !allocataireParent2.getWorkplace().equals(eR))
                return PARENT_1;
            //Le parent 2 travaille dans le canton de l'enfant mais le parent 1 non
            if (!allocataireParent1.getWorkplace().equals(eR) && allocataireParent2.getWorkplace().equals(eR))
                return PARENT_2;
        }



        //Contrôle le worktype
        //Les deux parents sont indépendants
        if(allocataireParent1.getWorktype().equals(WORK_TYPE_INDEPENDANT) && allocataireParent2.getWorktype().equals(WORK_TYPE_INDEPENDANT)) {
            if(allocataireParent1.getSalaire() == 0 && allocataireParent2.getSalaire() == 0)
                throw new RuntimeException("Impossible de déterminer le droit, salaires manquants");
            return allocataireParent1.getSalaire().doubleValue() > allocataireParent2.getSalaire().doubleValue() ? PARENT_1 : PARENT_2;
        }

        //Les deux parents sont employés ou un est salarié et l'autre indépendant et inversément
        if(checkIfBothParentsEmployeOROneEmployeAndOneIndepOrUnknown(allocataireParent1.getWorktype(), allocataireParent2.getWorktype())) {
            //Si les deux parents sont employés, on compare les salaires
            if (allocataireParent1.getWorktype().equals(WORK_TYPE_EMPLOYE) && allocataireParent2.getWorktype().equals(WORK_TYPE_EMPLOYE)) {
                if(allocataireParent1.getSalaire() == 0 && allocataireParent2.getSalaire() == 0)
                    throw new RuntimeException("Impossible de déterminer le droit, salaires manquants");
                return allocataireParent1.getSalaire().doubleValue() > allocataireParent2.getSalaire().doubleValue() ? PARENT_1 : PARENT_2;
            }
            //Sinon, le seul parent employé recevra les allocations
            else if (allocataireParent1.getWorktype().equals(WORK_TYPE_EMPLOYE)) {
                return PARENT_1;
            } else
                return PARENT_2;
        }

        throw new RuntimeException("Impossible de déterminer le droit");
    }

    private boolean checkIfBothParentsEmployeOROneEmployeAndOneIndepOrUnknown(String p1WorkType, String p2WorkType){
        if(p1WorkType.equals(WORK_TYPE_EMPLOYE) && (p2WorkType.equals(WORK_TYPE_INDEPENDANT) || !p2WorkType.equals(WORK_TYPE_EMPLOYE)))
            return true;
        if((p1WorkType.equals(WORK_TYPE_INDEPENDANT) || !p1WorkType.equals(WORK_TYPE_EMPLOYE)) && p2WorkType.equals(WORK_TYPE_EMPLOYE))
            return true;

        return p1WorkType.equals(WORK_TYPE_EMPLOYE) && p2WorkType.equals(WORK_TYPE_EMPLOYE);
    }
}
