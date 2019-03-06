<h1><strong>Getting Started with Serenity and CrossBrowserTesting</strong></h1>
<em>For this document, we provide a complete example test located in our <a href="https://github.com/crossbrowsertesting/selenium-serenity-java">Serenity Java Github Repository</a>.</em>

Want a powerful and easy to use command line tool for running Selenium tests? <a href="https://www.thucydides.info/#/">Serenity</a> might be the option for you. Serenity provides language-bindings for the powerful browser-driving tool <a href="http://www.seleniumhq.org/docs/" rel="nofollow">Selenium</a>. Its use of <a href="https://github.com/cucumber/cucumber-js">Cucumber's</a>  <a href="https://docs.cucumber.io/gherkin/" rel="nofollow">Gherkin</a> language allows you to write your tests in a way that can be easily read by anyone on your team. Serenity Java integrates easily with the CrossBrowserTesting platform, so you can perform tests on a wide variety of OS/Device/Browser combinations, all from one test.
<h3>Let's walk through getting setup.</h3>
<strong>1.</strong> Start by installing <a href="https://maven.apache.org/download.cgi">Maven</a>, a software project management and comprehension tool.

<strong>2.</strong> Create a simple project with Cucumber installed  by running command:
<pre><code>
mvn archetype:generate \
-DarchetypeGroupId=io.cucumber \
-DarchetypeArtifactId=cucumber-archetype \
-DarchetypeVersion=2.3.1.2 \
-DgroupId=com.cbt \
-DartifactId=seleniumserenity \
-Dpackage=com.cbt \
-Dversion=1.0.0-SNAPSHOT \
-DinteractiveMode=false
</code></pre>
<strong>3.</strong> After changing into the seleniumserenity/ directory that was just created, add the necessary serenity dependencies, properties, and plugins to the pom.xml file. A complete pom.xml file can be found <a href="https://github.com/crossbrowsertesting/selenium-serenity-java/blob/master/pom.xml">here</a>.

<strong>4.</strong>In the same folder, create the serenity.properties file, which will be used to set capabilities, containing the following content:
<pre><code>
serenity.dry.run=false
serenity.take.screenshots=AFTER_EACH_STEP

environment.single.name=Todo_Test_Serenity
environment.single.browserName=Chrome
environment.single.version=72
environment.single.platform=Windows 10 
environment.single.record_video=true
environment.single.screenResolution=1366x768"
</code></pre>
&nbsp;

<strong>5.</strong> Create an empty file called <span class="text-java"><code>src/test/resources/features/single.feature</code></span> with the following content:
<pre><code>Feature: ToDo
  Scenario: Testing ToDos
    Given I go to my ToDo App
    When I click on all todos
    When I click archive
    Then I should have no todos</code></pre>
<strong>6.</strong> Edit the file called <span class="text-java"><code>src/test/java/com/cbt/CbtSerenityCapabilities.java</code></span> with the following content:
<pre><code>package com.cbt;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Iterator;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class CbtSerenityCapabilities{
    DesiredCapabilities capabilities = new DesiredCapabilities();

    public DesiredCapabilities newCaps() {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        String environment = System.getProperty("environment");
        
        Iterator it = environmentVariables.getKeys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (environment != null &amp;&amp; key.startsWith("environment." + environment)) {
                capabilities.setCapability(key.replace("environment." + environment + ".", ""), environmentVariables.getProperty(key));
            }
        }
            System.out.println(capabilities);
            return capabilities;
    }

    public boolean takesScreenshots() {
        return true;
    }
}</code></pre>
<strong>7.</strong> Edit the file called <span class="text-java"><code>src/test/java/com/cbt/cucumber/SingleTest.java</code></span> with the following content:
<pre><code>package com.cbt.cucumber;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features="src/test/resources/features/single.feature")
public class SingleTest { }
</code></pre>
<strong>8.</strong> Edit the file called <span class="text-java"><code>src/test/java/com/cbt/cucumber/steps/TodoSteps.java</code></span> with the following content:
<pre><code>package com.cbt.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.junit.Assert;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.*;
import java.net.URL;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import net.thucydides.core.annotations.Steps;
import java.text.ParseException;
import com.cbt.CbtSerenityCapabilities;

public class TodoSteps {
    private String username, authkey;
    private RemoteWebDriver driver;

    @Before
    public void setUp() throws Throwable {

        username = "YOUR_USERNAME"; //replace with your username be sure to encode @ with %40
        authkey = "YOUR_AUTHKEY"; //replace with your authkey

        CbtSerenityCapabilities caps = new CbtSerenityCapabilities();

        driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"), caps.newCaps());
    }
    @Given("^I go to my ToDo App$")
    public void I_go_to_my_todo_app() throws Throwable {
         driver.get("http://crossbrowsertesting.github.io/todo-app.html");
    }

    @When("^I click on all todos$")
    public void I_click_on_my_todos() throws Throwable {
        driver.findElement(By.name("todo-1")).click();
        driver.findElement(By.name("todo-2")).click();
        driver.findElement(By.name("todo-3")).click();
        driver.findElement(By.name("todo-4")).click();
        driver.findElement(By.name("todo-5")).click();
    }

    @When("^I click archive$")
    public void I_click_archive() throws Throwable {
        driver.findElement(By.linkText("archive")).click();
    }

    @Then("^I should have no todos$")
    public void I_should_have_no_todos() throws Throwable {
        List elems =  driver.findElements(By.className("done-true"));
        Assert.assertEquals(0, elems.size());
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

</code></pre>
<div class="blue-alert">

You’ll need to use your Username and Authkey to run your tests on CrossBrowserTesting. To get yours, sign up for a <a href="https://crossbrowsertesting.com/freetrial"><b>free trial</b></a> or purchase a <a href="https://crossbrowsertesting.com/pricing"><b>plan</b></a>.

</div>
<strong>9.</strong> Now you are ready to run your test using the command:
<pre><code>mvn verify -P single</code></pre>
As you can probably make out from our test, we visit a small ToDo App example, interact with our page, and use assertions to verify that the changes we've made are actually reflected in our app.

We kept it short and sweet for our purposes, but there is so much more you can do with Cucumber! Being built on top of Selenium means the sky is the limit as far as what you can do. If you have any questions or concerns, feel <a href="mailto:info@crossbrowsertesting.com">free to get in touch</a>.
