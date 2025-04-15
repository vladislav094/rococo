package guru.qa.rococo.test.grpc;

import com.google.protobuf.ByteString;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.jupiter.annotation.Geo;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.utils.RandomDataUtils;
import io.grpc.Channel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.ImageUtils.imageToStringBytes;

@Order(2)
public class MuseumGrpcTest extends BaseGrpcTest {

    protected final Channel channelThreadLocal =
            getChannel(CFG.museumGrpcAddress(), CFG.museumGrpcPort());

    protected RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub getBlockingStub() {
        return RococoMuseumServiceGrpc.newBlockingStub(channelThreadLocal);
    }

    @Museum
    @Test
    public void museumShouldReturned(MuseumJson museumJson) {

        final ByIdRequest request = ByIdRequest.newBuilder()
                .setId(museumJson.id().toString())
                .build();

        final MuseumResponse response = getBlockingStub().getMuseumById(request);

        assertMuseumData(
                response,
                museumJson.id().toString(),
                museumJson.title(),
                museumJson.description(),
                museumJson.photo(),
                museumJson.geo()
        );
    }

    @Museum
    @Test
    public void countOfMuseumShouldBiggestThatZero() {

        final PageableRequest request = PageableRequest.newBuilder()
                .setPage(0)
                .setSize(10)
                .build();

        final MuseumsResponse response = getBlockingStub().getMuseums(request);
        Assertions.assertTrue(response.getMuseumCount() > 0);
    }

    @Museum
    @Geo
    @Test
    public void museumShouldEdited(MuseumJson museumJson, GeoJson geoJson) {

        final String randomTitle = RandomDataUtils.randomMuseumTitle();
        final String randomDescription = RandomDataUtils.randomDescription();
        final String museumPhoto = imageToStringBytes("img/museum.jpeg");

        UpdateMuseumRequest updateMuseumRequest = UpdateMuseumRequest.newBuilder()
                .setId(museumJson.id().toString())
                .setTitle(randomTitle)
                .setDescription(randomDescription)
                .setPhoto(ByteString.copyFromUtf8(museumPhoto))
                .setCity(geoJson.city())
                .setCountryId(geoJson.country().id().toString())
                .build();

        MuseumResponse updatedMuseum = getBlockingStub().updateMuseum(updateMuseumRequest);

        assertMuseumData(
                updatedMuseum,
                museumJson.id().toString(),
                randomTitle,
                randomDescription,
                museumPhoto,
                geoJson
        );
    }

}
