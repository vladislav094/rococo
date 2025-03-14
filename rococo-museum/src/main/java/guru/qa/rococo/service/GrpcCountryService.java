package guru.qa.rococo.service;

import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.grpc.CountriesResponse;
import guru.qa.rococo.grpc.PageableRequest;
import guru.qa.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.utils.GrpcResponseConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class GrpcCountryService extends RococoCountryServiceGrpc.RococoCountryServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCountryService.class);

    private final CountryRepository countryRepository;

    @Autowired
    public GrpcCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void getCountries(PageableRequest request, StreamObserver<CountriesResponse> responseObserver) {
        // данные с пагинацией
        Page<CountryEntity> countiesPage = countryRepository.findAll(
                PageRequest.of(request.getPage(), request.getSize())
        );
        // результат в gRPC ответ
        CountriesResponse response = CountriesResponse.newBuilder()
                .addAllCountry(countiesPage.getContent().stream()
                        .map(GrpcResponseConverter::toGrpcCountry)
                        .toList())
                .setTotalPages(countiesPage.getTotalPages())
                .setTotalElements(countiesPage.getTotalElements())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
