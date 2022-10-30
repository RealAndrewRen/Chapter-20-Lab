package chapter20Lab;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Stock {
	private PriorityQueue<TradeOrder> buyOrders;
	private String companyName;
	private double hiPrice;
	private double lastPrice;
	private double loPrice;
	public static DecimalFormat money;
	private PriorityQueue<TradeOrder> sellOrders;
	private String stockSymbol;
	private int volume;
	
	public Stock(String symbol, String name, double price) {
		companyName = name;
		stockSymbol = symbol;
		hiPrice = price;
		loPrice = price;
		lastPrice = price;
		volume = 0;
		buyOrders = new PriorityQueue<TradeOrder>(new PriceComparator(false));
		sellOrders = new PriorityQueue<TradeOrder>(new PriceComparator());
		
	}
	public String getQuote() {
		TradeOrder highBuy = null;
		Iterator<TradeOrder> iter = buyOrders.iterator();
		if (iter.hasNext()) {
			highBuy = iter.next();
		}
		while (iter.hasNext()) {
			TradeOrder order = iter.next();
			if(order.getPrice() > highBuy.getPrice()) {
				highBuy = order;
			}
		}
		TradeOrder lowSell = null;
		Iterator<TradeOrder> iter2 = sellOrders.iterator();
		if(iter2.hasNext()) {
			lowSell = iter2.next();
		}
		while(iter2.hasNext()) {
			TradeOrder order = iter2.next();
			if(order.getPrice() > lowSell.getPrice()) {
				highBuy = order;
			}
		}
		if (highBuy == null && lowSell == null) {
			return companyName + " " + stockSymbol + "\n" + "Price: " + lastPrice + " lo: " + loPrice + " hi: " + hiPrice + " vol: " + volume + "\n" + "Ask: none" + " Bid: none";
		}
		else if (highBuy == null) {
			return companyName + " " + stockSymbol + "\n" + "Price: " + lastPrice + " lo: " + loPrice + " hi: " + hiPrice + " vol: " + volume + "\n" + "Ask: " + lowSell.getPrice() + " size " + lowSell.getShares() + " Bid: none";
		}
		else if (lowSell == null) {
			return companyName + " " + stockSymbol + "\n" + "Price: " + lastPrice + " lo: " + loPrice + " hi: " + hiPrice + " vol: " + volume + "\n" + "Ask: none" + " Bid: " + highBuy.getPrice() + " size: " + highBuy.getShares();
		}
		else {
			return companyName + " " + stockSymbol + "\n" + "Price: " + lastPrice + " lo: " + loPrice + " hi: " + hiPrice + " vol: " + volume + "\n" + "Ask: " + lowSell.getPrice() + " size: " + lowSell.getShares() + " Bid: " + highBuy.getPrice() + " size: " + highBuy.getShares();
		}
	}
	public void executeOrders() {
		if(sellOrders.size() == 0 || buyOrders.size() == 0) return;
		Iterator<TradeOrder> buyIterator = buyOrders.iterator();
		boolean firstB = true, firstS, complete;
		double buyPrice, sellPrice;
		
		while(firstB || buyIterator.hasNext() && sellOrders.size() != 0)
		{
			TradeOrder buy = buyIterator.next();
			firstB = false;
			
			if(buy.isLimit()) buyPrice = buy.getPrice();
			else buyPrice = loPrice;
			complete = false;
			Iterator<TradeOrder> sellIterator = sellOrders.iterator();
			firstS = true;
			
			while(firstS || sellIterator.hasNext())
			{
				TradeOrder sell = sellIterator.next();
				firstS = false;
				
				if(sell.isLimit()) sellPrice = sell.getPrice();
				else sellPrice = hiPrice;
				if(buyPrice >= sellPrice)
				{
					int buyShares = buy.getShares();
					int sellShares = sell.getShares();
					if(sellShares > buyShares)
					{
						sell.subtractShares(buyShares);
						complete = true;
					}
					else if(sellShares == buyShares)
					{
						sellIterator.remove();
						complete = true;
					}
					else
					{
						buy.subtractShares(sellShares);
						sellIterator.remove();
						sell.getTrader().receiveMessage(getMessage(sell, sellShares, sellPrice));
						buy.getTrader().receiveMessage(getMessage(buy, sellShares, sellPrice));
					}
					if(complete)
					{
						sell.getTrader().receiveMessage(getMessage(sell, buyShares, sellPrice));
						buy.getTrader().receiveMessage(getMessage(buy, buyShares, sellPrice));
						buyIterator.remove();
					}
					if(sellPrice > hiPrice) hiPrice = sellPrice;
					if(sellPrice < loPrice) loPrice = sellPrice;
					lastPrice = sellPrice;
				}
				else break;
			}
		}
	}
	private String getMessage(TradeOrder order, int shares, double price)
	{
		String msg = "You ";
		if(order.isBuy()) msg += " bought ";
		else msg += " sold ";
		msg += shares + " " + stockSymbol + " at " + money.format(price) + " amt " + money.format((price * shares));
		return msg;
	}
	public void placeOrder(TradeOrder order) {
		String type = "";
		if (order.isBuy()) {
			type = "Buy";
			buyOrders.add(order);
		}
		else {
			type = "Sell";
			buyOrders.add(order);
		}
		String market = "";
		if (order.isMarket()) {
			market = "market";
		}
		else {
			market = "" + money.format(order.getPrice());
		}
		order.getTrader().receiveMessage("New order:	" + type + " " + order.getSymbol() + " (" + order.getTrader() + ")\n" + order.getShares() + " at " + market);
		executeOrders();
	}
}
