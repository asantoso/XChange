package org.knowm.xchange.cexio.service;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.cexio.CexIOAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.RestInvocationHandler;
import si.mazi.rescu.RestProxyFactory;

/** @author timmolter */
public class CexIOBaseService extends BaseExchangeService implements BaseService {

  protected final CexIOAuthenticated cexIOAuthenticated;
  protected final CexIODigest signatureCreator;

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

  /**
   * Constructor
   *
   * @param exchange
   */
  public CexIOBaseService(Exchange exchange) {

    super(exchange);
    //
    //    cexIOAuthenticated =
    //        RestProxyFactory.createProxy(
    //            CexIOAuthenticated.class, exchange.getExchangeSpecification().getSslUri());
    //    signatureCreator =
    //        CexIODigest.createInstance(
    //            exchange.getExchangeSpecification().getSecretKey(),
    //            exchange.getExchangeSpecification().getUserName(),
    //            exchange.getExchangeSpecification().getApiKey(),
    //            exchange.getNonceFactory());

    String uri = exchange.getExchangeSpecification().getSslUri();
    this.apiHandler = new RestInvocationHandler(CexIOAuthenticated.class, uri, getClientConfig());
    this.cexIOAuthenticated =
        RestProxyFactory.createProxy(
            CexIOAuthenticated.class, uri, getClientConfig(), this.apiHandler);
    signatureCreator =
        CexIODigest.createInstance(
            exchange.getExchangeSpecification().getSecretKey(),
            exchange.getExchangeSpecification().getUserName(),
            exchange.getExchangeSpecification().getApiKey(),
            exchange.getNonceFactory());
  }
}
