package menon.cs6100.program4;

public class BidderItems {
	
	private Bidder bidder;
	private int bidderItems;
	
	public BidderItems(Bidder bidder, int bidderItems) {
		this.bidder = bidder;
		this.bidderItems = bidderItems;
	}
	
	public Bidder getBidder() {
		return bidder;
	}
	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}
	public int getBidderItems() {
		return bidderItems;
	}
	public void setBidderItems(int bidderItems) {
		this.bidderItems = bidderItems;
	}

}
