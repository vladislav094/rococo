package guru.qa.rococo.page;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;

import javax.annotation.Nonnull;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    protected final Header header = new Header();

    public abstract T checkThatPageLoaded();

    @Nonnull
    public Header getHeader() {
        return header;
    }
}
