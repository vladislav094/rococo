package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.grpc.GetCurrentUserRequest;
import guru.qa.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.rococo.grpc.UserdataGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class GrpcUserdataService extends RococoUserdataServiceGrpc.RococoUserdataServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserdataService.class);

    private final UserRepository userRepository;

    @Autowired
    public GrpcUserdataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getCurrentUser(GetCurrentUserRequest request, StreamObserver<UserdataGrpc> responseObserver) {
        final String username = request.getUsername();
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " not found."));
        responseObserver.onNext(buildResponse(userEntity));
        responseObserver.onCompleted();
    }

    @Transactional
    public void updateUser(UserdataGrpc request, StreamObserver<UserdataGrpc> responseObserver) {
        final String username = request.getUsername();
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " not found."));
        userEntity.setFirstname(request.getFirstname());
        userEntity.setLastname(request.getLastname());
        userEntity.setAvatar(request.getAvatar().toByteArray());

        responseObserver.onNext(buildResponse(userRepository.save(userEntity)));
        responseObserver.onCompleted();
    }


    private static UserdataGrpc buildResponse(UserEntity entity) {
        return UserdataGrpc.newBuilder()
                .setId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname() != null ? entity.getFirstname() : "")
                .setLastname(entity.getLastname() != null ? entity.getLastname() : "")
                .setAvatar(entity.getAvatar() != null ?
                        ByteString.copyFrom(entity.getAvatar()) : ByteString.EMPTY)
                .build();
    }
}
