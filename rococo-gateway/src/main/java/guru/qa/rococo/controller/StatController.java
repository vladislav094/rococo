package guru.qa.rococo.controller;

import guru.qa.rococo.model.CurrencyValues;
import guru.qa.rococo.model.DataFilterValues;
import guru.qa.rococo.model.StatisticJson;
import guru.qa.rococo.service.StatisticAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stat")
public class StatController {

//  private final StatisticAggregator statisticAggregator;
//
//  @Autowired
//  public StatController(StatisticAggregator statisticAggregator) {
//    this.statisticAggregator = statisticAggregator;
//  }
//
//  @GetMapping("/total")
//  public List<StatisticJson> getTotalStatistic(@AuthenticationPrincipal Jwt principal,
//                                               @RequestParam(required = false) CurrencyValues statCurrency,
//                                               @RequestParam(required = false) CurrencyValues filterCurrency,
//                                               @RequestParam(required = false) DataFilterValues filterPeriod) {
//    String username = principal.getClaim("sub");
//    return statisticAggregator.enrichStatisticRequest(username, statCurrency, filterCurrency, filterPeriod);
//  }
}
