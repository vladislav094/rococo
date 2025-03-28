package guru.qa.rococo.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean(name = "museumChannel")
    public ManagedChannel museumChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8071) // Адрес и порт для GrpcMuseumService
                .usePlaintext()
                .build();
    }

    @Bean(name = "countryChannel")
    public ManagedChannel countryChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8071) // Адрес и порт для GrpcCountryService
                .usePlaintext()
                .build();
    }

    @Bean(name = "artistChannel")
    public ManagedChannel artistChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8073) // Адрес и порт для GrpcArtistService
                .usePlaintext()
                .build();
    }
}
