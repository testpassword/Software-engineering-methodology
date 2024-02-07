import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

record User(String phone, String email, String pass, String name,
            String city, String about,
            Date dateOfBirth
) {
    static User rand() {
        var f = new Faker();
        var about = f.lorem().characters(8);
        return new User(
                f.phoneNumber().cellPhone().replaceAll("[^0-9]", ""),
                f.internet().emailAddress(),
                f.internet().password(),
                f.name().fullName(),
                "Udomlya",
                about,
                new Faker().date().birthday(18, 55)
        );
    }
}

public class UITests {

    static WebDriver DRIVER;
    static final String DRIVER_PATH = "/usr/local/bin/chromedriver";
    static final List<String> BROWSER_ARGS = Arrays.asList("--disable-web-security" , "--remote-allow-origins=*");
    static final Map<String, Integer> PREFS = new HashMap<>() {{ put("profile.default_content_setting_values.notifications", 2); }};
    static final String BASE_URL = "http://localhost:3000/";

    User MATCHMAKER = new User("15513877642", "a@a.com", "a", "", "", "", new Date());
    User GROOM = new User("122132131231", "g@g.com", "g", "", "", "", new Date());


    static void redirectWait() throws InterruptedException { Thread.sleep(500); }
    String getSecret() { return (String) ((ChromeDriver) DRIVER).executeScript("return localStorage.getItem('secret');"); }
    static void zoom(double factor) { ((JavascriptExecutor) DRIVER).executeScript("document.body.style.zoom='" + factor + "'"); }
    static void acceptRedirect(String urlToken) throws InterruptedException {
        redirectWait();
        assertTrue(DRIVER.getCurrentUrl().contains(urlToken));
    }


    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        ChromeOptions opts = new ChromeOptions();
        opts.setExperimentalOption("prefs", PREFS);
        opts.addArguments(BROWSER_ARGS);
        DRIVER = new ChromeDriver(opts);
        DRIVER.manage().window().maximize();
    }

    void login(User u) throws InterruptedException {
        DRIVER.get(BASE_URL + "login");
        DRIVER.findElement(By.id("emailLogin")).sendKeys(u.email());
        DRIVER.findElement(By.id("passwordLogin")).sendKeys(u.pass());
        DRIVER.findElement(By.id("login")).click();
        redirectWait();
    }

    void register(User u) throws InterruptedException {
        DRIVER.get(BASE_URL + "login");
        DRIVER.findElement(By.id("toRegister")).click();
        redirectWait();
        DRIVER.findElement(By.id("phoneRegister")).sendKeys(u.phone());
        DRIVER.findElement(By.id("emailRegister")).sendKeys(u.email());
        DRIVER.findElement(By.id("passwordRegister")).sendKeys(u.pass());
        DRIVER.findElement(By.id("agreeRegister")).click();
        DRIVER.findElement(By.id("register")).click();
        redirectWait();
    }

    @Test // UC2
    void login() throws InterruptedException {
        login(MATCHMAKER);
        acceptRedirect("rooms");
        assertNotNull(getSecret());
    }

    @Test // UC6
    void logout() throws InterruptedException {
        login(MATCHMAKER);
        DRIVER.findElement(By.id("logout")).click();
        redirectWait();
        acceptRedirect("login");
        assertNull(getSecret());
    }

    @Test // UC1
    void register() throws InterruptedException {
        var newbie = User.rand();
        register(newbie);
        acceptRedirect("login");
    }

    @Test // UC3 & UC4
    void fillUserInfo() throws InterruptedException {
        var newbie = User.rand();
        register(newbie);
        login(newbie);
        redirectWait();
        zoom(0.75);
        DRIVER.findElements(By.className("roleSettings"))
                .stream()
                .findFirst()
                .ifPresent(WebElement::click);
        DRIVER.findElement(By.id("nameSettings")).sendKeys(newbie.name());
        new Select(DRIVER.findElement(By.id("citySettings"))).selectByIndex(1);
        new Select(DRIVER.findElement(By.id("eduSettings"))).selectByIndex(1);
        DRIVER.findElement(By.id("aboutSelfSettings")).sendKeys(newbie.about());
        DRIVER.findElement(By.id("aboutPartnerSettings")).sendKeys(newbie.email());
        DRIVER.findElement(By.id("dateSettings")).sendKeys(new SimpleDateFormat("dd.MM.yyyy").format(newbie.dateOfBirth()));
        zoom(1);
        DRIVER.findElement(By.id("nextSettings")).click();
        acceptRedirect("rooms");
    }

    @Test // UC5
    void deleteAccount() throws InterruptedException {
        fillUserInfo();
        DRIVER.findElement(By.id("deleteAccountRooms")).click();
        DRIVER.switchTo().alert().accept();
        acceptRedirect("login");
    }

    @Test // UC23
    void banUser() throws InterruptedException {
        login(MATCHMAKER);
        Thread.sleep(3000);
        var before = DRIVER.findElements(By.className("isBanned")).size();
        DRIVER.findElement(By.className("asyncDataTableUser"))
                .findElements(By.className("banUser"))
                .stream()
                .findFirst()
                .ifPresent(WebElement::click);
        DRIVER.switchTo().alert().accept();
        var after = DRIVER.findElements(By.className("isBanned")).size();
        assertEquals(before + 1, after);
    }

    @Test // UC7
    void changeRole() throws InterruptedException {
        fillUserInfo();
        redirectWait();
        DRIVER.findElement(By.id("settingsRooms")).click();
        redirectWait();
        var oldRole = DRIVER.findElement(By.cssSelector("h2.roleName.text-grey-100")).getText();
        DRIVER.findElements(By.className("roleSettings")).stream().toList().get(1).click();
        var newRole = DRIVER.findElement(By.cssSelector("h2.roleName.text-grey-100")).getText();
        assertNotEquals(oldRole, newRole);
    }

    @Test // UC9
    void dismiss() throws InterruptedException {
        login(GROOM);
        redirectWait();
        DRIVER.findElement(By.id("dismissRooms")).click();
        DRIVER.switchTo().alert().accept();
        assertEquals(0, DRIVER.findElements(By.id("dismissRooms")).size());
    }

    /*@AfterAll
    static void shutdown() { DRIVER.quit(); }*/
}
