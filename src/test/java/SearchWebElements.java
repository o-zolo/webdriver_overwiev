import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchWebElements {

    private static final String BASE_URL = "https://www.greencity.social/#/greenCity";
    private static final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private static final Long ONE_SECOND_DELAY = 1000L;
    private static WebDriver driver;

    // Overload
    protected void presentationSleep() {
        presentationSleep(1);
    }

    // Overload
    protected void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS)); // 0 by default
        driver.manage().window().maximize();
        System.out.println("@BeforeAll executed");
    }

    @BeforeEach
    public void setupThis() {
        setUp();
        driver.get(BASE_URL);
        System.out.println("\t@BeforeEach executed");
    }

    @AfterEach
    public void tearThis() {
        driver.quit();
        System.out.println("\t@AfterEach executed");
    }


    private static Stream<Arguments> providerUsers() {
        return Stream.of(
                Arguments.of("box.zolotar@gmail.com","Qwerty1234!","Qwerty")
        );
    }

    @ParameterizedTest(name = "{index} => email={0}, password={1}")
    @MethodSource("providerUsers")
    public void signInTest(String email, String password) {
        driver.findElement(By.cssSelector("a.header_sign-in-link")).click();
        presentationSleep();
        //Explicit waits
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement Email = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.id("email")));
        Email.click();
        Email.clear();
        Email.sendKeys(email);
        presentationSleep();
        WebElement Password = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.id("password")));
        Password.click();
        Password.clear();
        Password.sendKeys(password);
        presentationSleep();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //
        presentationSleep();
        driver.findElement(By.cssSelector("button.greenStyle")).click();
        presentationSleep();
        String actualUserName = driver.findElement(By.cssSelector("ul.nav-global-button >.body-2")).getText();
        String expectedUserName = "Olha Z";
        presentationSleep();
        Assertions.assertEquals(expectedUserName, actualUserName);
        presentationSleep();
        driver.findElement(By.cssSelector("ul.nav-global-button >.body-2")).click();
        driver.findElement(By.cssSelector("ul.dropdown-list")).click();
        System.out.println("\t\tTest testUi() executed");
    }
    private static Stream<Arguments> providerInvalidUsers() {
        return Stream.of(
                Arguments.of("box.zolotar@gmail.com","Qwerty","Password must be at least 8 characters long without spaces")
        );
    }
    @ParameterizedTest (name = "{index} => email={0},password={1},message={2}")
    @MethodSource("providerInvalidUsers")
    public void passwordIsNotValidTest(String email, String password, String message) {
        driver.findElement(By.cssSelector("a.header_sign-in-link")).click();
        presentationSleep();
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        presentationSleep();
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        presentationSleep();
        // Search Web Element using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement SignInButton = (WebElement) (js.executeScript(
        "return document.querySelector('button.greenStyle')"));
        SignInButton.click();
        //
        driver.findElement(By.cssSelector("button.greenStyle")).click();
        presentationSleep();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        assertThat(driver.findElement(By.xpath("//div[contains(@class,'validation-password-error')]")).getText(), is(message));
    }
    private static Stream<Arguments> providerComments() {
        return Stream.of(
                Arguments.of("Olha_0")
        );
    }
    @ParameterizedTest(name = "{index} => comment={0}")
    @MethodSource("providerComments")
    public void createCommentTest(String comment) {
        //Explicit waits
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement signInLink = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector("a.header_sign-in-link")));
        System.out.println("signInLink CSS class = " + signInLink.getAttribute("class"));
        signInLink.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //
        presentationSleep();
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("box.zolotar@gmail.com");
        presentationSleep();
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Qwerty1234!");
        presentationSleep();
        driver.findElement(By.cssSelector("button.greenStyle")).click();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        //Explicit waits
        WebElement EcoNewsLink = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//li[@class='nav-left-list ng-star-inserted']/a[@href='#/news']")));
        EcoNewsLink.click();
        presentationSleep();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //
        driver.findElement(By.xpath("//mat-icon[@class='mat-icon notranslate material-icons mat-icon-no-color']")).click();
        WebElement CountComments = driver.findElement(By.cssSelector("div.wrapper >p.total-count"));
        //scroll to WebElement CountComments using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);",
            CountComments);
        //
        driver.findElement(By.xpath("//div[@class='comment-textarea']")).sendKeys(comment);
        // Search Web Element using JavaScript
        WebElement CommentButton = (WebElement) (js.executeScript(
                "return document.querySelector('button.primary-global-button')"));
        CommentButton.click();
        //
        String actualComment = driver.findElement(By.xpath("//app-comments-list/div[1]/div[@class='comment-main-text']")).getText();
        String expectedComment = comment;
        Assertions.assertEquals(expectedComment, actualComment);
        driver.findElement(By.cssSelector("ul.nav-global-button >.body-2")).click();
        driver.findElement(By.cssSelector("ul.dropdown-list")).click();
        System.out.println("\t\tTest testUi() executed");
    }

}

