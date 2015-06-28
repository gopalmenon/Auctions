package menon.cs6100.program4;

import java.util.Comparator;

public class BidderItemsComparator implements Comparator<BidderItems> {

	private static final int BEFORE = -1;
	private static final int EQUAL = 0;
	private static final int AFTER = 1;
	
	@Override
	public int compare(BidderItems bidderItems1, BidderItems bidderItems2) {
		if (bidderItems1 == null || bidderItems2 == null) {
			throw new NullPointerException();
		} else if (bidderItems1.getBidderItems() < bidderItems2.getBidderItems()) {
			return AFTER;
		} else if (bidderItems1.getBidderItems() > bidderItems2.getBidderItems()) {
			return BEFORE;
		} else {
			return EQUAL;
		}
	}

}
