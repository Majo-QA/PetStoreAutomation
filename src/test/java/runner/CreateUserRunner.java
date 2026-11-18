package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("Feature/CreateUser/CreateUser.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps,hook")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:build/reports/cucumber/create-user-report.html")
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@smoke")

public class CreateUserRunner {
}