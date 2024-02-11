import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
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

    public static User MATCHMAKER =
            new User("15513877642", "a@a.com", "a", "", "", "", new Date());
    public static User GROOM =
            new User("122132131231", "g@g.com", "g", "", "", "", new Date());

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


    static void redirectWait() throws InterruptedException { Thread.sleep(2000); }
    static String js(String script) { return (String) ((JavascriptExecutor) DRIVER).executeScript(script); }
    String getSecret() { return js("return localStorage.getItem('secret');"); }
    static void zoom(double factor) { js("document.body.style.zoom='" + factor + "'"); }
    static void acceptRedirect(String urlToken) throws InterruptedException {
        redirectWait();
        assertTrue(DRIVER.getCurrentUrl().contains(urlToken));
    }
    static WebElement getActiveModal() { return DRIVER.findElement(By.cssSelector("dialog[open]")); }
    static String[] getElClasses(WebElement el) { return el.getAttribute("class").split(" "); }


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

    void fillUser(User newbie, String role) {
        if (role.isBlank()) DRIVER.findElements(By.className("roleSettings")).stream().findFirst().ifPresent(WebElement::click);
        else js("document.getElementsByClassName('" + role + "')[0].click()");
        DRIVER.findElement(By.id("nameSettings")).sendKeys(newbie.name());
        new Select(DRIVER.findElement(By.id("citySettings"))).selectByIndex(1);
        new Select(DRIVER.findElement(By.id("eduSettings"))).selectByIndex(1);
        DRIVER.findElement(By.id("aboutSelfSettings")).sendKeys(newbie.about());
        DRIVER.findElement(By.id("aboutPartnerSettings")).sendKeys(newbie.email());
        DRIVER.findElement(By.id("dateSettings")).sendKeys(new SimpleDateFormat("dd.MM.yyyy").format(newbie.dateOfBirth()));
    }

    int getArrowsAmount() { return Integer.parseInt(DRIVER.findElement(By.id("arrowsAmountRooms")).getText()); }

    @Test // UC1
    void register() throws InterruptedException {
        var newbie = User.rand();
        register(newbie);
        acceptRedirect("login");
    }

    @Test // UC2
    void login() throws InterruptedException {
        login(MATCHMAKER);
        acceptRedirect("rooms");
        assertNotNull(getSecret());
    }

    @Test // UC3 & UC4
    void fillUserInfo() throws InterruptedException {
        var newbie = User.rand();
        register(newbie);
        login(newbie);
        redirectWait();
        zoom(0.75);
        fillUser(newbie, "");
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

    @Test // UC6
    void logout() throws InterruptedException {
        login(MATCHMAKER);
        DRIVER.findElement(By.id("logout")).click();
        redirectWait();
        acceptRedirect("login");
        assertNull(getSecret());
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
    void dismissCompetition() throws InterruptedException {
        login(GROOM);
        redirectWait();
        DRIVER.findElement(By.id("dismissRooms")).click();
        DRIVER.switchTo().alert().accept();
        assertEquals(0, DRIVER.findElements(By.id("dismissRooms")).size());
    }

    @Test // UC13
    void commentCompetition() throws InterruptedException {
        login(GROOM);
        redirectWait();
        var clickCmd = "document.getElementsByClassName('commentCompetitionRooms')[0].click()";
        js(clickCmd);
        var comment = "sample text";
        getActiveModal().findElement(By.className("newCommentInput")).sendKeys(comment);
        js("document.getElementsByClassName('newCommentBtn')[1].click()");
        js(clickCmd);
        assertTrue(DRIVER
                .findElements(By.className("chat-bubble"))
                .stream()
                .anyMatch(it -> it.getText().contains(comment))
        );
    }

    @Test // UC14
    void followCompetition() throws InterruptedException {
        login(GROOM);
        redirectWait();
        js("document.getElementsByClassName('followCompetitionRooms')[0].click()");
        DRIVER.switchTo().alert().accept();
        assertTrue(true);
    }

    @Test // UC15
    void pushArrow() throws InterruptedException {
        var newbie = User.rand();
        register(newbie);
        login(newbie);
        redirectWait();
        zoom(0.75);
        fillUser(newbie, "groom");
        zoom(1);
        DRIVER.findElement(By.id("nextSettings")).click();
        redirectWait();
        var oldVal = getArrowsAmount();
        DRIVER.findElement(By.id("pushArrowRooms")).click();
        redirectWait();
        var newVal = getArrowsAmount();
        assertEquals(oldVal - 1, newVal);
    }

    @Test // UC16
    void buyArrow() throws InterruptedException {
        login(GROOM);
        redirectWait();
        var oldVal = getArrowsAmount();
        DRIVER.findElement(By.id("buyArrowsRooms")).click();
        getActiveModal().findElement(By.id("acceptDialogBtn")).click();
        DRIVER.switchTo().alert().accept();
        var newVal = getArrowsAmount();
        assertEquals(oldVal + 1, newVal);
    }

    @Test // UC17
    void showCompetitions() throws InterruptedException {
        login(MATCHMAKER);
        redirectWait();
        assertFalse(DRIVER.findElements(By.className("competitionCard")).isEmpty());
    }

    @Test // UC18
    void createCompetitionTemplate() throws InterruptedException {
        login(MATCHMAKER);
        redirectWait();
        DRIVER.findElement(By.id("createCompetitionRooms")).click();
        var modal = getActiveModal();
        modal.findElement(By.id("competitionCreationName")).sendKeys("Selenuim UC18");
        new Select(modal.findElement(By.id("competitionCreationCity"))).selectByIndex(1);
        assertTrue(true);
    }

    @Test // UC19
    void showUserQuestionnaire() throws InterruptedException {
        login(MATCHMAKER);
        redirectWait();
        DRIVER.findElement(By.className("asyncDataTableUser"))
                .findElements(By.className("userInfoBtn"))
                .stream()
                .findFirst()
                .ifPresent(WebElement::click);
        assertNotNull(getActiveModal());
    }

    @Test // UC22
    void createMarriageReport() throws InterruptedException {
        // todo
    }

    @Test // UC23
    void banUser() throws InterruptedException {
        login(MATCHMAKER);
        redirectWait();
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

    @Test // UC24
    void assignCompetitors() throws InterruptedException {
        createCompetitionTemplate();
        var oldNumOfComps = DRIVER.findElements(By.className("competitionCard")).size();
        var task = new Faker().lorem().characters(14);
        js(String.format("await window.assignDebug(['%s', '%s'])", task, task));
        getActiveModal().findElement(By.id("acceptDialogBtn")).click();
        redirectWait();
        DRIVER.switchTo().alert().accept();
        DRIVER.navigate().refresh();
        redirectWait();
        var newNumOfComps = DRIVER.findElements(By.className("competitionCard")).size();
        assertEquals(oldNumOfComps + 1, newNumOfComps);
    }

    /*@AfterAll
    static void shutdown() { DRIVER.quit(); }*/
}
