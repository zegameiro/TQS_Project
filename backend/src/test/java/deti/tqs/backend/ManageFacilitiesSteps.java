package deti.tqs.backend;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ManageFacilitiesSteps {
    private WebDriver driver;

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        ChromeOptions options = new ChromeOptions().setBinary("/usr/bin/brave-browser");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:3002");
    }

    @And("I login as admin")
    public void iLoginAsAdmin() {
        driver.findElement(By.id("email")).sendKeys("luissantos@beautyplaza.pt");
        driver.findElement(By.id("password")).sendKeys("Onepassword123");
        driver.findElement(By.cssSelector(".bg-indigo-600")).click();
    }

    @And("I click on \"Admin Page\"")
    public void iClickOnAdminPage() {
        driver.findElement(By.linkText("Admin Page")).click();
    }

    @And("I click on \"Facilities\"")
    public void iClickOnFacilities() {
        driver.findElement(By.id(":r5:-tab-2")).click();
    }

    @And("I click on \"Create Facility\"")
    public void iClickOnCreateFacility() {
        driver.findElement(By.cssSelector(".ml-2")).click();
    }

    @And("I fill in {string} with {string}")
    public void iFillInWith(String arg0, String arg1) {
        if (arg0.equals("Name")) {
            driver.findElement(By.id("name")).sendKeys(arg1);
        } else if (arg0.equals("City")) {
            driver.findElement(By.id("city")).sendKeys(arg1);
        } else if (arg0.equals("Street Name")) {
            driver.findElement(By.id("streetName")).sendKeys(arg1);
        } else if (arg0.equals("Postal Code")) {
            driver.findElement(By.id("postalCode")).sendKeys(arg1);
        } else if (arg0.equals("Phone Number")) {
            driver.findElement(By.id("phoneNumber")).sendKeys(arg1);
        } else if (arg0.equals("Room Capacity")) {
            driver.findElement(By.id("maxRoomsCapacity")).sendKeys(arg1);
        }
    }

    @And("I click on the button to \"Create Facility\"")
    public void iClickOnTheButtonToCreateFacility() {
        driver.findElement(By.cssSelector(".flex:nth-child(7) .group:nth-child(1) .flex")).click();
    }

    @Then("I should see {string} in the list of facilities")
    public void iShouldSeeInTheListOfFacilities(String arg0) {
        assertThat(driver.findElement(By.cssSelector(".text-lg")).getText(), equalTo(arg0));
    }

}
