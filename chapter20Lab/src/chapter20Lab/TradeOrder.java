package chapter20Lab;

public class TradeOrder {
	private Trader trader;
	private String symbol;
	private boolean buyOrder;
	private boolean marketOrder;
	private int numShares;
	private double price;
	
	public TradeOrder(Trader trader, String symbol, boolean buyOrder, boolean marketOrder, int numShares, double price) {
		this.trader = trader;
		this.symbol = symbol;
		this.buyOrder = buyOrder;
		this.marketOrder = marketOrder;
		this.numShares = numShares;
		this.price = price;
	}
	public double getPrice() {
		return price;
	}
	public int getShares() {
		return numShares;
	}
	public String getSymbol() {
		return symbol;
	}
	public Trader getTrader() {
		return trader;
	}
	public boolean isBuy() {
		return buyOrder;
	}
	public boolean isLimit() {
		return !marketOrder;
	}
	public boolean isMarket() {
		return marketOrder;
	}
	public boolean isSell() {
		return !buyOrder;
	}
	public void subtractShares(int shares) {
		numShares -= shares;
	}
}
