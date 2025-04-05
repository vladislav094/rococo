package guru.qa.rococo.data;

import guru.qa.rococo.clients.GrpcArtistClient;
import guru.qa.rococo.clients.GrpcMuseumClient;
import guru.qa.rococo.data.repository.PaintingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class PaintingDataInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(PaintingDataInitializer.class);

    private final PaintingRepository paintingRepository;
    private final GrpcMuseumClient grpcMuseumClient;
    private final GrpcArtistClient grpcArtistClient;

    @PostConstruct
    public void init() {
        updatePaintingReferences();
    }

    private void updatePaintingReferences() {
        // Получаем ID художников
        CompletableFuture<UUID> renoirArtistId = grpcArtistClient.getArtistIdByNameAsync("Ренуар");
        CompletableFuture<UUID> levitanArtistId = grpcArtistClient.getArtistIdByNameAsync("Левитан");
        CompletableFuture<UUID> shishkinArtistId = grpcArtistClient.getArtistIdByNameAsync("Шишкин");

        // Получаем ID музеев
        CompletableFuture<UUID> tretyakovMuseumId = grpcMuseumClient.getMuseumIdByTitleAsync("Третьяковская галерея");
        CompletableFuture<UUID> louvreMuseumId = grpcMuseumClient.getMuseumIdByTitleAsync("Лувр");
        CompletableFuture<UUID> minskMuseumId = grpcMuseumClient.getMuseumIdByTitleAsync("Национальный художественный музей Беларуси");

        // Комбинируем все futures и обновляем данные
        CompletableFuture.allOf(
                renoirArtistId, levitanArtistId, shishkinArtistId,
                tretyakovMuseumId, louvreMuseumId, minskMuseumId
        ).thenRunAsync(() -> {
            try {
                // Обновляем картины после получения всех ID
                updatePainting(
                        "Female nude",
                        renoirArtistId.get(),
                        tretyakovMuseumId.get()
                );

                updatePainting(
                        "Над вечным покоем",
                        levitanArtistId.get(),
                        louvreMuseumId.get()
                );

                updatePainting(
                        "Утро в сосновом лесу",
                        shishkinArtistId.get(),
                        minskMuseumId.get()
                );

            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Error updating painting references", e);
                Thread.currentThread().interrupt();
            }
        }).exceptionally(ex -> {
            LOG.error("Failed to fetch artist/museum IDs", ex);
            return null;
        });
    }

    private void updatePainting(String title, UUID artistId, UUID museumId) {
        paintingRepository.findByTitle(title).ifPresent(painting -> {
            painting.setArtistId(artistId);
            painting.setMuseumId(museumId);
            paintingRepository.save(painting);
            LOG.info("Updated painting {} with artistId={} and museumId={}",
                    title, artistId, museumId);
        });
    }
}