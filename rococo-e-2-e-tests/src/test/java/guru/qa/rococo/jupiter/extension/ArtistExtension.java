package guru.qa.rococo.jupiter.extension;

import org.junit.jupiter.api.extension.*;

public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

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
