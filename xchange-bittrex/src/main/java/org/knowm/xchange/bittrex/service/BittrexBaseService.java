package org.knowm.xchange.bittrex.service;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bittrex.BittrexAuthenticated;
import org.knowm.xchange.bittrex.BittrexV2;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocationHandler;
import si.mazi.rescu.RestProxyFactory;

public class BittrexBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final BittrexAuthenticated bittrexAuthenticated;
  protected final BittrexV2 bittrexV2;
  protected final ParamsDigest signatureCreator;

  RestInvocationHandler bittrexApiHandler;
  RestInvocationHandler bittrexV2ApiHandler;

  public RestInvocationHandler getInvokeHandler() {
    return this.bittrexApiHandler;
  }

  public RestInvocationHandler getInvokeHandlerV2() {
    return this.bittrexV2ApiHandler;
  }

  public void setHttpClient(HttpClient client, HttpClientOptions options) {
    getInvokeHandler().setHttpClient(client, options);
    getInvokeHandlerV2().setHttpClient(client, options);
  }

  public void setHttpClientOptions(HttpClientOptions options) {
    getInvokeHandler().setHttpClientOptions(options);
    getInvokeHandlerV2().setHttpClientOptions(options);
  }

  public HttpClientOptions getHttpClientOptions() {
    return getInvokeHandler().getHttpClientOptions();
  }

  public HttpClientOptions getHttpClientOptionsV2() {
    return getInvokeHandlerV2().getHttpClientOptions();
  }

  /**
   * Constructor
   *
   * @param exchange
   */
  public BittrexBaseService(Exchange exchange) {

    super(exchange);

    //    this.bittrexAuthenticated =
    //        RestProxyFactory.createProxy(
    //            BittrexAuthenticated.class,
    //            exchange.getExchangeSpecification().getSslUri(),
    //            getClientConfig());
    //    this.bittrexV2 =
    //        RestProxyFactory.createProxy(
    //            BittrexV2.class, exchange.getExchangeSpecification().getSslUri(),
    // getClientConfig());
    //    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    //    this.signatureCreator =
    //        BittrexDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());

    String uri = exchange.getExchangeSpecification().getSslUri();

    this.bittrexApiHandler =
        new RestInvocationHandler(BittrexAuthenticated.class, uri, getClientConfig());
    this.bittrexV2ApiHandler = new RestInvocationHandler(BittrexV2.class, uri, getClientConfig());

    this.bittrexAuthenticated =
        RestProxyFactory.createProxy(
            BittrexAuthenticated.class,
            exchange.getExchangeSpecification().getSslUri(),
            getClientConfig(),
            this.bittrexApiHandler);

    this.bittrexV2 =
        RestProxyFactory.createProxy(
            BittrexV2.class, uri, getClientConfig(), this.bittrexV2ApiHandler);

    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator =
        BittrexDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }
}
