package guru.qa.rococo.utils;

import com.google.protobuf.ByteString;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    private static final Base64.Encoder encoder = Base64.getEncoder();

    @SneakyThrows
    public static String imageToStringBytes(String imagePath) {

        BufferedImage bufferedImage = ImageIO.read(new ClassPathResource(imagePath).getInputStream());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", outputStream);
            return "data:image/jpg;base64," + encoder.encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    public static String convertPhotoFromBase64(ByteString photo) {
        if (photo == null || photo.isEmpty()) {
            return "";
        }
        return photo.toStringUtf8();
    }
}
