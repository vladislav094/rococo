package guru.qa.rococo.test.grpc;

import com.google.protobuf.ByteString;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.utils.RandomDataUtils;
import io.grpc.Channel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.ImageUtils.imageToStringBytes;

@Order(1)
public class ArtistGrpcTest extends BaseGrpcTest {

    protected final Channel channelThreadLocal =
            getChannel(CFG.artistGrpcAddress(), CFG.artistGrpcPort());

    protected RococoArtistServiceGrpc.RococoArtistServiceBlockingStub getBlockingStub() {
        return RococoArtistServiceGrpc.newBlockingStub(channelThreadLocal);
    }

    @Artist
    @Test
    public void artistShouldReturned(ArtistJson artistJson) {

        final ByIdRequest request = ByIdRequest.newBuilder()
                .setId(artistJson.id().toString())
                .build();

        final ArtistResponse response = getBlockingStub().getArtistById(request);

        assertArtistData(
                response,
                artistJson.id().toString(),
                artistJson.name(),
                artistJson.biography(),
                artistJson.photo()
        );

    }

    @Artist
    @Test
    public void countOfArtistShouldBiggestThatZero() {

        final PageableRequest request = PageableRequest.newBuilder()
                .setPage(0)
                .setSize(10)
                .build();

        final ArtistsResponse response = getBlockingStub().getArtists(request);
        Assertions.assertTrue(response.getArtistCount() > 0);
    }

    @Artist
    @Test
    public void artistShouldEdited(ArtistJson artistJson) {

        final String artistPhotoPath = "img/picasso.jpeg";
        final String randomName = RandomDataUtils.randomArtistName();
        final String randomBiography = RandomDataUtils.randomDescription();
        final String artistPhoto = imageToStringBytes(artistPhotoPath);

        UpdateArtistRequest updateArtistRequest = UpdateArtistRequest.newBuilder()
                .setId(artistJson.id().toString())
                .setName(randomName)
                .setBiography(randomBiography)
                .setPhoto(ByteString.copyFromUtf8(artistPhoto))
                .build();

        ArtistResponse updatedArtist = getBlockingStub().updateArtist(updateArtistRequest);

        assertArtistData(
                updatedArtist,
                artistJson.id().toString(),
                randomName,
                randomBiography,
                artistPhoto
        );
    }
}
