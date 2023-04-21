package ch.hearc.cafheg.business.allocations;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/ch.hearc.cafheg.business.allocations",
        glue = "ch.hearc.cafheg.business.allocations"
)
public class RunAllocationServiceTest { }
