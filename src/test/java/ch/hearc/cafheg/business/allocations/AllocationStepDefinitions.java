package ch.hearc.cafheg.business.allocations;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static org.junit.Assert.*;

public class AllocationStepDefinitions {

    private AllocationService allocationService;
    protected Map<String, Object> parameters;

    @Given("parent 1 is {string} works at {string} as {string} for {int} and can parent the child {boolean}")
    public void parent1(String parent1Name, String parent1WorkPlace, String parent1WorkType, Integer parent1Salary, Boolean parent1Parenting) {
        parameters.put("Parent1Name",parent1Name);
        parameters.put("Parent1WorkPlace",parent1WorkPlace);
        parameters.put("Parent1WorkType",parent1WorkType);
        parameters.put("Parent1Salary",parent1Salary);
        parameters.put("Parent1Parenting",parent1Parenting);
    }

    @Given("parent 2 is {string} works at {string} as {string} for {int} and can parent the child {boolean}")
    public void parent2(String parent2Name, String parent2WorkPlace, String parent2WorkType, Integer parent2Salary, Boolean parent2Parenting) {
        parameters.put("Parent2Name",parent2Name);
        parameters.put("Parent2WorkPlace",parent2WorkPlace);
        parameters.put("Parent2WorkType",parent2WorkType);
        parameters.put("Parent2Salary",parent2Salary);
        parameters.put("Parent2Parenting",parent2Parenting);
    }

    @Given("parents live together ? {boolean}")
    public void liveTogether(Boolean liveTogether) {
        parameters.put("LiveTogether",liveTogether);
    }

    @Given("child live in {string} with parent {int}")
    public void child(String childResidence, Integer childLivesWithParent) {
        parameters.put("ChildResidence",childResidence);
        parameters.put("ChildLivesWithParent",childLivesWithParent);
    }

    @When("parents ask for CAF rights")
    public void askForRights() {
        // implémentation pour le When
    }

    @Then("parent who will receive the CAF is {string}")
    public void parentWithRights(String parentWithRights) {
        assertEquals(parentWithRights, allocationService.getParentDroitAllocation(parameters));
    }
}
