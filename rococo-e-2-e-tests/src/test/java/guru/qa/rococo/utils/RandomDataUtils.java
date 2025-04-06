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

    public static String randomMuseumName() {
        return String.format("%s %s", faker.educator().university().split(" ")[0], "Museum");
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
}
