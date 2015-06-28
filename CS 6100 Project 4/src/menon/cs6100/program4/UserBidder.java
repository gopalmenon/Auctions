package menon.cs6100.program4;

import java.util.Map;

public class UserBidder implements Bidder {
	
	private Bid bid;
	private int bidderNumber;

	public UserBidder(int bidderNumber, Bid bid) {
		this.bid = bid;
		this.bidderNumber = bidderNumber;
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
		return "You";
	}

}
