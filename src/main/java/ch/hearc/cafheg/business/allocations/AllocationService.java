package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.DroitAllocationDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.util.List;

public class AllocationService {

    private static final String PARENT_1 = "Parent1";
    private static final String PARENT_2 = "Parent2";

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

    public String getParentDroitAllocation(DroitAllocationDTO droitAllocationDTO) {
        //TODO Utiliser classe métier ?
        //TODO extraire les conditions if dans des méthodes

        System.out.println("Déterminer quel parent a le droit aux allocations");
        String eR = droitAllocationDTO.getEnfantResidence();
        Boolean p1AL = droitAllocationDTO.getParent1ActiviteLucrative();
        String p1Residence = droitAllocationDTO.getParent1Residence();
        Boolean p2AL = droitAllocationDTO.getParent2ActiviteLucrative();
        String p2Residence = droitAllocationDTO.getParent2Residence();
        Boolean pEnsemble = droitAllocationDTO.getParentsEnsemble();
        Number salaireP1 = droitAllocationDTO.getParent1Salaire();
        Number salaireP2 = droitAllocationDTO.getParent2Salaire();
        Boolean p1AP = droitAllocationDTO.getParent1AutoriteParentale();
        Boolean p2AP = droitAllocationDTO.getParent2AutoriteParentale();
        String p1WorkPlace = droitAllocationDTO.getParent1WorkPlace();
        String p2WorkPlace = droitAllocationDTO.getParent2WorkPlace();
        String p1WorkType = droitAllocationDTO.getParent1WorkType();
        String p2WorkType = droitAllocationDTO.getParent2WorkType();

        if (p1AL && !p2AL) {
            return PARENT_1;
        }

        if (p2AL && !p1AL) {
            return PARENT_2;
        }

        if (p1AP && p2AP != null && !p2AP ) {
            return PARENT_1;
        }
        if (!p1AP && p2AP != null && p2AP ) {
            return PARENT_2;
        }

        //Contrôle que le parent 1 habite avec l'enfant
        if (!pEnsemble) {
            return eR.equals(p1Residence) ? PARENT_1 : null;
        }

        //Contrôle qu'un parent travaille dans le canton de l'enfant
        if (p1WorkPlace.equals(eR) && !p2WorkPlace.equals(eR))
            return PARENT_1;
        if (!p1WorkPlace.equals(eR) && p2WorkPlace.equals(eR))
            return PARENT_2;

        //Contrôle le worktype
        if(p1WorkType.equals("independant") && p2WorkType.equals("independant"))
            return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;

        if(checkIfParentsBothEmployeOROneEmployeAndOneIndep(p1WorkType, p2WorkType))
            if(p1WorkType.equals("employe") && p2WorkType.equals("employe")) {
                return salaireP1.doubleValue() > salaireP2.doubleValue() ? PARENT_1 : PARENT_2;
            }
            else if(p1WorkType.equals("employe")) {
                return PARENT_1;
            }
            else
                return PARENT_2;
        return null;
    }

    private boolean checkIfParentsBothEmployeOROneEmployeAndOneIndep(String p1WorkType, String p2WorkType){
        if(p1WorkType.equals("employe") && p2WorkType.equals("independant"))
            return true;
        if(p1WorkType.equals("independant") && p2WorkType.equals("employe"))
            return true;

        return p1WorkType.equals("employe") && p2WorkType.equals("employe");
    }
}
