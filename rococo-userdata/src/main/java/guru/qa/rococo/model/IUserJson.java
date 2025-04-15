package guru.qa.rococo.model;

import java.util.UUID;

public interface IUserJson {
    UUID id();

    String username();

    String firstname();

    String lastname();

    String avatar();
}
