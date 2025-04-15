package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Geo;
import guru.qa.rococo.model.rest.CountryJson;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.service.MuseumClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class GeoExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(GeoExtension.class);
    private static final ThreadLocal<MuseumClient> museumClient = ThreadLocal.withInitial(MuseumDbClient::new);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Geo.class)
                .ifPresent(anno -> {
                    GeoJson geoFromAnno = setGeoFromAnno(anno);
                    context.getStore(NAMESPACE).put(context.getUniqueId(), geoFromAnno);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(GeoJson.class);
    }

    @Override
    public GeoJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), GeoJson.class);
    }

    private GeoJson setGeoFromAnno(Geo anno) {
        if (anno.city().isEmpty() || anno.city().isBlank()) {
            final String country = anno.country().isEmpty() || anno.country().isBlank()
                    ? RandomDataUtils.getRandomCountry()
                    : anno.country();

            final GeoJson geoJson = new GeoJson(null, RandomDataUtils.randomCity(),
                    new CountryJson(null, country)
            );

            return museumClient.get().createGeo(geoJson);
        } else {
            return museumClient.get().getGeoByCity(anno.city());
        }
    }
}
