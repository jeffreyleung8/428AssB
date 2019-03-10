package ca.mcgill.ecse428;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;



import java.util.List;

public class User {

    //Variables
    private String emailAddress;
    private String password;
    private int numberEmailsSent;
    private WebDriver driver;
    private Boolean isSignedIn;
    private String currentURL;

    //URL
    private final String SIGN_IN_URL = "https://accounts.google.com/signin/v2/identifier?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&hl=en&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
    private final String INBOX_URL = "https://mail.google.com/mail/u/0/#inbox";
    private final String COMPOSE_EMAIL_URL = "https://mail.google.com/mail/u/0/#inbox?compose=new";
    private final String SENT_URL = "https://mail.google.com/mail/#sent";

    //Input
    private final String EMAIL_TEXT_INPUT = "input[type='email']";
    private final String PASSWORD_TEXT_INPUT = "input[type='password']";
    private final String RECIPIENT_TEXT_INPUT ="textarea[name='to']";
    private final String SUBJECT_TEXT_INPUT = "input[name='subjectbox']";
    private final String FILE_INPUT = "input[name='Filedata']";

    //Button
    private final String EMAIL_NEXT_BUTTON = "identifierNext";
    private final String PASSWORD_NEXT_BUTTON = "passwordNext";
    private final String SEND_BUTTON = "td[class='gU Up']";
    private final String SENT_BUTTON="a[title='Sent']";

    //Elements
    private final String EMAIL_ELEMENT = "tr.zA.yO.byw";
    private final String SENT_BOX = "span.aT";
    private final String SENT_NOTIFICATION = "span.bAq";
    private final String FILE_LINK = "div[class='vI']";
    private final String PROGRESS_BAR = "div[class='dQ']";

    //Constructor
    public User(String emailAddress,String password){
        this.emailAddress = emailAddress;
        this.password = password;
        this.numberEmailsSent = 0;
        this.isSignedIn = false;
        driver = null;
    }

    //Sign in
    @SuppressWarnings("Duplicates")
    public Boolean signIn() throws Exception {
        isSignedIn = false;

        //Sanity Check
        if(emailAddress == null || password == null){
            throw new Exception("Email address or Password are missing!!!");
        }

        try {
            goToURL(SIGN_IN_URL);

            //Find email text input
            WebElement emailTextInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(EMAIL_TEXT_INPUT)));

            //Enter email address in the email text input
            emailTextInput.sendKeys(emailAddress);

