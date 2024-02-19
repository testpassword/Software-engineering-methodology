import com.github.javafaker.Faker;
import java.util.Date;

public record User(String phone, String email, String pass, String name,
            String city, String about,
            Date dateOfBirth
) {

    // tests users should be presented in DB before test executing
    public static User TEST_MATCHMAKER =
            new User("1111111111", "m@m.com", "m", "", "", "", new Date());
    public static User TEST_GROOM =
            new User("2222222222", "g@g.com", "g", "", "", "", new Date());

    static User rand() {
        var f = new Faker();
        var about = f.lorem().characters(8);
        return new User(
                f.phoneNumber().cellPhone().replaceAll("[^0-9]", ""),
                f.internet().emailAddress(),
                "Selenium",
                f.name().fullName(),
                "Udomlya",
                about,
                new Faker().date().birthday(18, 55)
        );
    }
}
