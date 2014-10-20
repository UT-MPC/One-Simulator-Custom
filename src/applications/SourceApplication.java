package applications;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import core.Application;
import core.BitVector;
import core.Chunk;
import core.DTNHost;
import core.Item;
import core.Message;
import core.Settings;

public class SourceApplication extends Application {

	/** Application ID */
	public static final String APP_ID = "SourceApplication";
	
	public static final String NUM_ITEMS = "numItems";
	public static final String ITEM_PREFIX = "item"; // format of ITEM_NAME, SIZE
	public static final String NUM_CHUNKS = "numChunks";
	
	HashMap<String, BitVector> bitVectors;
	HashMap<String, Item> items; //TODO change string to UUID
	
	int num_chunks = 64;
	
	public SourceApplication(Settings s) {
		
		if (s.contains(NUM_CHUNKS)) {
			num_chunks = s.getInt(NUM_CHUNKS);
		}
		if (s.contains(NUM_ITEMS)) {
			int numItems = s.getInt(NUM_ITEMS);
			for (int i=1; i<=numItems; ++i) {
				String [] itemParse = s.getCsvSetting(ITEM_PREFIX+i); // Needs better error checking
				String curItemName = itemParse[0];
				int curItemSize = Integer.parseInt(itemParse[1]);
				// construct a item
				Item newItem = new Item(curItemName, curItemSize, new BitVector(-1)); // set the bit vector to all 1s
				items.put(curItemName, newItem);
				bitVectors.put(curItemName, newItem.bv);
			}
		}
		super.setAppID(APP_ID);
	}

	public SourceApplication (SourceApplication a) {
		super(a);
	}
	
	@Override
	public Message handle(Message msg, DTNHost host) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void update(DTNHost host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Application replicate() {
		// TODO Auto-generated method stub
		return new SourceApplication(this);
	}

}