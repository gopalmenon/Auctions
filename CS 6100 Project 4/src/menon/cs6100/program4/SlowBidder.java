package menon.cs6100.program4;

import java.util.Map;
import java.util.Random;

/**
 * The slow bidder generates an integer value of the number of units based on the formula:
 * Number of Units = ((Maximum Units ^ ( 1 / Exponent )) / (Unit Price) ) ^ Exponent, where Exponent is between 0.5 and 1.0
 *
 */
public class SlowBidder implements Bidder {
	
	private Bid bid;
	private int bidderNumber;
	
	public SlowBidder(int bidderNumber, Random bidUnitsGenerator) {
		
		this.bidderNumber = bidderNumber;
		
		this.bid = new Bid(AuctionHouse.MAXIMUM_UNIT_BID_PRICE);
		
		//Adjust the maximum units to be bid
		int adjustedMaximumUnits = bidUnitsGenerator.nextInt(AuctionHouse.NUMBER_OF_ITEMS_FOR_SALE + 1);
		
		double exponent = bidUnitsGenerator.nextDouble();
		
		//Generate the number of units at each unit price
		int numberOfItemsBid = 0;
		for (int unitPrice = 1; unitPrice <= AuctionHouse.MAXIMUM_UNIT_BID_PRICE; ++unitPrice) {
			numberOfItemsBid = (int) Math.round(Math.pow(Math.pow(adjustedMaximumUnits, 1 / exponent) / unitPrice, exponent));
			this.bid.add(unitPrice, numberOfItemsBid);
		}
		
	}
	
	@Override
	public int hashCode() {
		return this.bidderNumber;
	}
	
	@Override
	public boolean equals(Object otherBidder) {
		
		if (otherBidder instanceof Bidder && this.bidderNumber == otherBidder.hashCode()) {
			return true;
		}
		
		return false;
	}

	@Override
	public Map<Integer, Integer> getBidVector() {
		return this.bid.getBidVector();
	}

	@Override
	public int getBidderNumber() {
		return this.bidderNumber;
	}

	@Override
	public String getBidderTypeLetter() {
		return "S";
	}

}
