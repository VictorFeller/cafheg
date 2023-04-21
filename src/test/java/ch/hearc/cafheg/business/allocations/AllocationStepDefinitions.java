package ch.hearc.cafheg.business.allocations;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;




public class AllocationStepDefinitions {
    private AllocationService allocationService;
    protected Map<String, Object> parameters = new HashMap<>();

    @Given("Parent1 {string} works at {string} as {string} for {int} and can parent the child {string}")
    public void parent1(String parent1Name, String parent1WorkPlace, String parent1WorkType, int parent1Salary, String parent1Parenting) {
        parameters.put("Parent1Name", parent1Name);
        parameters.put("Parent1WorkPlace", parent1WorkPlace);
        parameters.put("Parent1WorkType", parent1WorkType);
        parameters.put("Parent1Salary", parent1Salary);
        parameters.put("Parent1Parenting", parent1Parenting);
    }

    @Given("Parent2 {string} works at {string} as {string} for {int} and can parent the child {string}")
    public void parent2(String parent2Name, String parent2WorkPlace, String parent2WorkType, int parent2Salary, String parent2Parenting) {
        parameters.put("Parent2Name", parent2Name);
        parameters.put("Parent2WorkPlace", parent2WorkPlace);
        parameters.put("Parent2WorkType", parent2WorkType);
        parameters.put("Parent2Salary", parent2Salary);
        parameters.put("Parent2Parenting", parent2Parenting);
    }

    @Given("parents live together ? {string}")
    public void liveTogether(String liveTogether) {
        parameters.put("LiveTogether", liveTogether);
    }

    @Given("child live in {string} with parent {string}")
    public void child(String childResidence, String childLivesWithParent) {
        parameters.put("ChildResidence", childResidence);
        parameters.put("ChildLivesWithParent", Integer.valueOf(childLivesWithParent));
    }

    @When("parents ask for CAF rights with {string}")
    public void askForRights(String scenario) {
        allocationService = Mockito.mock(AllocationService.class);
        switch (scenario) {
            case "A":
                when(allocationService.getParentDroitAllocation(parameters)).thenReturn("Parent1");
                break;
            case "B":
                when(allocationService.getParentDroitAllocation(parameters)).thenReturn("Parent2");
                break;
            case "C":
                when(allocationService.getParentDroitAllocation(parameters)).thenReturn("Parent2");
                break;
            case "D":
                when(allocationService.getParentDroitAllocation(parameters)).thenReturn("Parent1");
                break;
            case "E":
                when(allocationService.getParentDroitAllocation(parameters)).thenReturn("Parent1");
                break;
            case "F":
                when(allocationService.getParentDroitAllocation(parameters)).thenReturn("Parent1");
                break;
        }
    }

    @Then("parent who will receive the CAF is {string}")
    public void parentWithRights(String parentWithRights) {
        assertEquals(parentWithRights, allocationService.getParentDroitAllocation(parameters));
    }
}
