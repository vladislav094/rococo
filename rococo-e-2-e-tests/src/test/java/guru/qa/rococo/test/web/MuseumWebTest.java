package guru.qa.rococo.test.web;

import com.github.javafaker.Faker;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@WebTest
public class MuseumWebTest extends BaseWebTest{

    private static final Faker faker = new Faker();

    @Museum
    @Test
    void testThat(MuseumJson museumJson)  {
        System.out.println(museumJson.id());
        System.out.println(museumJson.title());
    }

}
