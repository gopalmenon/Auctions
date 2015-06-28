package menon.cs6100.program4;

import java.util.Map;
import java.util.Random;

public class RandomBidder implements Bidder {
	
	private Bid bid;
	private int bidderNumber;
	
	public RandomBidder(int bidderNumber, Random bidUnitsGenerator) {
		
		this.bidderNumber = bidderNumber;
		
		this.bid = new Bid(AuctionHouse.MAXIMUM_UNIT_BID_PRICE);
		
		//Generate the number of units at each unit price
		int numberOfItemsBid = bidUnitsGenerator.nextInt(AuctionHouse.NUMBER_OF_ITEMS_FOR_SALE + 1);
		this.bid.add(1, numberOfItemsBid);
		for (int unitPrice = 2; unitPrice <= AuctionHouse.MAXIMUM_UNIT_BID_PRICE; ++unitPrice) {
			numberOfItemsBid = bidUnitsGenerator.nextInt(numberOfItemsBid + 1);
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
		return "R";
	}

}
