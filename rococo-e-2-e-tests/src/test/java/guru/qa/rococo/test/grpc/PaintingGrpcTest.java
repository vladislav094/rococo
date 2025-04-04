package guru.qa.rococo.test.grpc;

import com.google.protobuf.ByteString;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.utils.RandomDataUtils;
import io.grpc.Channel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.ImageUtils.imageToStringBytes;

@Order(3)
public class PaintingGrpcTest extends BaseGrpcTest {

    protected final Channel channelThreadLocal =
            getChannel(CFG.paintingGrpcAddress(), CFG.paintingGrpcPort());

    protected RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub getBlockingStub() {
        return RococoPaintingServiceGrpc.newBlockingStub(channelThreadLocal);
    }

    @Painting
    @Test
    public void paintingShouldReturned(PaintingJson paintingJson) {

        final ByIdRequest request = ByIdRequest.newBuilder()
                .setId(paintingJson.id().toString())
                .build();

        final PaintingResponse response = getBlockingStub().getPaintingById(request);

        assertPaintingData(
                response,
                paintingJson.id().toString(),
                paintingJson.title(),
                paintingJson.description(),
                paintingJson.artist(),
                paintingJson.museum(),
                paintingJson.content()
        );
    }

    @Painting
    @Test
    public void countOfPaintingShouldBiggestThatZero() {

        final PageableRequest request = PageableRequest.newBuilder()
                .setPage(0)
                .setSize(10)
                .build();

        final PaintingsResponse response = getBlockingStub().getPaintings(request);
        Assertions.assertTrue(response.getPaintingCount() > 0);
    }

    @Painting
    @Artist
    @Museum
    @Test
    public void paintingShouldEdited(PaintingJson paintingJson, ArtistJson artistJson, MuseumJson museumJson) {

        final String randomTitle = RandomDataUtils.randomMuseumTitle();
        final String randomDescription = RandomDataUtils.randomDescription();
        final String museumPhoto = imageToStringBytes("img/ivan-terrible.jpg");

        UpdatePaintingRequest updatePaintingRequest = UpdatePaintingRequest.newBuilder()
                .setId(paintingJson.id().toString())
                .setTitle(randomTitle)
                .setDescription(randomDescription)
                .setArtistId(artistJson.id().toString())
                .setMuseumId(museumJson.id().toString())
                .setContent(ByteString.copyFromUtf8(museumPhoto))
                .build();

        PaintingResponse updatedPaintingResponse = getBlockingStub().updatePainting(updatePaintingRequest);

        assertPaintingData(
                updatedPaintingResponse,
                paintingJson.id().toString(),
                randomTitle,
                randomDescription,
                artistJson,
                museumJson,
                museumPhoto
        );
    }

    private void assertPaintingData(PaintingResponse paintingResponse,
                                    String expectedId,
                                    String expectedTitle,
                                    String expectedDescription,
                                    ArtistJson expectedArtist,
                                    MuseumJson expectedMuseum,
                                    String expectedContent) {

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(paintingResponse.getId()).isEqualTo(expectedId);
        softly.assertThat(paintingResponse.getTitle()).isEqualTo(expectedTitle);
        softly.assertThat(paintingResponse.getDescription()).isEqualTo(expectedDescription);
        softly.assertThat(paintingResponse.getContent().toStringUtf8()).isEqualTo(expectedContent);

        assertArtistData(
                paintingResponse.getArtist(),
                expectedArtist.id().toString(),
                expectedArtist.name(),
                expectedArtist.biography(),
                expectedArtist.photo()

        );

        assertMuseumData(
                paintingResponse.getMuseum(),
                expectedMuseum.id().toString(),
                expectedMuseum.title(),
                expectedMuseum.description(),
                expectedMuseum.photo(),
                expectedMuseum.geo()
        );

        softly.assertAll();
    }
}
