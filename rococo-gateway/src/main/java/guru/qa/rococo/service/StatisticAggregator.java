package guru.qa.rococo.service;

import guru.qa.rococo.model.CurrencyValues;
import guru.qa.rococo.model.DataFilterValues;
import guru.qa.rococo.model.StatisticJson;
import guru.qa.rococo.model.StatisticV2Json;
import guru.qa.rococo.service.api.RestSpendClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticAggregator {

//  private final RestSpendClient restSpendClient;
//  private final UserDataClient userDataClient;
//
//  @Autowired
//  public StatisticAggregator(RestSpendClient restSpendClient,
//                             UserDataClient userDataClient) {
//    this.restSpendClient = restSpendClient;
//    this.userDataClient = userDataClient;
//  }
//
//  public @Nonnull
//  List<StatisticJson> enrichStatisticRequest(@Nonnull String username,
//                                             @Nullable CurrencyValues userCurrency,
//                                             @Nullable CurrencyValues filterCurrency,
//                                             @Nullable DataFilterValues filterPeriod) {
//    userCurrency = userCurrency == null
//        ? userDataClient.currentUser(username).currency()
//        : userCurrency;
//    return restSpendClient.statistic(username, userCurrency, filterCurrency, filterPeriod);
//  }
//
//  public @Nonnull
//  StatisticV2Json enrichStatisticRequestV2(@Nonnull String username,
//                                           @Nullable CurrencyValues statCurrency,
//                                           @Nullable CurrencyValues filterCurrency,
//                                           @Nullable DataFilterValues filterPeriod) {
//    statCurrency = statCurrency == null
//        ? userDataClient.currentUser(username).currency()
//        : statCurrency;
//    return restSpendClient.statisticV2(username, statCurrency, filterCurrency, filterPeriod);
//  }
}
