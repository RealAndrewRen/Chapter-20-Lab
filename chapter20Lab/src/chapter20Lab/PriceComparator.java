package chapter20Lab;

public class PriceComparator implements java.util.Comparator<TradeOrder> {
	private boolean ascending;
	public PriceComparator() {
		ascending = true;
	}
	public PriceComparator(boolean asc) {
		ascending = asc;
	}
	public int compare(TradeOrder order1, TradeOrder order2) {
		if (order1.isMarket() && order2.isMarket()) {
			return 0;
		}
		if (order1.isMarket() && order2.isLimit()) {
			return -1;
		}
		if (order1.isLimit() && order2.isMarket()) {
			return 1;
		}
		else {
			if(ascending) {
				return (int) Math.round(100 * (order1.getPrice() - order2.getPrice()));
			}
			else {
				return (int) Math.round(100 * (order2.getPrice() - order1.getPrice()));
			}
		}
	}
}
