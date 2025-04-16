package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.service.MuseumClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.rococo.utils.JsonFromAnnoUtils.setMuseumFromAnno;

public class MuseumExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);

    private static final ThreadLocal<MuseumClient> museumClient = ThreadLocal.withInitial(MuseumDbClient::new);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class)
                .ifPresent(anno -> {
                            MuseumJson museumFromAnno = setMuseumFromAnno(museumClient, anno);
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
}
