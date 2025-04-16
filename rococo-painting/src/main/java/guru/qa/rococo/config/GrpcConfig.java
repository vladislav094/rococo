package guru.qa.rococo.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Value("${grpc.client.museumGrpcClient.address}")
    private String museumAddress;

    @Value("${grpc.client.artistGrpcClient.address}")
    private String artistAddress;

    @Bean(name = "museumChannel")
    public ManagedChannel museumChannel() {
        String[] parts = parseAddress(museumAddress);
        return ManagedChannelBuilder.forAddress(parts[0], Integer.parseInt(parts[1])) // Адрес и порт для GrpcMuseumService
                .usePlaintext()
                .build();
    }

    @Bean(name = "artistChannel")
    public ManagedChannel artistChannel() {
        String[] parts = parseAddress(artistAddress);
        return ManagedChannelBuilder.forAddress(parts[0], Integer.parseInt(parts[1])) // Адрес и порт для GrpcArtistService
                .usePlaintext()
                .build();
    }

    private String[] parseAddress(String address) {
        return address.replace("static://", "").split(":");
    }
}
