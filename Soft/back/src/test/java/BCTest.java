import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.util.stream.Stream;

public class BCTest {

    static void clickOnActiveStep(int compId) {
        UCTests.findComp(compId)
                .findElement(By.cssSelector("li.step.animate-pulse"))
                .click();
    }

    @BeforeAll
    public static void setup() {
        UCTests.setup();
    }

    @Test
    void businessProcess() throws InterruptedException {
        var br = User.rand();
        int brId = registerNewUser(br, "bride");
        UCTests.logout();
        var gr = User.rand();
        int grId = registerNewUser(gr, "groom");
        pushArrow(gr);
        UCTests.logout();
        createCompTemplate();
        int newCompId = assignCompetitors(brId, grId);
        report(br, newCompId);
        report(gr, newCompId);
        toVoting(newCompId);
        voting(newCompId);
        toWaitingAgreement(newCompId);
        agreement(br, newCompId);
        agreement(gr, newCompId);
        toMarriage(newCompId);
        marriageReport(newCompId);
    }

    int registerNewUser(User u, String role) throws InterruptedException {
        UCTests.register(u);
        UCTests.login(u);
        UCTests.redirectWait();
        UCTests.zoom(0.75);
        UCTests.fillUser(u, role);
        UCTests.zoom(1);
        UCTests.DRIVER.findElement(By.id("nextSettings")).click();
        UCTests.redirectWait();
        return UCTests.getLoggedUserId();
    }

    void pushArrow(User u) throws InterruptedException {
        UCTests.login(u);
        UCTests.DRIVER.findElement(By.id("pushArrowRooms")).click();
        UCTests.redirectWait();
    }

    void createCompTemplate() throws InterruptedException {
        UCTests.login(User.TEST_MATCHMAKER);
        UCTests.DRIVER.findElement(By.id("createCompetitionRooms")).click();
        var modal = UCTests.getActiveModal();
        modal.findElement(By.id("competitionCreationName")).sendKeys("BC TESTING");
        new Select(modal.findElement(By.id("competitionCreationCity"))).selectByIndex(1);
    }

    int assignCompetitors(int brId, int grId) throws InterruptedException {
        var task = new Faker().lorem().characters(14);
        UCTests.js(String.format("await window.assignDebug(['%s', '%s'], %d, %d)", task, task, brId, grId));
        UCTests.getActiveModal().findElement(By.id("acceptDialogBtn")).click();
        UCTests.redirectWait();
        var alert = UCTests.DRIVER.switchTo().alert();
        var alertText = alert.getText().split("=");
        var newCompId = Integer.parseInt(alertText[alertText.length - 1]);
        alert.accept();
        UCTests.logout();
        UCTests.redirectWait();
        return newCompId;
    }

    void report(User u, int compId) throws InterruptedException {
        UCTests.login(u);
        Thread.sleep(6000);
        clickOnActiveStep(compId);
        UCTests.js(String.format("await window.report('%s')", new Faker().lorem().characters(14, 32)));
        UCTests.getActiveModal().findElement(By.className("reportBtn")).click();
        UCTests.redirectWait();
        UCTests.logout();
    }

    void toVoting(int compId) throws InterruptedException {
        UCTests.login(User.TEST_MATCHMAKER);
        UCTests.changeCompStatus(compId, "ГОЛОСОВАНИЕ");
        UCTests.logout();
    }

    void voting(int compId) {
        Stream.of("assistant" , "enemy", "guest").forEach(it -> {
            try {
                registerNewUser(User.rand(), it);
                Thread.sleep(5000);
                clickOnActiveStep(compId);
                var modal = UCTests.getActiveModal();
                modal.findElement(By.className("voteCard")).click();
                modal.findElement(By.id("acceptDialogBtn")).click();
                UCTests.redirectWait();
                UCTests.DRIVER.switchTo().alert().accept();
                modal.findElement(By.className("btn-modal-close")).click();
                UCTests.logout();
            } catch (InterruptedException ignored) {}
        });
    }

    void toWaitingAgreement(int compId) throws InterruptedException {
        UCTests.login(User.TEST_MATCHMAKER);
        UCTests.changeCompStatus(compId, "ОЖИДАЕТ СОГЛАСИЙ");
        UCTests.logout();
    }

    void agreement(User u, int compId) throws InterruptedException {
        UCTests.login(u);
        Thread.sleep(3000);
        clickOnActiveStep(compId);
        var modal = UCTests.getActiveModal();
        modal.findElement(By.className("marriageAgreementCheck")).click();
        UCTests.DRIVER.switchTo().alert().accept();
        modal.findElement(By.className("btn-modal-close")).click();
        UCTests.logout();
    }

    void toMarriage(int compId) throws InterruptedException {
        UCTests.login(User.TEST_MATCHMAKER);
        UCTests.changeCompStatus(compId, "СВАДЬБА");
    }

    void marriageReport(int compId) throws InterruptedException {
        UCTests.findComp(compId)
                .findElement(By.cssSelector("li.step.animate-pulse"))
                .click();
        UCTests.js(String.format("window.debugReport('%s')", new Faker().lorem().characters(14)));
        UCTests.getActiveModal().findElement(By.className("marriageReportBtn")).click();
        UCTests.DRIVER.switchTo().alert().accept();
    }

    @AfterAll
    static void shutdown() {
        UCTests.shutdown();
    }
}