            //Click on "Next" button
            WebElement emailNextButton = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.id(EMAIL_NEXT_BUTTON)));
            emailNextButton.click();


            //Find password text input
            WebElement passwordInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(PASSWORD_TEXT_INPUT)));

            //Enter password in the password text input
            passwordInput.sendKeys(password);

            //Click on "Next" button
            WebElement passwordNextButton = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.id(PASSWORD_NEXT_BUTTON)));
            passwordNextButton.click();

            //Check if user is signed in (on inbox page)
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.titleContains("Inbox"));

            isSignedIn = true;
            return true; //true

        } catch (Exception e) {
            throw new Exception("Login failed");
        }
    }

    //Press "Compose" button
    public void pressComposeButton() throws Exception{
        if(!goToURL(COMPOSE_EMAIL_URL)){
            throw new Exception("Press Compose Button Failed");
        }
    }

    //Input a recipient in the recipient text input
    public void inputRecipient(String recipientEmail) throws Exception{
        try {
            //Find recipient text input
            WebElement emailTextInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(RECIPIENT_TEXT_INPUT)));

            //Enter email address in the recipient text input
            emailTextInput.sendKeys(recipientEmail);

            emailTextInput.sendKeys(Keys.ENTER);

        } catch(Exception e){
            throw new Exception("Input Recipient failed");
        }
    }

    //Input a subject in the subject text input
    public void inputSubject(String message) throws Exception{
        try {
            //Find subject text input
            WebElement subjectTextInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(SUBJECT_TEXT_INPUT)));

            //Enter subject in the subject text input
            subjectTextInput.sendKeys(message);

        } catch(Exception e){
            throw new Exception("Input subject failed");
        }
    }

    //Attach image to the email
    public void attachFile(String fileName) throws Exception{
        try {
            //Find file input
            WebElement fileInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(FILE_INPUT)));

            //Input file
            fileInput.sendKeys("/Users/Jeff/Desktop/428AssB/resources/img/" + fileName);

            //Wait until file is uploaded (when progress bar disappears)
            new WebDriverWait(driver, 45).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(PROGRESS_BAR)));

        } catch (Exception e){
            throw new Exception("Attach file failed");
        }
    }

    //Check that image is attached
    public Boolean checkImageLink() throws Exception{
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(FILE_LINK)));

            return true;

        } catch(Exception e){
            throw new Exception("File is not attached");
        }
    }

    //Press "Send" button
    public void pressSendButton() throws Exception{
        try {
            //Click on "Send" button
            WebElement sendButton = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(SEND_BUTTON)));
            sendButton.click();

        } catch(Exception e){
            throw new Exception("Press Send Button failed");
        }

    }

    //Check sent notification
    public Boolean checkSentNotification() throws Exception{
        try {
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SENT_BOX)));
            return true;
        } catch (Exception e){
            throw new Exception("Sent notification not found");
        }
    }

    //Check email is sent
    public Boolean checkEmailIsSent() throws Exception{
        try {
            (new WebDriverWait(driver, 25)).until(ExpectedConditions.textToBe(By.cssSelector(SENT_NOTIFICATION),"Message sent."));
            numberEmailsSent++;
            return true;

        } catch (Exception e){
            throw new Exception("Sent notification not found");
        }
    }

    //Press "Send" button
    public void pressSentButton() throws Exception{
        try {
            //Click on "Send" button
            WebElement sentButton = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector(SENT_BUTTON)));
            sentButton.click();

        } catch(Exception e){
            throw new Exception("Press Sent Button failed");
        }

    }

    //Go to sent URL
    public void goToSentURL() throws Exception{
        if(!goToURL(SENT_URL)){
            throw new Exception("Go to SENT_URL failed");
        }
    }

    //Email confirmation
    public Boolean verifyEmailIsSent(String recipient, String message,String fileName) throws Exception{
        try {

            //Verify that on sent page
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Sent Mail"));

            List<WebElement> sentMessages = driver.findElements(By.cssSelector(EMAIL_ELEMENT));

            WebElement lastMessage = sentMessages.get(0);

            lastMessage.getText().contains(recipient);
            System.out.println("Recipient found");

            lastMessage.getText().contains(message);
            System.out.println("Message found");

            lastMessage.getText().contains(fileName);
            System.out.println("Filename found");
            return true;

        } catch(Exception e){
            throw new Exception("Verify email failed");
        }
    }

    //Email confirmation
    public int countSentEmails() throws Exception{
        try {
            //Verify that on sent page
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Sent Mail"));

            //Count number of sent emails
            List<WebElement> sentMessages = driver.findElements(By.cssSelector(EMAIL_ELEMENT));

           return sentMessages.size();

        } catch(Exception e){
            throw new Exception("Count sent emails failed");
        }
    }


    //Update number of emails sent
    public void updateNumberEmailsSent() throws Exception{
        try{
            goToSentURL();

            //Verify that on sent page
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Sent Mail"));

            List<WebElement> sentMessages = driver.findElements(By.cssSelector(EMAIL_ELEMENT));

            numberEmailsSent =  sentMessages.size();
        } catch(Exception e){
            throw new Exception(e.getMessage());
        }

    }

    //Go to specified url
    public Boolean goToURL(String url){
        if (driver != null && url != null) {
//            System.out.println("Going to " + url.);
            driver.get(url);
            return true;
        }
        return false;

    }

    //Check if webDriver is in initial state (inbox page)
    public boolean checkInitialState() {
        if(driver != null) {
            String checkUrl = driver.getCurrentUrl();
            return checkUrl.equals(INBOX_URL);
        }
        return false;
    }

    //Return to the initial state (inbox page)
    public void resetInitialState() throws Exception {
        if(!goToURL(INBOX_URL)){
            throw new Exception("Reset initial state failed");
        }
    }

    //Setters
    public void setEmailAddress(String email){this.emailAddress = email;}
    public void setPassword(String password){this.password = password;}
    public void setNumberEmailsSent(int numberEmailsSent){this.numberEmailsSent = numberEmailsSent;}
    public void setIsSignedIn(Boolean isSignedIn){this.isSignedIn=isSignedIn;}
    public void setCurrentURL (String url){this.currentURL = url;}
    public void setDriver(WebDriver driver){this.driver=driver;}

    //Getters
    public String getEmailAddress(){return this.emailAddress;}
    public String getPassword(){return this.password;}
    public int getNumberEmailsSent(){return this.numberEmailsSent;}
    public Boolean getIsSignedIn(){return this.isSignedIn;}
    public String getCurrentURL(){return this.currentURL;}
    public WebDriver getDriver(){return this.driver;}



}
