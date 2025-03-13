package guru.qa.rococo.service.api;


//import guru.qa.rococo.grpc.RococoCurrencyServiceGrpc;
import org.springframework.stereotype.Component;

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
