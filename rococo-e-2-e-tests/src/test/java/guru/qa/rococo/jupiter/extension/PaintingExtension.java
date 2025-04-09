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

    private final PaintingClient paintingClient = new PaintingDbClient();
    private final ArtistClient artistClient = new ArtistDbClient();
    private final MuseumClient museumClient = new MuseumDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(anno -> {
                            final String title = anno.title().isEmpty()
                                    ? RandomDataUtils.randomPaintingTitle()
                                    : anno.title();

                            ArtistJson artistFromAnno = setArtistFromAnno(anno.artist());
                            MuseumJson museumFromAnno = setMuseumFromAnno(anno.museum());
                            PaintingJson paintingFromAnno = new PaintingJson(
                                    null,
                                    title,
                                    RandomDataUtils.randomDescription(),
                                    artistFromAnno,
                                    museumFromAnno,
                                    ImageUtils.imageToStringBytes("img/museum.jpeg")
                            );

                            PaintingJson createdPainting = paintingClient.createPainting(paintingFromAnno);

                            context.getStore(NAMESPACE).put(context.getUniqueId(), createdPainting);
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

    private ArtistJson setArtistFromAnno(Artist artistAnno) {
        if (artistAnno.name().isEmpty()) {
            ArtistJson artistFromAnno = new ArtistJson(
                    null,
                    RandomDataUtils.randomArtistName(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes("img/picasso.jpeg")
            );
            return artistClient.createArtist(artistFromAnno);
        } else {
            return artistClient.getArtistByName(artistAnno.name());
        }
    }

    private MuseumJson setMuseumFromAnno(Museum museumAnno) {
        if (museumAnno.title().isEmpty()) {
            final String country = museumAnno.country().isEmpty() ? RandomDataUtils.randomCountry() : museumAnno.country();
            final String city = museumAnno.city().isEmpty() ? RandomDataUtils.randomCity() : museumAnno.city();

            MuseumJson museumFromAnno = new MuseumJson(
                    null,
                    RandomDataUtils.randomMuseumTitle(),
                    RandomDataUtils.randomDescription(),
                    ImageUtils.imageToStringBytes("img/museum.jpeg"),
                    new GeoJson(null, city, new CountryJson(null, country))
            );
            return museumClient.createMuseum(museumFromAnno);
        } else {
            return museumClient.getMuseumByTitle(museumAnno.title());
        }
    }
}
