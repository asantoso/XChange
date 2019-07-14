package org.knowm.xchange.okcoin.service;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.RestInvocationHandler;

public class OkCoinBaseService extends BaseExchangeService implements BaseService {

  /** Set to true if international site should be used */
  protected final boolean useIntl;

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
  public OkCoinBaseService(Exchange exchange) {

    super(exchange);

    useIntl =
        (Boolean)
            exchange.getExchangeSpecification().getExchangeSpecificParameters().get("Use_Intl");
  }

  protected String createDelimitedString(String[] items) {

    StringBuilder commaDelimitedString = null;
    if (items != null) {
      for (String item : items) {
        if (commaDelimitedString == null) {
          commaDelimitedString = new StringBuilder(item);
        } else {
          commaDelimitedString.append(",").append(item);
        }
      }
    }

    return (commaDelimitedString == null) ? null : commaDelimitedString.toString();
  }
}
