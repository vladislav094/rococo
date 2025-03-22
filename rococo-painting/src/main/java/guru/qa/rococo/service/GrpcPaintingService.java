package guru.qa.rococo.service;

import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class GrpcPaintingService extends RococoPaintingServiceGrpc.RococoPaintingServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcPaintingService.class);

    private final PaintingRepository paintingRepository;

    @Autowired
    public GrpcPaintingService(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaintings(PageableRequest request, StreamObserver<PaintingsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<PaintingEntity> museumPage = paintingRepository.findAll(pageable);
//        MuseumsResponse response = toGrpcMuseumsResponse(museumPage);
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
    }
}
