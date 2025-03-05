package guru.qa.rococo.ex;

public class NoSoapResponseException extends RuntimeException {
  public NoSoapResponseException(String message) {
    super(message);
  }
}
