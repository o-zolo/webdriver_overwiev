import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebDriverOverviewTest {
    @FindBy(css = "app-header:nth-child(1) .ubs-header-sing-in-img")
    private WebElement signInButton;
    @FindBy(css = ".ng-star-inserted > h1")
    private WebElement welcomeText;
    @FindBy(css = "h2:nth-child(2)")
    private WebElement signInDetailsText;
    @FindBy(css = "label:nth-child(1)")
    private WebElement emailLabel;
    @FindBy(id = "email")
    private WebElement emailInput;
    @FindBy(id = "password")
    private WebElement passwordInput;
    @FindBy(css = ".ubsStyle")
    private WebElement signInSubmitButton;
    @FindBy(css=".mat-simple-snackbar > span")
    private WebElement result;
    @FindBy(xpath = "//div[contains(@class,'alert-general-error')]")
    private WebElement errorMessage;
    @FindBy(xpath = "//div[contains(@class,'validation-password-error')]")
    private WebElement errorPassword;
    @FindBy(xpath = "//*[@id='email-err-msg']/app-error/div")
    private WebElement errorEmail;
    @FindBy(css = "a.forgot-password-ubs")
    private WebElement forgotPasswordLink;
    @FindBy(css = ".ubs-send-btn" )
    private WebElement submitButtonLink;
    @FindBy(css = "app-header:nth-child(1) .body-2.ubs-user-name")
    //@FindBy(css = "a.body-2.ubs-user-name")
    private WebElement userName;
    private static WebDriver driver;
    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.greencity.social/");
        driver.manage().window().setSize(new Dimension(1264, 798));
    }
    @BeforeEach
    public void initPageElements() {
        PageFactory.initElements(driver, this);
    }
    @Test
    public void verifyTitle() {
        assertEquals("GreenCity", driver.getTitle());
        assertThat(driver.getTitle(), is("GreenCity"));
    }
    @ParameterizedTest
    @CsvSource( {
            "box.zolotar@gmail.com, Qwerty1234!"
    })
    public void signIn(String email, String password) {
        signInButton.click();
        assertThat(welcomeText.getText(), is("Welcome back!"));
        assertThat(signInDetailsText.getText(), is("Please enter your details to sign in."));
                assertThat(emailLabel.getText(), is("Email"));
        emailInput.sendKeys("box.zolotar@gmail.com");
        assertThat(emailInput.getAttribute("value"), is(email));
        passwordInput.sendKeys("Qwerty1234!");
        assertThat(passwordInput.getAttribute("value"), is(password));
        signInSubmitButton.click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        assertEquals("Olha", userName.getText());
        assertThat(userName.getText(), is("Olha"));
    }

    @ParameterizedTest
    @CsvSource({
            "Please check if the email is written correctly"
    })
    public void emailInNotValid(String message) {
        signInButton.click();
        emailInput.sendKeys("samplestesgreencity.com");
        passwordInput.sendKeys("uT346^^^erw");
        assertThat(errorEmail.getText(), is(message));
    }
    @ParameterizedTest
    @CsvSource({
            "Password must be at least 8 characters long without spaces"
    })
    public void passwordlInNotValid(String message) {
        signInButton.click();
        emailInput.sendKeys("box.zolotar@gmail.com");
        passwordInput.sendKeys("uTerw");
        emailInput.click();
        assertThat(errorPassword.getText(), is(message));
    }
    @ParameterizedTest
    @CsvSource({
            "friday@mailforspam.com"
    })
    public void forgotPassword(String message) {
        signInButton.click();
        forgotPasswordLink.click();
        emailInput.sendKeys("friday@mailforspam.com");
        submitButtonLink.click();
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
