package com.xeiam.xchange.itbit.v1.service.polling;

import java.io.IOException;
import java.util.Date;

import si.mazi.rescu.SynchronizedValueFactory;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.itbit.v1.dto.trade.ItBitOrder;
import com.xeiam.xchange.itbit.v1.dto.trade.ItBitPlaceOrderRequest;

public class ItBitTradeServiceRaw extends ItBitBasePollingService {

  /** Wallet ID used for transactions with this instance */
  private final String walletId;

  /**
   * Constructor
   *
   * @param exchangeSpecification
   *          The {@link ExchangeSpecification}
   */
  public ItBitTradeServiceRaw(ExchangeSpecification exchangeSpecification, SynchronizedValueFactory<Long> nonceFactory) {

    super(exchangeSpecification, nonceFactory);

    // wallet Id used for this instance.
    walletId = (String) exchangeSpecification.getExchangeSpecificParameters().get("walletId");
  }

  public ItBitOrder[] getItBitOpenOrders() throws IOException {

    ItBitOrder[] orders = itBit.getOrders(signatureCreator, new Date().getTime(), nonceFactory, "XBTUSD", "1", "1000", "open", walletId);

    return orders;
  }

  /**
   * Retrieves the set of orders with the given status.
   *
   * @param status
   * @return
   * @throws IOException
   */
  public ItBitOrder[] getItBitOrders(String status) throws IOException {

    ItBitOrder[] orders = itBit.getOrders(signatureCreator, new Date().getTime(), nonceFactory, "XBTUSD", "1", "1000", status, walletId);

    return orders;
  }

  public ItBitOrder getItBitOrder(String orderId) throws IOException {

    ItBitOrder order = itBit.getOrder(signatureCreator, new Date().getTime(), nonceFactory, walletId, orderId);

    return order;
  }

  public ItBitOrder placeItBitLimitOrder(LimitOrder limitOrder) throws IOException {

    String side = limitOrder.getType().equals(OrderType.BID) ? "buy" : "sell";

    ItBitOrder postOrder =
        itBit.postOrder(signatureCreator, new Date().getTime(), nonceFactory, walletId, new ItBitPlaceOrderRequest(side, "limit", limitOrder.getCurrencyPair().baseSymbol, limitOrder
            .getTradableAmount().toPlainString(), limitOrder.getLimitPrice().toPlainString(), limitOrder.getCurrencyPair().baseSymbol + limitOrder.getCurrencyPair().counterSymbol));

    return postOrder;
  }

  public void cancelItBitOrder(String orderId) throws IOException {

    itBit.cancelOrder(signatureCreator, new Date().getTime(), nonceFactory, walletId, orderId);
  }

  public ItBitOrder[] getItBitTradeHistory(String currency, String pageNum, String pageLen) throws IOException {

    ItBitOrder[] orders = itBit.getOrders(signatureCreator, new Date().getTime(), nonceFactory, currency, pageNum, pageLen, "filled", walletId);
    return orders;
  }
}
