package applications;

import core.*;

/**
 * Created by Aurelius on 12/5/14.
 */
public class RandomApp extends DisseminateApplication {

    public static final String APP_ID = "RandomApp";

    public RandomApp(Settings s) {
        super.setAppID(APP_ID);
    }

    public RandomApp(DisseminateApplication a) {
        super(a);
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
        return new RandomApp(this);
    }
}
