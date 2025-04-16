package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.service.ArtistClient;
import guru.qa.rococo.service.impl.ArtistDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.rococo.utils.JsonFromAnnoUtils.setArtistFromAnno;

public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistExtension.class);

    private static final ThreadLocal<ArtistClient> artistClient = ThreadLocal.withInitial(ArtistDbClient::new);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Artist.class)
                .ifPresent(anno -> {
                            ArtistJson artistFromAnno = setArtistFromAnno(artistClient, anno);
                            context.getStore(NAMESPACE).put(context.getUniqueId(), artistFromAnno);
                        }
                );
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
