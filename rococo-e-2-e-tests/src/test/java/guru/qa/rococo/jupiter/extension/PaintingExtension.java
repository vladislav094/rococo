package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.service.ArtistClient;
import guru.qa.rococo.service.MuseumClient;
import guru.qa.rococo.service.PaintingClient;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.service.impl.PaintingDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.rococo.utils.JsonFromAnnoUtils.setPaintingFromAnno;

public class PaintingExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);

    private static final ThreadLocal<PaintingClient> paintingClient = ThreadLocal.withInitial(PaintingDbClient::new);
    private static final ThreadLocal<ArtistClient> artistClient = ThreadLocal.withInitial(ArtistDbClient::new);
    private static final ThreadLocal<MuseumClient> museumClient = ThreadLocal.withInitial(MuseumDbClient::new);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(anno -> {
                            PaintingJson paintingFromAnno = setPaintingFromAnno(
                                    artistClient, paintingClient, museumClient, anno
                            );
                            context.getStore(NAMESPACE).put(context.getUniqueId(), paintingFromAnno);
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(PaintingJson.class);
    }

    @Override
    public PaintingJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), PaintingJson.class);
    }
}
