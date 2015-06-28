package menon.cs6100.program4;

import java.util.Map;
import java.util.Random;

/**
 * The quick bidder generates an integer value of the number of units based on the formula:
 * Number of Units = Maximum Units / (Unit Price ^ Exponent), where Exponent is between 1.0 and 2.0
 * The formula is derived from (Number of Units) * (Unit Price) ^ Exponent = Maximum Units
 * Depending on a boolean parameter, the above formula may use a random value instead of the Maximum Bid Price.
 * 
 */
public class QuickBidder implements Bidder {
	
	private Bid bid;
	private int bidderNumber;
	
	public QuickBidder(int bidderNumber, Random bidUnitsGenerator) {
		
		this.bidderNumber = bidderNumber;
		
		this.bid = new Bid(AuctionHouse.MAXIMUM_UNIT_BID_PRICE);
		
		//Adjust the maximum units to be bid
		int adjustedMaximumUnits = bidUnitsGenerator.nextInt(AuctionHouse.NUMBER_OF_ITEMS_FOR_SALE + 1);
		
		double exponent = bidUnitsGenerator.nextDouble() + 1.0;
		
		//Generate the number of units at each unit price
		int numberOfItemsBid = 0;
		for (int unitPrice = 1; unitPrice <= AuctionHouse.MAXIMUM_UNIT_BID_PRICE; ++unitPrice) {
			numberOfItemsBid = (int) Math.round(adjustedMaximumUnits / Math.pow(unitPrice, exponent));
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
		return "Q";
	}

}
