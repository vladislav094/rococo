package guru.qa.rococo.utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username() + RandomStringUtils.randomAlphabetic(5);
    }

    public static String randomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }

    public static String randomMuseumTitle() {

        return String.format(
                "%s %s %s %s",
                faker.educator().university().split(" ")[0],
                faker.educator().campus().split(" ")[0],
                "Museum",
                RandomStringUtils.randomAlphabetic(5)
        );
    }

    public static String randomDescription() {
        return faker.lorem().sentence(10);
    }

    public static String getRandomCountry() {
        Random random = new Random();
        int randomIndex = random.nextInt(randomCountries.size());
        return randomCountries.get(ThreadLocalRandom.current().nextInt(randomIndex));
    }

    public static String randomCity() {
        return faker.country().capital();
    }

    public static String randomPaintingTitle() {
        return String.format("%s %s", faker.book().title(), faker.book().publisher());
    }

    public static String randomArtistName() {
        return String.format("%s %s", faker.artist().name(), faker.book().author());
    }

    private static final List<String> randomCountries = List.of(
            "Belarus",
            "Russian Federation",
            "France",
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belgium",
            "Belize"
    );

}
