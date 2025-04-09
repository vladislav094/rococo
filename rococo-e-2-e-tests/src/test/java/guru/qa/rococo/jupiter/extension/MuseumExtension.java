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

    private final MuseumClient museumClient = new MuseumDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class)
                .ifPresent(anno -> {
                            final String title = anno.title().isEmpty()
                                    || anno.title().isBlank()
                                    ? RandomDataUtils.randomMuseumTitle()
                                    : anno.title();

                            final String country = anno.country().isEmpty()
                                    || anno.country().isBlank()
                                    ? RandomDataUtils.randomCountry()
                                    : anno.country();

                            final String city = anno.city().isEmpty()
                                    || anno.city().isBlank()
                                    ? RandomDataUtils.randomCity()
                                    : anno.city();

                            MuseumJson museumFromAnno = new MuseumJson(
                                    null,
                                    title,
                                    RandomDataUtils.randomDescription(),
                                    ImageUtils.imageToStringBytes("img/museum.jpeg"),
                                    new GeoJson(
                                            null,
                                            city,
                                            new CountryJson(
                                                    null,
                                                    country
                                            )
                                    )
                            );

                            MuseumJson createdMuseum = museumClient.createMuseum(museumFromAnno);

                            context.getStore(NAMESPACE).put(context.getUniqueId(), createdMuseum);
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
}
