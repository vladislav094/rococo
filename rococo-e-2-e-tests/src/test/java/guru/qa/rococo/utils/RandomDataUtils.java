package guru.qa.rococo.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }

    public static String randomMuseumTitle() {
        return String.format(
                "%s %s %s",
                faker.educator().university().split(" ")[0],
                faker.educator().campus(),
                "Museum"
        );
    }

    public static String randomDescription() {
        return faker.lorem().sentence(10);
    }

    public static String randomCountry() {
        return faker.country().name();
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
}
