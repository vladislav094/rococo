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
    private final String imagePath = "img/museum.jpeg";

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class)
                .ifPresent(anno -> {
                            final String title = anno.title().isEmpty() ? RandomDataUtils.randomMuseumName() : anno.title();
                            final String country = anno.country().isEmpty() ? RandomDataUtils.randomCountry() : anno.country();
                            final String city = anno.city().isEmpty() ? RandomDataUtils.randomCity() : anno.city();
                            // создаем MuseumJson из данных аннотации
                            MuseumJson museumFromAnno = new MuseumJson(
                                    null,
                                    title,
                                    RandomDataUtils.randomDescription(),
                                    ImageUtils.imageToStringBytes(imagePath),
                                    new GeoJson(
                                            null,
                                            city,
                                            new CountryJson(
                                                    null,
                                                    country
                                            )
                                    )
                            );
                            // создаем новую запись MuseumJson в БД
                            MuseumJson createdMuseum = museumClient.createMuseum(museumFromAnno);
                            // устанавливаем контекст
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
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), MuseumJson.class);
    }
}
