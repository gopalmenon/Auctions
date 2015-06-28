package menon.cs6100.program4;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class AuctionHouse extends JFrame {
	
	public static final int NUMBER_OF_ITEMS_FOR_SALE = 30;
	public static final int MAXIMUM_UNIT_BID_PRICE = 10;
	private static final int MINIMUM_NUMBER_OF_OTHER_BIDDERS = 1;
	private static final int MAXIMUM_NUMBER_OF_OTHER_BIDDERS = 50;
	private static final int DEFAULT_NUMBER_OF_OTHER_BIDDERS = 25;
	private static final String SETUP_SPACER = "                                                                                                ";
	private static final String BID_SPACER = "                        ";
	private static final String USER_BID_ERROR_HEADER = "Bid Error";
	private static final String USER_BID_NOT_FOUND = "You need to put in a bid first";
	private static final String UNITS_CANNOT_INCREASE_WITH_UNIT_PRICE = "Number of items cannot increase with unit price";
	
	private int numberOfOtherBidders;
	private OtherBiddersMix otherBiddersMix;
	private Bid userBid;
	private int originalClearingPrice;
	
    public AuctionHouse(String name) {
        super(name);
        setResizable(true);
    	this.numberOfOtherBidders = DEFAULT_NUMBER_OF_OTHER_BIDDERS;
    	this.otherBiddersMix = OtherBiddersMix.AllRandom;
    	this.originalClearingPrice = 0;
    }
    
    /**
     * Allow the user to specify the number of other bidders and the mix of bidder types
     */
    private void showBiddingSetup() {
    	
    	
    	this.userBid = null;

    	//Create the panel that will contain the user input for number of other bidders and the bidder mix
    	Container contentPane = this.getContentPane();
    	contentPane.setLayout(new BorderLayout());
    	
    	//Panel for number of bidders
    	JPanel otherBidderCountPanel = new JPanel();
    	otherBidderCountPanel.setLayout(new BorderLayout());
    	otherBidderCountPanel.add(new JLabel(" Number of other bidders "), BorderLayout.WEST);
    	SpinnerNumberModel numberOfOtherBiddersNumberModel = new SpinnerNumberModel(DEFAULT_NUMBER_OF_OTHER_BIDDERS, MINIMUM_NUMBER_OF_OTHER_BIDDERS, MAXIMUM_NUMBER_OF_OTHER_BIDDERS, 1);
    	JSpinner numberOfOtherBiddersSpinner = new JSpinner(numberOfOtherBiddersNumberModel);
    	numberOfOtherBiddersSpinner.addChangeListener(this.new NumberOfOtherBiddersSpinnerChangeListener(numberOfOtherBiddersSpinner));
    	numberOfOtherBiddersSpinner.setToolTipText("Select a value from " + MINIMUM_NUMBER_OF_OTHER_BIDDERS + " to " + MAXIMUM_NUMBER_OF_OTHER_BIDDERS);
    	otherBidderCountPanel.add(numberOfOtherBiddersSpinner, BorderLayout.CENTER);
    	otherBidderCountPanel.add(new JLabel(SETUP_SPACER), BorderLayout.EAST);
    	JLabel otherBiddersHeader = new JLabel("  Other Bidders");
    	Font otherBiddersHeaderFont = otherBiddersHeader.getFont();
    	Font boldHeaderFont = new Font(otherBiddersHeaderFont.getFontName(), Font.BOLD, otherBiddersHeaderFont.getSize());
    	otherBiddersHeader.setFont(boldHeaderFont);
    	otherBidderCountPanel.add(otherBiddersHeader, BorderLayout.SOUTH);
    	this.numberOfOtherBidders = DEFAULT_NUMBER_OF_OTHER_BIDDERS;
    	this.otherBiddersMix = OtherBiddersMix.AllRandom;
    	
    	//Panel for bidder type radio buttons
    	JPanel bidderTypeRadioButtonsPanel = new JPanel();
    	bidderTypeRadioButtonsPanel.setLayout(new GridLayout(4, 1));
    	ButtonGroup bidderTypeRadioButtonsGroup = new ButtonGroup();
    	//Original versus All Random
    	JRadioButton allRandom = new JRadioButton();
    	allRandom.addActionListener(this.new BidderMixSelectionActionListener(OtherBiddersMix.AllRandom));
    	bidderTypeRadioButtonsGroup.add(allRandom);
    	bidderTypeRadioButtonsPanel.add(allRandom);
    	allRandom.setSelected(true);
    	allRandom.setToolTipText("Bidding will be between you and random bidders.");
    	//Original versus all quick
    	JRadioButton allQuick = new JRadioButton();
    	allQuick.addActionListener(this.new BidderMixSelectionActionListener(OtherBiddersMix.AllQuick));
    	bidderTypeRadioButtonsGroup.add(allQuick);
    	bidderTypeRadioButtonsPanel.add(allQuick);
    	allQuick.setToolTipText("Bidding will be between you and quick bidders.");
    	//Original versus all slow
    	JRadioButton allSlow = new JRadioButton();
    	allSlow.addActionListener(this.new BidderMixSelectionActionListener(OtherBiddersMix.AllSlow));
    	bidderTypeRadioButtonsGroup.add(allSlow);
    	bidderTypeRadioButtonsPanel.add(allSlow);
    	allSlow.setToolTipText("Bidding will be between you and slow bidders.");
    	//Original versus equal mix
    	JRadioButton equalMix = new JRadioButton();
    	equalMix.addActionListener(this.new BidderMixSelectionActionListener(OtherBiddersMix.EqualMix));
    	bidderTypeRadioButtonsGroup.add(equalMix);
    	bidderTypeRadioButtonsPanel.add(equalMix);
    	equalMix.setToolTipText("Bidding will be between you and equally distributed random, quick and slow bidders.");
    	
    	//Panel for bidder type labels
    	JPanel bidderTypeLabelsPanel = new JPanel();
    	bidderTypeLabelsPanel.setLayout(new GridLayout(4, 1));
    	bidderTypeLabelsPanel.add(new JLabel("All Random"));
    	bidderTypeLabelsPanel.add(new JLabel("All Quick"));
    	bidderTypeLabelsPanel.add(new JLabel("All Slow"));
    	bidderTypeLabelsPanel.add(new JLabel("Equal Mix"));

    	contentPane.removeAll();
    	contentPane.add(otherBidderCountPanel, BorderLayout.NORTH);
    	contentPane.add(bidderTypeRadioButtonsPanel, BorderLayout.WEST);
    	contentPane.add(bidderTypeLabelsPanel, BorderLayout.CENTER);
    	this.pack();
    	this.setVisible(true);
    }
    
    private void showUserBidInput() {
    	
    	this.userBid = new Bid(MAXIMUM_UNIT_BID_PRICE);
    	
    	//Create the panel that will contain the user bid
    	Container contentPane = this.getContentPane();
    	JPanel userBids = new JPanel();
    	userBids.setLayout(new GridLayout(MAXIMUM_UNIT_BID_PRICE + 3, 3));
    	
    	//Show the user selections for number of other bidders and the bidder mix
    	userBids.add(new JLabel(" Number of other bidders "));
    	userBids.add(new JLabel(String.valueOf(this.numberOfOtherBidders)));
    	userBids.add(new JLabel(BID_SPACER));
    	userBids.add(new JLabel(" Other bidders mix "));
    	userBids.add(new JLabel(getBidderMix()));
    	userBids.add(new JLabel(BID_SPACER));
    	
    	//Show the header for the user bid
    	JLabel userBidsPriceHeader = new JLabel(" Price");
    	Font headerFont = userBidsPriceHeader.getFont();
    	Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, headerFont.getSize());
    	userBidsPriceHeader.setFont(boldHeaderFont);
    	userBids.add(userBidsPriceHeader);
    	JLabel userBidsItemsHeader = new JLabel("Items");
    	userBidsItemsHeader.setFont(boldHeaderFont);
    	userBids.add(userBidsItemsHeader);
    	userBids.add(new JLabel(BID_SPACER));
    	
    	//Show the user the bid vector selection
    	for (int userBidCounter = 0; userBidCounter < MAXIMUM_UNIT_BID_PRICE; ++userBidCounter) {
    		
    		userBids.add(new JLabel(" " + String.valueOf(userBidCounter + 1)));
    		
     		//Create spinner for number of items
        	SpinnerNumberModel numberOfItemsModel = new SpinnerNumberModel(1, 1, NUMBER_OF_ITEMS_FOR_SALE, 1);
        	JSpinner numberOfItemsSpinner = new JSpinner(numberOfItemsModel);
        	numberOfItemsSpinner.addChangeListener(this.new NumberItemsSpinnerChangeListener(numberOfItemsSpinner, userBidCounter + 1));
        	numberOfItemsSpinner.setToolTipText("Select a value from 1 to " + NUMBER_OF_ITEMS_FOR_SALE);
        	userBids.add(numberOfItemsSpinner);
        	userBids.add(new JLabel(BID_SPACER));
    		
    	}
    	
     	contentPane.removeAll();
     	contentPane.add(userBids);
     	this.pack();
     	this.setVisible(true);
    }
    
    /**
     * @return description of other bidders mix
     */
    private String getBidderMix() {
    	
    	switch (this.otherBiddersMix) {
    	
    	case AllRandom:
    		return "All Random";

    	case AllQuick:
    		return "All Quick";
    		
    	case AllSlow:
    		return "All Slow";
    		
    	case EqualMix:
    		return "Equal mix of bidders";
    		
    	default:
    		return "Unknown";
    	
    	}
    }
    
    private void computeAuctionResults() {
    	
    	if (!isUserBidValid()) {
    		return;
    	}
    	
    	List<Bidder> bidsFromBidders = getBidsFromBidders();
    	
    	Map<Bidder, Allocation> itemAllocations = getItemAllocations(bidsFromBidders);
    	
    	showAuctionResults(bidsFromBidders, itemAllocations);
    	
    }
    
    /**
     * Show the auction results in terms of all the bids and the units allocated to each bidder
     */
    private void showAuctionResults(List<Bidder> bidsFromBidders, Map<Bidder, Allocation> itemAllocations) {
    	
    	//Create the panel that will contain the user bid
    	Container contentPane = this.getContentPane();
    	contentPane.setLayout(new BorderLayout());
    	
    	//Panel to show bids from all the bidders
    	JPanel allBids = new JPanel();
    	allBids.setLayout(new GridLayout(MAXIMUM_UNIT_BID_PRICE + 1, this.numberOfOtherBidders + 2));
    	
    	//Show the header for the bids
    	JLabel unitPriceHeader = new JLabel("Price");
    	Font unitPriceHeaderFont = unitPriceHeader.getFont();
    	Font boldHeaderFont = new Font(unitPriceHeaderFont.getFontName(), Font.BOLD, unitPriceHeaderFont.getSize());
    	unitPriceHeader.setFont(boldHeaderFont);
    	allBids.add(unitPriceHeader);
    	
    	//Loop though the bidders and show the bidder number and type in the header
    	JLabel bidderHeader = null;
    	for (int bidderNumber = 0; bidderNumber < this.numberOfOtherBidders + 1; ++bidderNumber) {
    		bidderHeader = new JLabel(bidsFromBidders.get(bidderNumber).getBidderNumber() + bidsFromBidders.get(bidderNumber).getBidderTypeLetter());
    		bidderHeader.setFont(boldHeaderFont);
    		allBids.add(bidderHeader);
    	}
    	
    	//Loop through the bidders and show the number of units at each bid price
    	int numberOfUnits = 0;
    	JLabel numberOfUnitsToDisplay = null, unitPriceToDisplay = null;
    	
    	//Loop through each unit price
    	for (int unitPrice = 1; unitPrice <= MAXIMUM_UNIT_BID_PRICE; ++unitPrice) {
			
			//Unit Price
			unitPriceToDisplay = new JLabel(String.valueOf(unitPrice));
			allBids.add(unitPriceToDisplay);
    		
    		//For each unit price show the number of units bid for by each bidder
    		for (int bidderNumber = 0; bidderNumber < this.numberOfOtherBidders + 1; ++bidderNumber) {
    			
    			numberOfUnits = bidsFromBidders.get(bidderNumber).getBidVector().get(Integer.valueOf(unitPrice));
    			numberOfUnitsToDisplay = new JLabel(String.valueOf(numberOfUnits));
    			allBids.add(numberOfUnitsToDisplay);
    		
    		}
    	}

    	//Panel to show winning bidders and units won
    	Set<Bidder> winningBidders = itemAllocations.keySet();
    	JPanel itemsWon = new JPanel();
    	itemsWon.setLayout(new GridLayout(winningBidders.size() + 1, 5));
    	
    	//Show headers for winning bidders
    	JLabel winningBidderHeader = new JLabel("Bidder");
    	winningBidderHeader.setFont(boldHeaderFont);
    	itemsWon.add(winningBidderHeader);
    	JLabel winningBidderUnits = new JLabel("Units");
    	winningBidderUnits.setFont(boldHeaderFont);
    	itemsWon.add(winningBidderUnits);
    	JLabel winningBidderPrice = new JLabel("Price");
    	winningBidderPrice.setFont(boldHeaderFont);
    	itemsWon.add(winningBidderPrice);
    	JLabel originalClearingPrice = new JLabel("Original Clearing Price");
    	originalClearingPrice.setFont(boldHeaderFont);
    	itemsWon.add(originalClearingPrice);
    	JLabel bidderUtility = new JLabel("Utility");
    	bidderUtility.setFont(boldHeaderFont);
    	itemsWon.add(bidderUtility);
    	
    	//Show the units won by each bidder
    	for (Bidder bidder : winningBidders) {
    		
    		itemsWon.add(new JLabel(bidder.getBidderNumber() + bidder.getBidderTypeLetter()));
    		itemsWon.add(new JLabel(String.valueOf(itemAllocations.get(bidder).getNumberOfItems())));
    		itemsWon.add(new JLabel(String.valueOf(itemAllocations.get(bidder).getUnitPrice())));
    		itemsWon.add(new JLabel(String.valueOf(this.originalClearingPrice)));
    		itemsWon.add(new JLabel(String.valueOf(this.originalClearingPrice - itemAllocations.get(bidder).getUnitPrice())));
    	}
     	contentPane.removeAll();
     	contentPane.add(allBids, BorderLayout.NORTH);
     	contentPane.add(itemsWon, BorderLayout.SOUTH);
     	this.pack();
     	this.setVisible(true);
     	
    }
    
    private List<Bidder> getBidsFromBidders() {
    	
    	List<Bidder> returnValue = new ArrayList<Bidder>();
    	
    	Random bidUnitsGenerator = new Random();

    	int bidderNumber = 0;
    	switch (this.otherBiddersMix) {
    	
    	case AllRandom:
    		for (int bidderIndex = 0; bidderIndex < this.numberOfOtherBidders; ++bidderIndex) {
    			returnValue.add(new RandomBidder(bidderNumber++, bidUnitsGenerator));
    		}
    		break;
    		
    	case AllQuick:
    		for (int bidderIndex = 0; bidderIndex < this.numberOfOtherBidders; ++bidderIndex) {
    			returnValue.add(new QuickBidder(bidderNumber++, bidUnitsGenerator));
    		}
    		break;
    		
    	case AllSlow:
    		for (int bidderIndex = 0; bidderIndex < this.numberOfOtherBidders; ++bidderIndex) {
    			returnValue.add(new SlowBidder(bidderNumber++, bidUnitsGenerator));
    		}
    		break;
    		
    	case EqualMix:
    		int randomBidderCount = this.numberOfOtherBidders / 3;
    		for (int bidderIndex = 0; bidderIndex < randomBidderCount; ++bidderIndex) {
    			returnValue.add(new RandomBidder(bidderNumber++, bidUnitsGenerator));
    		}
    		int quickBidderCount = this.numberOfOtherBidders / 3;
    		for (int bidderIndex = 0; bidderIndex < quickBidderCount; ++bidderIndex) {
    			returnValue.add(new QuickBidder(bidderNumber++, bidUnitsGenerator));
    		}
    		int slowBidderCount = this.numberOfOtherBidders - (randomBidderCount + quickBidderCount);
    		for (int bidderIndex = 0; bidderIndex < slowBidderCount; ++bidderIndex) {
    			returnValue.add(new SlowBidder(bidderNumber++, bidUnitsGenerator));
    		}
    		break;
    		
    	default:
    		break;
    	
    	}
    	
    	returnValue.add(new UserBidder(bidderNumber, this.userBid));
    	
    	return returnValue;
    }
    
    /**
     * @param allBidders
     * @return a map of all bidder allocations for bidders who were allocated units
     */
    private Map<Bidder, Allocation> getItemAllocations(List<Bidder> bidsFromBidders) {
    	
    	//Find the number of units allocated to each bidder
    	Map<Bidder, Allocation> bidderAllocations = getBidderAllocations(bidsFromBidders);
    	
    	if (bidderAllocations == null) {
    		return null;
    	}
    	
    	//Loop through all the bidder allocations and remove each bidder at a time to find the clearing price without that bidder
    	Set<Bidder> biddersWithAllocations = bidderAllocations.keySet();
    	for (Bidder bidder : biddersWithAllocations) {
    		bidderAllocations.put(bidder, new Allocation(bidderAllocations.get(bidder).getNumberOfItems(), getClearingPriceForBidder(bidsFromBidders, bidder.getBidderNumber(), bidderAllocations.get(bidder).getUnitPrice())));
    	}
    	
    	return bidderAllocations;
    	
    }
    
    /**
     * @return the allocations based on the Clearing Price which is the unit price at which all the items will be sold
     */
    private Map<Bidder, Allocation> getBidderAllocations(List<Bidder> bidsFromBidders) {
    	
    	int clearingPrice = 0;
    	List<BidderItems> itemsBidByAllBiddersAtAPrice = null;
    	Map<Bidder, Allocation> bidderAllocations = null;
    	
    	//Loop through all the bids starting at the highest bid. Stop when the sum of the number of units bid for is at least equal to the total number of units 
    	for (int unitPrice = MAXIMUM_UNIT_BID_PRICE; unitPrice > 0; --unitPrice) {
    		
    		//Find all bids at this unit price
    		itemsBidByAllBiddersAtAPrice = new ArrayList<BidderItems>();
    		for (Bidder bidder : bidsFromBidders) {
    			
    			itemsBidByAllBiddersAtAPrice.add(new BidderItems(bidder, bidder.getBidVector().get(Integer.valueOf(unitPrice))));
    		
    		}
    		
    		//Sort the bids at this unit price by descending number of items
    		Collections.sort(itemsBidByAllBiddersAtAPrice, new BidderItemsComparator());
    		
    		//Loop through the sorted bids to see if the number of items bid for add up to the total number of items
    		int totalUnitsBidFor = 0, bidderAllocation = 0;
    		bidderAllocations = new HashMap<Bidder, Allocation>();
    		for (BidderItems bidderItems : itemsBidByAllBiddersAtAPrice) {
    			
    			totalUnitsBidFor += bidderItems.getBidderItems();
    			if (totalUnitsBidFor >= NUMBER_OF_ITEMS_FOR_SALE) {
    				clearingPrice = unitPrice;
    				bidderAllocation = bidderItems.getBidderItems() - (totalUnitsBidFor - NUMBER_OF_ITEMS_FOR_SALE);
    				bidderAllocations.put(bidderItems.getBidder(), new Allocation(bidderAllocation, unitPrice));
    				break;
    			} else {
    				bidderAllocations.put(bidderItems.getBidder(), new Allocation(bidderItems.getBidderItems(), unitPrice));
    			}
    		}
    		
    		if (clearingPrice != 0) {
    			break;
    		}
    	}
    	
    	if (clearingPrice != 0) {
    		this.originalClearingPrice = clearingPrice;
    		return bidderAllocations;
    	} else {
    		return null;
    	}
    }
    
    /**
     * @param bidsFromBidders
     * @param bidderToBeRemoved
     * @param startingUnitPrice
     * @return the clearing price for the bidder which is the clearing price when the bidder is removed from consideration
     */
    private int getClearingPriceForBidder(List<Bidder> bidsFromBidders, int bidderToBeRemoved, int startingUnitPrice) {
    	
    	int clearingPrice = 0;
    	List<BidderItems> itemsBidByAllBiddersAtAPrice = null;
    	
    	//Loop through all the bids starting at the highest bid. Stop when the sum of the number of units bid for is at least equal to the total number of units 
    	for (int unitPrice = startingUnitPrice; unitPrice > 0; --unitPrice) {
    		
    		//Find all bids at this unit price
    		itemsBidByAllBiddersAtAPrice = new ArrayList<BidderItems>();
    		for (Bidder bidder : bidsFromBidders) {
    			
    			if (bidder.getBidderNumber() != bidderToBeRemoved) {
    				itemsBidByAllBiddersAtAPrice.add(new BidderItems(bidder, bidder.getBidVector().get(Integer.valueOf(unitPrice))));
    			}
    		}
    		
    		//Sort the bids at this unit price by descending number of items
    		Collections.sort(itemsBidByAllBiddersAtAPrice, new BidderItemsComparator());
    		
    		//Loop through the sorted bids to see if the number of items bid for add up to the total number of items
    		int totalUnitsBidFor = 0;
    		for (BidderItems bidderItems : itemsBidByAllBiddersAtAPrice) {
    			
    			totalUnitsBidFor += bidderItems.getBidderItems();
    			if (totalUnitsBidFor >= NUMBER_OF_ITEMS_FOR_SALE) {
    				clearingPrice = unitPrice;
    				break;
    			}
    		}
    		
    		if (clearingPrice != 0) {
    			break;
    		}
    	}
   	
    	return clearingPrice;
    	
    }
    
    /**
     * @return true if user bid has number of items same or decreasing as bid price increases 
     */
    private boolean isUserBidValid() {
    	
    	if (this.userBid == null) {
    		JOptionPane.showMessageDialog(this, USER_BID_NOT_FOUND, USER_BID_ERROR_HEADER, JOptionPane.PLAIN_MESSAGE);
    		return false;
    	}
    	
    	Map<Integer, Integer> userBidVector = this.userBid.getBidVector();
    	if (userBidVector == null) {
    		return false;
    	}
    	
    	//Make sure nmber of items does not increase with unit price
    	int currentNumberOfItems = 0, previousNumberOfItems = 0; 
    	for (int unitPrice = 1; unitPrice <= MAXIMUM_UNIT_BID_PRICE; ++unitPrice) {
    		currentNumberOfItems = userBidVector.get(Integer.valueOf(unitPrice));
    		if (unitPrice != 1) {
    			if (currentNumberOfItems > previousNumberOfItems) {
    				JOptionPane.showMessageDialog(this, UNITS_CANNOT_INCREASE_WITH_UNIT_PRICE, USER_BID_ERROR_HEADER, JOptionPane.PLAIN_MESSAGE);
    				return false;
    			}
    		}
    		previousNumberOfItems = currentNumberOfItems;
    	}
    	
    	return true;
    }
    
    /**
     * Create the menu for user interaction
     */
    private void createMenu() {
    	
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);   
                
        //Create the menus for exiting, setup and running the auction
        JMenu fileMenu, setupMenu, auctionMenu;
        JMenuItem exitMenuItem, SetupMenuItem, putInBidMenuItem, showResultsMenuItem;        
        
        //Create menu item for ending the program
        fileMenu = new JMenu("File");
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {System.exit(0);} });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        //Create the auction setup menu
        setupMenu = new JMenu("Setup");
        SetupMenuItem = new JMenuItem("Bidders");
        SetupMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {showBiddingSetup();} });
        setupMenu.add(SetupMenuItem);
        menuBar.add(setupMenu);
        
        //Create auction bidding and results menu
        auctionMenu = new JMenu("Auction");
        putInBidMenuItem = new JMenuItem("Put in bid");
        putInBidMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {showUserBidInput();} });
        auctionMenu.add(putInBidMenuItem);
        showResultsMenuItem = new JMenuItem("Run Auction");
        showResultsMenuItem.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {computeAuctionResults();} });
        auctionMenu.add(showResultsMenuItem);
        menuBar.add(auctionMenu);
        
        //Create the menu bar
        this.setJMenuBar(menuBar);
    }
    
   /**
    * Create the GUI and show it.  For thread safety,
    * this method is invoked from the
    * event dispatch thread.
    */
   private static void createAndShowGUI() {
       //Create and set up the window.
   		AuctionHouse frame = new AuctionHouse("Multi-Unit Vickrey Auction");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //Create the menu
       frame.createMenu();
       //Display the window.
       frame.getContentPane().setPreferredSize(new Dimension(600, 300));
       frame.pack();
       frame.setVisible(true);
   }
    
   public static void main(String[] args) {
       /* Use an appropriate Look and Feel */
       try {
           UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
       } catch (UnsupportedLookAndFeelException ex) {
           ex.printStackTrace();
       } catch (IllegalAccessException ex) {
           ex.printStackTrace();
       } catch (InstantiationException ex) {
           ex.printStackTrace();
       } catch (ClassNotFoundException ex) {
           ex.printStackTrace();
       }
       /* Turn off metal's use of bold fonts */
       UIManager.put("swing.boldMetal", Boolean.FALSE);
        
       //Schedule a job for the event dispatch thread:
       //creating and showing this application's GUI.
       javax.swing.SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               createAndShowGUI();
           }
       });
   }
   
   private class BidderMixSelectionActionListener implements ActionListener {
   	
		private OtherBiddersMix mixSelection;
		
		public BidderMixSelectionActionListener(OtherBiddersMix mixSelection) {
			this.mixSelection = mixSelection;
		}
   	
		@Override
		public void actionPerformed(ActionEvent e) {
			AuctionHouse.this.otherBiddersMix = this.mixSelection;
		}
   	
   }
   
   private class NumberOfOtherBiddersSpinnerChangeListener implements ChangeListener {
	   
	   private JSpinner numberOfOtherBiddersSpinner;
	   
	   public NumberOfOtherBiddersSpinnerChangeListener(JSpinner numberOfOtherBiddersSpinner) {
		   this.numberOfOtherBiddersSpinner = numberOfOtherBiddersSpinner;
	   }
	   
	   public void stateChanged(ChangeEvent e) {
		   AuctionHouse.this.numberOfOtherBidders = Integer.valueOf(this.numberOfOtherBiddersSpinner.getModel().getValue().toString()).intValue();
	   }
   }
   
   //NumberItemsSpinnerChangeListener
   private class NumberItemsSpinnerChangeListener implements ChangeListener {
	   
	   private JSpinner numberOfItemsSpinner;
	   private int unitPrice;
	   
	   public NumberItemsSpinnerChangeListener(JSpinner numberOfItemsSpinner, int unitPrice) {
		   this.numberOfItemsSpinner = numberOfItemsSpinner;
		   this.unitPrice = unitPrice;
	   }
	   
	   public void stateChanged(ChangeEvent e) {
		   AuctionHouse.this.userBid.add(this.unitPrice, Integer.valueOf(this.numberOfItemsSpinner.getModel().getValue().toString()).intValue());
	   }
   }
}
