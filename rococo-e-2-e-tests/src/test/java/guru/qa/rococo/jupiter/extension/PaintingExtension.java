package guru.qa.rococo.jupiter.extension;

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
import guru.qa.rococo.utils.ImageUtils;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class PaintingExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);

    private static final ThreadLocal<PaintingClient> paintingClient = ThreadLocal.withInitial(PaintingDbClient::new);
    private static final ThreadLocal<ArtistClient> artistClient = ThreadLocal.withInitial(ArtistDbClient::new);
    private static final ThreadLocal<MuseumClient> museumClient = ThreadLocal.withInitial(MuseumDbClient::new);

    private static final String paintingPhotoPath = "img/ivan-terrible.jpg";
    private static final String artistPhotoPath = "img/picasso.jpeg";
    private static final String museumPhotoPath = "img/museum.jpeg";


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(anno -> {
                            PaintingJson paintingFromAnno = setPaintingFromAnno(anno);
                            context.getStore(NAMESPACE).put(context.getUniqueId(), paintingFromAnno);
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(PaintingJson.class);
    }

    @Override
    public PaintingJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), PaintingJson.class);
    }

    private PaintingJson setPaintingFromAnno(Painting paintingAnno) {
        if (paintingAnno.title().isEmpty() || paintingAnno.title().isBlank()) {

            PaintingJson paintingJson = new PaintingJson(
                    null,
                    RandomDataUtils.randomPaintingTitle(),
                    RandomDataUtils.randomDescription(),
                    setArtistFromAnno(paintingAnno.artist()),
                    setMuseumFromAnno(paintingAnno.museum()),
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

    private ArtistJson setArtistFromAnno(Artist artistAnno) {
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

    private MuseumJson setMuseumFromAnno(Museum museumAnno) {
        if (museumAnno.title().isEmpty() || museumAnno.title().isBlank()) {
            MuseumJson museumFromAnno = new MuseumJson(
                    null,
                    RandomDataUtils.randomMuseumTitle(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes(museumPhotoPath),
                    setGeoFromAnno(museumAnno)
            );
            return museumClient.get().createMuseum(museumFromAnno);
        } else {
            return museumClient.get().getMuseumByTitle(museumAnno.title());
        }
    }

    private GeoJson setGeoFromAnno(Museum anno) {
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
