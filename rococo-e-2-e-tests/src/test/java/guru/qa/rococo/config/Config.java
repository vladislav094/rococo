package guru.qa.rococo.config;

import javax.annotation.Nonnull;
import java.util.List;

public interface Config {

    static Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.INSTANCE
                : LocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    // jdbc url
    String authJdbcUrl();

    String museumJdbcUrl();

    String userdataJdbcUrl();

    String artistJdbcUrl();

    String paintingJdbcUrl();

    // grpc url
    String museumGrpcAddress();

    String userdataGrpcAddress();

    String artistGrpcAddress();

    String paintingGrpcAddress();

    // grpc port
    default int museumGrpcPort() {
        return 8071;
    }

    default int userdataGrpcPort() {
        return 8072;
    }

    default int artistGrpcPort() {
        return 8073;
    }

    default int paintingGrpcPort() {
        return 8074;
    }

    // kafka
    String kafkaAddress();

    default List<String> kafkaTopics() {
        return List.of("users");
    }


    @Nonnull
    default String ghUrl() {
        return "https://api.github.com/";
    }
}
