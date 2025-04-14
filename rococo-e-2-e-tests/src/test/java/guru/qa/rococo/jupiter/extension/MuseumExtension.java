package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.model.rest.CountryJson;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.service.MuseumClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.utils.ImageUtils;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class MuseumExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);

    private static final ThreadLocal<MuseumClient> museumClient = ThreadLocal.withInitial(MuseumDbClient::new);

    private static final String museumPhotoPath = "img/museum.jpeg";


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class)
                .ifPresent(anno -> {
                            MuseumJson museumFromAnno = setMuseumFromAnno(anno);
                            context.getStore(NAMESPACE).put(context.getUniqueId(), museumFromAnno);
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(MuseumJson.class);
    }

    @Override
    public MuseumJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), MuseumJson.class);
    }

    public MuseumJson setMuseumFromAnno(Museum museumAnno) {
        if (museumAnno.title().isEmpty() || museumAnno.title().isBlank()) {
            MuseumJson museumFromAnno = new MuseumJson(
                    null,
                    RandomDataUtils.randomMuseumTitle(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes(museumPhotoPath),
                    setGeoFromAnno(museumAnno)
            );
            return museumClient.get().createMuseum(museumFromAnno);
        } else {
            return museumClient.get().getMuseumByTitle(museumAnno.title());
        }
    }

    private GeoJson setGeoFromAnno(Museum anno) {
        if (anno.city().isEmpty() || anno.city().isBlank()) {
            final String country = anno.country().isEmpty() || anno.country().isBlank()
                    ? RandomDataUtils.getRandomCountry()
                    : anno.country();

            return new GeoJson(null, RandomDataUtils.randomCity(), new CountryJson(null, country));
        } else {
            return museumClient.get().getGeoByCity(anno.city());
        }
    }
}
