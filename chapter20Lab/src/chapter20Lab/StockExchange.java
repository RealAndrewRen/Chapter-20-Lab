package chapter20Lab;

import java.util.HashMap;

public class StockExchange {
	private HashMap<String, Stock> stockList;
	
	public StockExchange() {
		stockList = new HashMap<String, Stock>();
	}
	public String getQuote(String symbol) {
		Stock stock = stockList.get(symbol);
		if (stock == null) {
			return "Invalid symbol";
		}
		else {
			return stock.getQuote();
		}
	}
	public void listStock(String symbol, String name, double price) {
		stockList.put(symbol, new Stock(symbol, name, price));
	}
	public void placeOrder(TradeOrder order) {
		Stock stock = stockList.get(order.getSymbol());
		if (stock == null) {
			order.getTrader().receiveMessage("Invalid symbol");
		}
		else {
			stock.placeOrder(order);
		}
	}
}
