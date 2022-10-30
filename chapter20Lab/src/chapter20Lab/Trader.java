package chapter20Lab;

import java.util.ArrayList;

public class Trader implements Comparable<Trader>{
	private Brokerage brokerage;
	private String name;
	private String pswd;
	private ArrayList<String> mailbox;
	private TraderWindow myWindow;
	
	public Trader(Brokerage brokerage, String name, String pswd) {
		this.brokerage = brokerage;
		this.name = name;
		this.pswd = pswd;
		mailbox = new ArrayList<String>();
	}
	public int compareTo(Trader other) {
		return this.name.compareToIgnoreCase(other.name);
	}
	public boolean equals(Object other) {
		if (other instanceof Trader) {
			return this.name.equalsIgnoreCase(((Trader) other).name);
		}
		else {
			throw new ClassCastException();
		}
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return pswd;
	}
	public void getQuote(String symbol) {
		brokerage.getQuote(symbol, this);
	}
	public boolean hasMessages() {
		return mailbox.size() != 0;
	}
	@SuppressWarnings("deprecation")
	public void openWindow() {
		myWindow = new TraderWindow(this);
		for(String msg : mailbox) {
			myWindow.show(mailbox.remove(msg));
		}
	}
	public void placeOrder(TradeOrder order) {
		brokerage.placeOrder(order);
	}
	public void quit() {
		myWindow = null;
		brokerage.logout(this);
	}
	@SuppressWarnings("deprecation")
	public void receiveMessage(String msg) {
		mailbox.add(msg);
		if (myWindow != null)
		{
			for (String message : mailbox) {
				myWindow.show(mailbox.remove(message));
			}
		}
	}
}
