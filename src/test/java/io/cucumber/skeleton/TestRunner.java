package io.cucumber.skeleton;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/main/resources/io.cucumber.skeleton",
        glue = "src/test/java/io/cucumber/skeleton",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true
)
public class TestRunner {
    public TestRunner() {
    }
}