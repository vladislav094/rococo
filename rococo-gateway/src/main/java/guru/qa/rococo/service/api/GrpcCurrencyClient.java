package guru.qa.rococo.service.api;


import com.google.protobuf.Empty;
//import guru.qa.rococo.grpc.RococoCurrencyServiceGrpc;
import guru.qa.rococo.model.CurrencyJson;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class GrpcCurrencyClient {
//
//  private static final Logger LOG = LoggerFactory.getLogger(GrpcCurrencyClient.class);
//  private static final Empty EMPTY = Empty.getDefaultInstance();
//
//  @GrpcClient("grpcCurrencyClient")
//  private RococoCurrencyServiceGrpc.RococoCurrencyServiceBlockingStub rococoCurrencyServiceStub;
//
//  public @Nonnull
//  List<CurrencyJson> getAllCurrencies() {
//    try {
//      return rococoCurrencyServiceStub.getAllCurrencies(EMPTY).getAllCurrenciesList()
//          .stream()
//          .map(CurrencyJson::fromGrpcMessage)
//          .toList();
//    } catch (StatusRuntimeException e) {
//      LOG.error("### Error while calling gRPC server ", e);
//      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
//    }
//  }
}
