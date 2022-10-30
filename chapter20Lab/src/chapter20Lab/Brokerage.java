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
		final String welcome = "Welcome to SafeTrade!";
		Trader login = traders.get(name);
		if(login == null)
		{
			return -1;
		}
		if(!login.getPassword().equals(password))
		{
			return -2;
		}
		if(activeTraders.contains(login))
		{
			return -3;
		}
		activeTraders.add(login);
		
		if(!login.hasMessages()) 
		{
			login.receiveMessage(welcome);
		}		
		login.openWindow();
		return 0;
	}
	public void logout(Trader trader)
	{
		activeTraders.remove(trader);
	}
	public void placeOrder(TradeOrder order) {
		exchange.placeOrder(order);
	}
}
