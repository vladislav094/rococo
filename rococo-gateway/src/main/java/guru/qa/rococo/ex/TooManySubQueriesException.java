package guru.qa.rococo.ex;

public class TooManySubQueriesException extends RuntimeException {
  public TooManySubQueriesException(String message) {
    super(message);
  }
}
