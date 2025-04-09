package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.service.PaintingClient;
import guru.qa.rococo.service.impl.PaintingDbClient;
import guru.qa.rococo.utils.ImageUtils;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class PaintingExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);

    private final PaintingClient paintingClient = new PaintingDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(anno -> {
                            final String title = anno.title().isEmpty()
                                    ? RandomDataUtils.randomPaintingTitle()
                                    : anno.title();

                            ArtistJson artistJson = context.getStore(UserExtension.NAMESPACE)
                                    .get(context.getUniqueId(), ArtistJson.class);

                            MuseumJson museumJson = context.getStore(UserExtension.NAMESPACE)
                                    .get(context.getUniqueId(), MuseumJson.class);

                            // создаем PaintingJson из данных аннотации
                            PaintingJson paintingFromAnno = new PaintingJson(
                                    null,
                                    title,
                                    RandomDataUtils.randomDescription(),
                                    artistJson,
                                    museumJson,
                                    ImageUtils.imageToStringBytes("img/museum.jpeg")
                            );
                            // создаем новую запись PaintingJson в БД
                            PaintingJson createdPainting = paintingClient.createPainting(paintingFromAnno);
                            // устанавливаем контекст
                            context.getStore(NAMESPACE).put(context.getUniqueId(), createdPainting);
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }
}
