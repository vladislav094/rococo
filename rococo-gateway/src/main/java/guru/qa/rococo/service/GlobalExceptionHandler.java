package guru.qa.rococo.service;

import guru.qa.rococo.ex.NoRestResponseException;
import guru.qa.rococo.ex.NoSoapResponseException;
import guru.qa.rococo.model.ErrorJson;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ws.client.WebServiceIOException;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.application.name}")
    private String appName;


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorJson> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        LOG.warn("### Resolve ResponseStatusException in @RestControllerAdvice ", ex);
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorJson(
                        appName + ": " + ex.getStatusCode(),
                        ex.getStatusCode().toString(),
                        ex.getStatusCode().value(),
                        Objects.requireNonNull(ex.getReason()),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                ));
    }

//    @Override
//    protected @Nonnull ResponseEntity<Object> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException ex,
//                                                                           @Nonnull HttpHeaders headers,
//                                                                           @Nonnull HttpStatusCode status,
//                                                                           @Nonnull WebRequest request) {
//        return ResponseEntity
//                .status(status)
//                .body(new ErrorJson(
//                        appName + ": Entity validation error",
//                        HttpStatus.resolve(status.value()).getReasonPhrase(),
//                        status.value(),
//                        ex.getBindingResult()
//                                .getFieldErrors()
//                                .stream()
//                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                                .collect(Collectors.joining(", ")),
//                        ((ServletWebRequest) request).getRequest().getRequestURI()
//                ));
//    }
//
//    @ExceptionHandler({
//            HttpClientErrorException.NotAcceptable.class,
//            HttpClientErrorException.Conflict.class,
//            HttpClientErrorException.NotFound.class,
//            HttpClientErrorException.BadRequest.class,
//            HttpServerErrorException.InternalServerError.class,
//            HttpServerErrorException.NotImplemented.class,
//            HttpServerErrorException.ServiceUnavailable.class
//    })
//    public ResponseEntity<ErrorJson> handleRestTemplateExceptions(@Nonnull HttpClientErrorException ex,
//                                                                  @Nonnull HttpServletRequest request) {
//        LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
//        return handleForwardedException(ex, request);
//    }
//
//    @ExceptionHandler({NoSoapResponseException.class, NoRestResponseException.class})
//    public ResponseEntity<ErrorJson> handleApiNoResponseException(@Nonnull RuntimeException ex,
//                                                                  @Nonnull HttpServletRequest request) {
//        LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
//        return withStatus("Failed to collect data", HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request);
//    }
//
//    @ExceptionHandler(WebServiceIOException.class)
//    public ResponseEntity<ErrorJson> handleConnectException(@Nonnull RuntimeException ex,
//                                                            @Nonnull HttpServletRequest request) {
//        LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
//        return withStatus("SOAP connection refused", HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorJson> handleException(@Nonnull Exception ex,
//                                                     @Nonnull HttpServletRequest request) {
//        LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
//        return withStatus("Internal error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
//    }
//
//    private @Nonnull ResponseEntity<ErrorJson> withStatus(@Nonnull String type,
//                                                          @Nonnull HttpStatus status,
//                                                          @Nonnull String message,
//                                                          @Nonnull HttpServletRequest request) {
//        return ResponseEntity
//                .status(status)
//                .body(new ErrorJson(
//                        appName + ": " + type,
//                        status.getReasonPhrase(),
//                        status.value(),
//                        message,
//                        request.getRequestURI()
//                ));
//    }
//
//    @Nonnull
//    private ResponseEntity<ErrorJson> handleForwardedException(@Nonnull HttpClientErrorException ex, @Nonnull HttpServletRequest request) {
//        ErrorJson originalError = ex.getResponseBodyAs(ErrorJson.class);
//        return ResponseEntity
//                .status(originalError.status())
//                .body(new ErrorJson(
//                        originalError.type(),
//                        originalError.title(),
//                        originalError.status(),
//                        originalError.detail(),
//                        request.getRequestURI()
//                ));
//    }
}