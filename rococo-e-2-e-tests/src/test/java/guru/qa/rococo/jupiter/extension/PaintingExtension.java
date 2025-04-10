package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.utils.ExtensionUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class PaintingExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(anno -> {
                            PaintingJson paintingFromAnno = ExtensionUtils.setPaintingFromAnno(anno);
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
