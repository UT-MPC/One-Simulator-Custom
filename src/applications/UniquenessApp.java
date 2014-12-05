package applications;

import core.*;

import java.util.HashMap;

/**
 * Created by Aurelius on 12/5/14.
 */
public class UniquenessApp extends DisseminateApplication {

    public static final String APP_ID = "UniquenessApp";

    public UniquenessApp(Settings s) {
        super.setAppID(APP_ID);
    }

    public UniquenessApp(DisseminateApplication a) {
        super(a);
    }

    @Override
    public void update(DTNHost host) {
        HashMap<String, Beacon> beacons = host.beacons;
        HashMap<String, Item> items = host.items;

        // first find the intersection of chunks

        HashMap<>

        // select the chunk
        Item i = host.items.get("item0");
        //System.out.println("items length:"+host.items.size());
        if (i == null) {
            System.err.println("Item should not be null");
        }
        Chunk chunkToSend = new Chunk (i.name, 0, host.chunkSize, this.appID);
        host.chunkToSend = chunkToSend;
    }

    @Override
    public Application replicate() {
        return new UniquenessApp(this);
    }
}
