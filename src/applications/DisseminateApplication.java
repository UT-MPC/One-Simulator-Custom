package applications;

import core.*;

/**
 * Created by Aurelius on 12/4/14.
 */
public class DisseminateApplication extends Application {

    public static final String APP_ID = "DisseminateApplication";

    public DisseminateApplication() {
        super();
    }

    public DisseminateApplication(Settings s) {
        super.setAppID(APP_ID);
        /*items =  new HashMap<String, Item>();
        if (s.contains(CHUNK_SIZE)) {
            chunkSize = s.getInt(CHUNK_SIZE);
        }
        if (s.contains(ITEM_INTERESTS)) {
            String [] listItems = s.getCsvSetting(ITEM_INTERESTS);
            for (int i=0; i<listItems.length; ++i) {
                items.put(listItems[i], new Item(listItems[i], chunkSize, new BitVector(-1L) ) );
            }
        }*/
    }

    public DisseminateApplication(DisseminateApplication a) {
        super(a);
        //this.chunkSize = a.chunkSize;
        //this.items = new HashMap<String, Item>(a.items);
    }


    @Override
    public Message handle(Message msg, DTNHost host) {
        System.out.println("Handling received message");
        // make sure the message has a chunk
        if (msg.chk != null) {
            // check if item is part of interests
            String curItemName = msg.chk.itemId;
            if (host.items.get(curItemName) == null) {
                //host.trashDataRecv += msg.chk.size;
                System.out.println("updating trashData");
                for (DisseminateListener dl : host.getRouter().dListeners) {
                    System.out.println("updating trashData");
                    dl.addTrashData(msg.chk.size);
                }
                return msg;
            }
            BitVector curBitVector = host.items.get(curItemName).bv;
            // if (curBitVector.data == 0)
            // TODO: check if new item, probably not important
            if (curBitVector.testBit(msg.chk.chunkId)) {
                // we already had the chunk
                System.out.println("updating redundantData");
                for (DisseminateListener dl : host.getRouter().dListeners) {
                    System.out.println("updating redundantData");
                    dl.addRedundantData(msg.chk.size);
                }
                //host.redundantDataRecv += msg.chk.size;
            } else {
                // update the bitVector
                curBitVector.setBit(msg.chk.chunkId);
                // TODO: make sure this update is working
                host.usefulDataRecv += msg.chk.size;
            }
            return msg;
        } else {
            return msg;
        }
    }

    @Override
    public void update(DTNHost host) {
        // select the chunk
        Item i = host.items.get("item0");
        System.out.println("items length:"+host.items.size());
        if (i == null) {
            System.err.println("Item should not be null");
        }
        Chunk chunkToSend = new Chunk (i.name, 0, host.chunkSize, this.appID);
        host.chunkToSend = chunkToSend;
    }

    @Override
    public Application replicate() {
        return new DisseminateApplication(this);
    }
}
