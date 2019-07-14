package org.knowm.xchange.hitbtc.v2.service;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.hitbtc.v2.HitbtcAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.ClientConfigUtil;
import si.mazi.rescu.RestInvocationHandler;
import si.mazi.rescu.RestProxyFactory;

public class HitbtcBaseService extends BaseExchangeService implements BaseService {

  protected final HitbtcAuthenticated hitbtc;

  RestInvocationHandler apiHandler;

  public RestInvocationHandler getInvokeHandler() {
    return this.apiHandler;
  }

  public void setHttpClient(HttpClient client, HttpClientOptions options) {
    getInvokeHandler().setHttpClient(client, options);
  }

  public void setHttpClientOptions(HttpClientOptions options) {
    getInvokeHandler().setHttpClientOptions(options);
  }

  public HttpClientOptions getHttpClientOptions() {
    return getInvokeHandler().getHttpClientOptions();
  }

  protected HitbtcBaseService(Exchange exchange) {

    super(exchange);

    //    String apiKey = exchange.getExchangeSpecification().getApiKey();
    //    String secretKey = exchange.getExchangeSpecification().getSecretKey();
    //
    //    ClientConfig config = getClientConfig();
    //    ClientConfigUtil.addBasicAuthCredentials(config, apiKey, secretKey);
    //    hitbtc =
    //        RestProxyFactory.createProxy(
    //            HitbtcAuthenticated.class, exchange.getExchangeSpecification().getSslUri(),
    //    sconfig);

    String apiKey = exchange.getExchangeSpecification().getApiKey();
    String secretKey = exchange.getExchangeSpecification().getSecretKey();
    ClientConfig config = getClientConfig();
    ClientConfigUtil.addBasicAuthCredentials(config, apiKey, secretKey);
    String uri = exchange.getExchangeSpecification().getSslUri();
    this.apiHandler = new RestInvocationHandler(HitbtcAuthenticated.class, uri, config);
    this.hitbtc =
        RestProxyFactory.createProxy(HitbtcAuthenticated.class, uri, config, this.apiHandler);
  }
}
