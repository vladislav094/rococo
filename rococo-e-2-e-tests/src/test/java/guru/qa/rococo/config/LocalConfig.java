package guru.qa.rococo.config;

import javax.annotation.Nonnull;

public enum LocalConfig implements Config {
    INSTANCE;

    @Override
    public String ghUrl() {
        return "https://api.github.com/";
    }

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8090/";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String museumGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String userdataGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String artistGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String paintingGrpcAddress() {
        return "127.0.0.1";
    }

    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-auth";
    }

    @Override
    public String museumJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-museum";
    }

    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-userdata";
    }

    @Override
    public String artistJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-artist";
    }

    @Override
    public String paintingJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-painting";
    }

    @Nonnull
    @Override
    public String kafkaAddress() {
        return "127.0.0.1:9092";
    }
}
