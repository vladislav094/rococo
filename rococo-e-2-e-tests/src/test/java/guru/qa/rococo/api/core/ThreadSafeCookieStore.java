package guru.qa.rococo.api.core;

import java.net.*;
import java.util.List;

public enum ThreadSafeCookieStore implements CookieStore {
    INSTANCE;

    private final ThreadLocal<CookieStore> threadSafeCookieStore = ThreadLocal.withInitial(
            ThreadSafeCookieStore::inMemoryCookieStore
    );

    private static CookieStore inMemoryCookieStore() {
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return manager.getCookieStore();
    }

    @Override
    public void add(URI uri, HttpCookie httpCookie) {
        getStore().add(uri, httpCookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getStore().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return getStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return getStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        return getStore().remove(uri, httpCookie);
    }

    @Override
    public boolean removeAll() {
        return getStore().removeAll();
    }

    private CookieStore getStore() {
        return threadSafeCookieStore.get();
    }

    public String cookieValue(String cookieName) {
        List<HttpCookie> cookies = getCookies();
        if (cookies.isEmpty()) {
            throw new IllegalStateException("No cookies available. Make sure authorize() was called first");
        }
        return cookies.stream()
                .filter(c -> c.getName().equals(cookieName))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                                "Cookie '" + cookieName + "' not found. Available cookies: "
                                        + cookies.stream().map(HttpCookie::getName).toList()
                        )
                );
    }
}
