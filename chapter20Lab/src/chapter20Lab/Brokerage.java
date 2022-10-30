package chapter20Lab;

import java.util.TreeMap;
import java.util.TreeSet;

public class Brokerage implements Login{
	private StockExchange exchange;
	private TreeMap<String, Trader> traders;
	private TreeSet<Trader> activeTraders;

	public Brokerage(StockExchange exchange) {
		this.exchange = exchange;
		traders = new TreeMap<String, Trader>();
		activeTraders = new TreeSet<Trader>();
	}
	
	public int addUser(String name, String password) {
		if (name.length() < 4 || name.length() > 10) {
			return -1;
		}
		else if (password.length() < 2 || password.length() > 10) {
			return -2;
		}
		else if(traders.get(name) == null) {
			traders.put(name, new Trader(this, name, password));
			return 0;
		}
		else {
			return -3;
		}
	}
	public void getQuote(String symbol, Trader trader) {
		trader.receiveMessage(exchange.getQuote(symbol));
	}
	public int login(String name, String password)
	{
		if (traders.get(name) == null) {
			return -1;
		}
		else if (traders.get(name).getPassword() != password) {
			return -2;
		}
		else if (activeTraders.contains(traders.get(name))) {
			return -3;
		}
		else {
			activeTraders.add(new Trader(this, name, password));
			if(!traders.get(name).hasMessages()) {
				traders.get(name).receiveMessage("Welcome to SafeTrade!");
			}
			traders.get(name).openWindow();
			return 0;
		}
	}
	public void logout(Trader trader)
	{
		activeTraders.remove(trader);
	}
	public void placeOrder(TradeOrder order) {
		exchange.placeOrder(order);
	}
}
