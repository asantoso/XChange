package org.knowm.xchange.coinbasepro.service;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinbasepro.CoinbasePro;
import org.knowm.xchange.coinbasepro.dto.CoinbaseProException;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.FundsExceededException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.*;

public class CoinbaseProBaseService extends BaseExchangeService implements BaseService {

  protected final CoinbasePro coinbasePro;
  protected final ParamsDigest digest;

  protected final String apiKey;
  protected final String passphrase;

  RestInvocationHandler apiHandler;

  protected CoinbaseProBaseService(Exchange exchange) {

    super(exchange);
    //    coinbasePro =
    //        RestProxyFactory.createProxy(
    //            CoinbasePro.class, exchange.getExchangeSpecification().getSslUri(),

    // getClientConfig());

    //    digest =
    // CoinbaseProDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    //    apiKey = exchange.getExchangeSpecification().getApiKey();
    //    passphrase = (String)
    // exchange.getExchangeSpecification().getExchangeSpecificParametersItem("passphrase");

    // (Class<I> restInterface, String baseUrl, ClientConfig config, RestInvocationHandler handler,
    // Interceptor... interceptors)

    this.apiHandler =
        new RestInvocationHandler(
            CoinbasePro.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());

    this.coinbasePro =
        RestProxyFactory.createProxy(
            CoinbasePro.class,
            exchange.getExchangeSpecification().getSslUri(),
            getClientConfig(),
            this.apiHandler);

    this.digest =
        CoinbaseProDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.passphrase =
        (String)
            exchange.getExchangeSpecification().getExchangeSpecificParametersItem("passphrase");
  }

  public RestInvocationHandler getInvokeHandler() {
    return this.apiHandler;
  }

  public void setHttpClient(HttpClient client, HttpClientOptions options) {
    getInvokeHandler().setHttpClient(client, options);
  }

  public void setHttpClientOptions(HttpClientOptions options) {
    getInvokeHandler().setHttpClientOptions(options);
  }

  protected ExchangeException handleError(CoinbaseProException exception) {

    if (exception.getMessage().contains("Insufficient")) {
      return new FundsExceededException(exception);
    } else if (exception.getMessage().contains("Rate limit exceeded")) {
      return new RateLimitExceededException(exception);
    } else if (exception.getMessage().contains("Internal server error")) {
      return new InternalServerException(exception);
    } else {
      return new ExchangeException(exception);
    }
  }
}
