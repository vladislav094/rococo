package guru.qa.rococo.utils;

import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.rest.*;
import guru.qa.rococo.service.ArtistClient;
import guru.qa.rococo.service.MuseumClient;
import guru.qa.rococo.service.PaintingClient;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.service.impl.PaintingDbClient;

public class ExtensionUtils {

    private static final PaintingClient paintingClient = new PaintingDbClient();
    private static final ArtistClient artistClient = new ArtistDbClient();
    private static final MuseumClient museumClient = new MuseumDbClient();

    private static final String paintingPhotoPath = "img/ivan-terrible.jpg";
    private static final String artistPhotoPath = "img/picasso.jpeg";
    private static final String museumPhotoPath = "img/museum.jpeg";


    public static PaintingJson setPaintingFromAnno(Painting paintingAnno) {
        if (paintingAnno.title().isEmpty() || paintingAnno.title().isBlank()) {
            PaintingJson paintingJson = new PaintingJson(
                    null,
                    RandomDataUtils.randomPaintingTitle(),
                    RandomDataUtils.randomDescription(),
                    setArtistFromAnno(paintingAnno.artist()),
                    setMuseumFromAnno(paintingAnno.museum()),
                    ImageUtils.imageToStringBytes(paintingPhotoPath)
            );
            return paintingClient.createPainting(paintingJson);
        } else {
            return paintingClient.getPaintingByTitle(paintingAnno.title());
        }
    }

    public static ArtistJson setArtistFromAnno(Artist artistAnno) {
        if (artistAnno.name().isEmpty() || artistAnno.name().isBlank()) {
            ArtistJson artistFromAnno = new ArtistJson(
                    null,
                    RandomDataUtils.randomArtistName(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes(artistPhotoPath)
            );
            return artistClient.createArtist(artistFromAnno);
        } else {
            return artistClient.getArtistByName(artistAnno.name());
        }
    }

    public static MuseumJson setMuseumFromAnno(Museum museumAnno) {
        if (museumAnno.title().isEmpty() || museumAnno.title().isBlank()) {
            MuseumJson museumFromAnno = new MuseumJson(
                    null,
                    RandomDataUtils.randomMuseumTitle(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes(museumPhotoPath),
                    setGeoFromAnno(museumAnno)
            );
            return museumClient.createMuseum(museumFromAnno);
        } else {
            return museumClient.getMuseumByTitle(museumAnno.title());
        }
    }

    public static GeoJson setGeoFromAnno(Museum anno) {
        if (anno.city().isEmpty() || anno.city().isBlank()) {
            final String country = anno.country().isEmpty()
                    || anno.country().isBlank()
                    ? RandomDataUtils.randomCountry()
                    : anno.country();

            return new GeoJson(
                    null, RandomDataUtils.randomCity(), new CountryJson(null, country)
            );
        } else {
            return museumClient.getGeoByCity(anno.city());
        }
    }
}
