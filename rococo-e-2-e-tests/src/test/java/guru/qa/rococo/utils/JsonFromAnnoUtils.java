package guru.qa.rococo.utils;

import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.rest.*;
import guru.qa.rococo.service.ArtistClient;
import guru.qa.rococo.service.MuseumClient;
import guru.qa.rococo.service.PaintingClient;

public class JsonFromAnnoUtils {

    private static final String paintingPhotoPath = "img/ivan-terrible.jpg";
    private static final String artistPhotoPath = "img/picasso.jpeg";
    private static final String museumPhotoPath = "img/museum.jpeg";


    public static PaintingJson setPaintingFromAnno(ThreadLocal<ArtistClient> artistClient,
                                                   ThreadLocal<PaintingClient> paintingClient,
                                                   ThreadLocal<MuseumClient> museumClient,
                                                   Painting paintingAnno) {
        if (paintingAnno.title().isEmpty() || paintingAnno.title().isBlank()) {

            PaintingJson paintingJson = new PaintingJson(
                    null,
                    RandomDataUtils.randomPaintingTitle(),
                    RandomDataUtils.randomDescription(),
                    setArtistFromAnno(artistClient, paintingAnno.artist()),
                    setMuseumFromAnno(museumClient, paintingAnno.museum()),
                    ImageUtils.imageToStringBytes(paintingPhotoPath)
            );

            PaintingJson createdPainting = paintingClient.get().createPainting(paintingJson);

            return new PaintingJson(
                    createdPainting.id(),
                    createdPainting.title(),
                    createdPainting.description(),
                    artistClient.get().getArtistById(createdPainting.artist().id().toString()),
                    museumClient.get().getMuseumById(createdPainting.museum().id().toString()),
                    createdPainting.content()
            );
        } else {
            return paintingClient.get().getPaintingByTitle(paintingAnno.title());
        }
    }

    public static ArtistJson setArtistFromAnno(ThreadLocal<ArtistClient> artistClient, Artist artistAnno) {
        if (artistAnno.name().isEmpty() || artistAnno.name().isBlank()) {
            ArtistJson artistFromAnno = new ArtistJson(
                    null,
                    RandomDataUtils.randomArtistName(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes(artistPhotoPath)
            );
            return artistClient.get().createArtist(artistFromAnno);
        } else {
            return artistClient.get().getArtistByName(artistAnno.name());
        }
    }

    public static MuseumJson setMuseumFromAnno(ThreadLocal<MuseumClient> museumClient, Museum museumAnno) {
        if (museumAnno.title().isEmpty() || museumAnno.title().isBlank()) {
            MuseumJson museumFromAnno = new MuseumJson(
                    null,
                    RandomDataUtils.randomMuseumTitle(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes(museumPhotoPath),
                    setGeoFromAnno(museumClient, museumAnno)
            );
            return museumClient.get().createMuseum(museumFromAnno);
        } else {
            return museumClient.get().getMuseumByTitle(museumAnno.title());
        }
    }

    public static GeoJson setGeoFromAnno(ThreadLocal<MuseumClient> museumClient, Museum anno) {
        if (anno.city().isEmpty() || anno.city().isBlank()) {
            final String country = anno.country().isEmpty() || anno.country().isBlank()
                    ? RandomDataUtils.getRandomCountry()
                    : anno.country();

            return new GeoJson(null, RandomDataUtils.randomCity(), new CountryJson(null, country));
        } else {
            return museumClient.get().getGeoByCity(anno.city());
        }
    }
}
