package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.Currency;
import jakarta.annotation.Nonnull;

public record CurrencyJson(
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("currencyRate")
    Double currencyRate) {


  public static @Nonnull CurrencyJson fromGrpcMessage(@Nonnull Currency currencyMessage) {
    return new CurrencyJson(
        CurrencyValues.valueOf(currencyMessage.getCurrency().name()),
        currencyMessage.getCurrencyRate()
    );
  }
}
