package guru.qa.rococo.service;

import guru.qa.rococo.model.ErrorJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

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
}