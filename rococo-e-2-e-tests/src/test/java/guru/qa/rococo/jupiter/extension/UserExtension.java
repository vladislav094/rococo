package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.service.UserdataClieint;
import guru.qa.rococo.service.impl.UserdataDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";

    private static final ThreadLocal<UserdataClieint> userdataClient = ThreadLocal.withInitial(UserdataDbClient::new);


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if ("".equals(anno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = userdataClient.get().createUser(username, defaultPassword);
                        setUser(testUser);
                    } else {
                        UserJson existingUser = userdataClient.get().getUserByName(anno.username());
                        setUser(existingUser);
                    }
                });
    }

    public static void setUser(UserJson testUser) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                testUser
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getUdUserJson();
    }

    public static UserJson getUdUserJson() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }
}
