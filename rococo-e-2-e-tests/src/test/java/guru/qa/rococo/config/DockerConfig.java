package guru.qa.rococo.config;

import javax.annotation.Nonnull;

public enum DockerConfig implements Config{
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://frontend.rococo.dc/";
    }

    @Override
    public String authUrl() {
        return "http://auth.rococo.dc:9000/";
    }

    @Override
    public String gatewayUrl() {
        return "http://gateway.rococo.dc:8090/";
    }

    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-auth";
    }

    @Override
    public String museumJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-museum";
    }

    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-userdata";
    }

    @Override
    public String artistJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-artist";
    }

    @Override
    public String paintingJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-painting";
    }

    @Override
    public String userdataGrpcAddress() {
        return "userdata.rococo.dc";
    }

    @Override
    public String museumGrpcAddress() {
        return "museum.rococo.dc";
    }

    @Override
    public String artistGrpcAddress() {
        return "artist.rococo.dc";
    }

    @Override
    public String paintingGrpcAddress() {
        return "painting.rococo.dc";
    }

    @Nonnull
    @Override
    public String kafkaAddress() {
        return "kafka:9092";
    }

}
