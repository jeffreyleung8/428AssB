package ca.mcgill.ecse428.cucumber;

import ca.mcgill.ecse428.User;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.*;

public class StepDefinitions {

    //Variables
    private WebDriver driver;
    private User user;

    //Constant
    private String EMAIL_ADDRESS = "ecse428student@gmail.com";
    private String PASSWORD = "Helloworld123";
    private String PATH_TO_CHROME_DRIVER = "/Users/Jeff/Downloads/chromedriver";


    @Before
    public void setUp(){
        user = new User(EMAIL_ADDRESS,PASSWORD);
    }


    @Given ("^I am logged in to gmail$")
    public void login(){
        try {
            //Set up driver
            setupSeleniumWebDrivers();
            user.setDriver(driver);

            //Login
            System.out.println("Attempt to login...");
            user.signIn();

            //Check
            assertTrue(user.getIsSignedIn());
            System.out.println("Login successful!!!");

            //Update number of emails sent
            user.updateNumberEmailsSent();
            System.out.println("Number of sent emails: " + user.getNumberEmailsSent());

            //Return to inbox
            user.resetInitialState();

            //Check initial state
            System.out.println("Check initial state : " + user.checkInitialState());

            System.out.println();

        } catch(Exception e){
            System.out.println("Login failed!!!");
        }

    }
    @When("^I send an email to valid \"([^\"]*)\"$")
    public void composeEmail(String recipientAddress){
        try {
            //Press Compose Button
            user.pressComposeButton();
            System.out.println("Compose button pressed");

            //Input recipient
            user.inputRecipient(recipientAddress);
            System.out.println("Recipient is inserted");

            System.out.println();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @When("^\"([^\"]*)\" is attached$")
    public void attachFile(String file){
        try {
            //Attach file
            user.attachFile(file);
            System.out.println("File is inserted");

            //Check file is attached
            System.out.println("File is attached : " + user.checkImageLink());

            System.out.println();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @When("^\"([^\"]*)\" is the subject$")
    public void sendEmail(String message){
        try {
            //Input subject
            user.inputSubject(message);
            System.out.println("Subject is inserted");

            //Press Send Button
            user.pressSendButton();
            System.out.println("Send button pressed");

            //Check sent notification
            System.out.println("Sent notification is found : " + user.checkSentNotification());

            //Check email is sent
            System.out.println("Email is sent : " + user.checkEmailIsSent());

            System.out.println();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Then ("^an email should be found in the sent folder to \"([^\"]*)\" with \"([^\"]*)\" attached with subject \"([^\"]*)\"$")
    public void emailConfirmation(String recipient,String file, String message){
        try{
            //See sent messages
            user.pressSentButton();

            //Verify sent email is here
            System.out.println("Email sent is found : " + user.verifyEmailIsSent(recipient,message,file));


            //Verify number of messages sent
            assertEquals(user.getNumberEmailsSent(),user.countSentEmails());

            //Return to Inbox
            user.resetInitialState();

            //Check initial state (Inbox)
            System.out.println("Check initial state : " + user.checkInitialState());

            driver.close();

            System.out.println();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Set up selenium driver
    private void setupSeleniumWebDrivers() {
        try {
            if (driver == null) {
                System.out.println("Looking for ChromeDriver... ");
                System.setProperty("webdriver.chrome.driver", PATH_TO_CHROME_DRIVER);
                driver = new ChromeDriver();
                driver.manage().window().setSize(new Dimension(800,700));
                System.out.print("Chrome Driver is successfully set up!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
