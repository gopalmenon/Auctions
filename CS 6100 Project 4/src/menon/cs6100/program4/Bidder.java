package menon.cs6100.program4;

import java.util.Map;

public interface Bidder {
	
	public Map<Integer, Integer> getBidVector();
	
	public int getBidderNumber();
	
	public String getBidderTypeLetter();

}
