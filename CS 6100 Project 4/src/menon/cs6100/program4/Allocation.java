package menon.cs6100.program4;

public class Allocation {
	
	private int numberOfItems;
	private int unitPrice;
	
	public Allocation(int numberOfItems, int unitPrice) {
		this.numberOfItems = numberOfItems;
		this.unitPrice = unitPrice;
	}
	
	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
	
}
