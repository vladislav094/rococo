package guru.qa.rococo.ex;

public class GeoNotFoundException extends RuntimeException {
    public GeoNotFoundException(String message) {
        super(message);
    }
}
