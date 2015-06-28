package menon.cs6100.program4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will hold the bid vector that is submitted by a bidder
 *
 */
public class Bid {
	
	private Map<Integer, Integer> bidVector;
	int maximumBidPrice;
	
	public Bid(int maximumBidPrice) {
		this.bidVector = new HashMap<Integer, Integer>();
		this.maximumBidPrice = maximumBidPrice;
		for (int bidPriceCounter = 0; bidPriceCounter < this.maximumBidPrice; ++ bidPriceCounter) {
			this.bidVector.put(Integer.valueOf(bidPriceCounter + 1), Integer.valueOf(1));
		}
	}
	
	/**
	 * @return a ready only copy of the bid vector
	 */
	public Map<Integer, Integer> getBidVector() {
		return Collections.unmodifiableMap(this.bidVector);
	}

	/**
	 * @param unitPrice
	 * @param numberOfItems
	 */
	public void add(int unitPrice, int numberOfItems) {
		
		this.bidVector.put(Integer.valueOf(unitPrice), Integer.valueOf(numberOfItems));
		
	}
}
