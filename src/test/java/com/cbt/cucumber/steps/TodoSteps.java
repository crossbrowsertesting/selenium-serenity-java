package com.cbt.cucumber.steps;

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

        //String browser = System.getProperty("browser");
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
