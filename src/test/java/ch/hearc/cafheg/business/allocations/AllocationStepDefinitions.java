
package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.api.dto.DroitAllocationDTO;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;




public class AllocationStepDefinitions {

    private final AllocataireMapper allocataireMapper = new AllocataireMapper();
    private final AllocationMapper allocationMapper = new AllocationMapper();
    private AllocationService allocationService = new AllocationService(allocataireMapper,allocationMapper);
    protected DroitAllocationDTO dto;
    protected Map<String,Object> parameters = new HashMap<>();

    public AllocationStepDefinitions() {
    }

    @Given("Parent1 works {string} at {string} as {string} for {int} lives in {string} and can parent the child {string}")
    public void parent1(String parent1ActivLucr, String parent1WorkPlace, String parent1WorkType, int parent1Salary,String parent1Resid, String parent1Parenting) {
        parameters.put("Parent1ActiviteLucrative", Boolean.valueOf(parent1ActivLucr));
        parameters.put("Parent1WorkPlace", parent1WorkPlace);
        parameters.put("Parent1WorkType", parent1WorkType);
        parameters.put("Parent1Salary", Integer.valueOf(parent1Salary));
        parameters.put("Parent1Residence", parent1Resid);
        parameters.put("Parent1Parenting", Boolean.valueOf(parent1Parenting));
    }

    @Given("Parent2 works {string} at {string} as {string} for {int} lives in {string} and can parent the child {string}")
    public void parent2(String parent2ActivLucr, String parent2WorkPlace, String parent2WorkType, int parent2Salary,String parent2Resid, String parent2Parenting) {
        parameters.put("Parent2ActiviteLucrative", Boolean.valueOf(parent2ActivLucr));
        parameters.put("Parent2WorkPlace", parent2WorkPlace);
        parameters.put("Parent2WorkType", parent2WorkType);
        parameters.put("Parent2Salary", Integer.valueOf(parent2Salary));
        parameters.put("Parent2Residence", parent2Resid);
        parameters.put("Parent2Parenting", Boolean.valueOf(parent2Parenting));
    }

    @Given("parents live together ? {string}")
    public void liveTogether(String liveTogether) {
        parameters.put("LiveTogether", Boolean.valueOf(liveTogether));
    }

    @Given("child live in {string}")
    public void child(String childResidence) {
        parameters.put("ChildResidence", childResidence);
    }

    @When("^parents ask for CAF rights with information above$")
    public void askForRights() {
        dto = new DroitAllocationDTO(
                (String) parameters.get("ChildResidence"),
                (String) parameters.get("Parent1Residence"),
                (String) parameters.get("Parent2Residence"),
                (Boolean) parameters.get("Parent1ActiviteLucrative"),
                (Boolean) parameters.get("Parent2ActiviteLucrative"),
                (Boolean) parameters.get("Parent1Parenting"),
                (Boolean) parameters.get("Parent2Parenting"),
                (String)parameters.get("Parent1WorkPlace"),
                (String)parameters.get("Parent2WorkPlace"),
                (String)parameters.get("Parent1WorkType"),
                (String)parameters.get("Parent2WorkType"),
                (Boolean) parameters.get("LiveTogether"),
                (Integer) parameters.get("Parent1Salary"),
                (Integer) parameters.get("Parent2Salary")
        );
    }

    @Then("parent who will receive the CAF is {string}")
    public void parentWithRights(String parentWithRights) throws Exception {
        assertEquals(parentWithRights, allocationService.getParentDroitAllocation(dto));
    }
}
