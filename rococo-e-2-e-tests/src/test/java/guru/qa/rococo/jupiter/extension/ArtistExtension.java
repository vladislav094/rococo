package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.service.ArtistClient;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.utils.ImageUtils;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistExtension.class);

    private final ArtistClient artistClient = new ArtistDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Artist.class)
                .ifPresent(anno -> {
                    final String name = anno.name().isEmpty() || anno.name().isBlank() ? RandomDataUtils.randomArtistName() : anno.name();

                    ArtistJson artistFromAnno = new ArtistJson(
                            null,
                            name,
                            RandomDataUtils.randomDescription(),
                            ImageUtils.imageToStringBytes("img/picasso.jpeg")
                    );

                    ArtistJson createdArtist = artistClient.createArtist(artistFromAnno);

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdArtist);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(ArtistJson.class);
    }

    @Override
    public ArtistJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), ArtistJson.class);
    }
}
