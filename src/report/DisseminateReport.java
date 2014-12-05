package report;

import core.DisseminateListener;
import core.Info;
import core.Item;

import java.util.HashMap;

/**
 * Created by Aurelius on 12/5/14.
 */
public class DisseminateReport extends Report implements DisseminateListener {

    private HashMap<String, HashMap<String, Info>> completeData;
    private long trashData;
    private long redundantData;
    private long lostData;

    public DisseminateReport() {
        init();
    }

    @Override
    protected void init() {
        super.init();
        completeData =  new HashMap<String, HashMap<String, Info>>();
    }

    // TODO: starttime is hardcoded as zero right now!
    @Override
    public void startItem(String hostId, Item item, double time) {
        if (completeData.get(hostId) == null) {
            HashMap<String, Info> itemData = new HashMap<String,Info>();
            Info newInfo = new Info();
            newInfo.setStartTime(0.0);
            itemData.put(item.name, newInfo);
            completeData.put(hostId, itemData);
        } else if (completeData.get(hostId).get(item.name) == null) {
            HashMap<String, Info> itemData = completeData.get(hostId);
            Info newInfo = new Info();
            newInfo.setStartTime(0.0);
            itemData.put(item.name, newInfo);
        } else {
            completeData.get(hostId).get(item.name).setStartTime(0.0);
        }
    }

    @Override
    public void endItem(String hostId, Item item, double time) {
        if (completeData.get(hostId) == null) {
            HashMap<String, Info> itemData = new HashMap<String,Info>();
            Info newInfo = new Info();
            newInfo.setStartTime(time);
            itemData.put(item.name, newInfo);
            completeData.put(hostId, itemData);
        } else if (completeData.get(hostId).get(item.name) == null) {
            HashMap<String, Info> itemData = completeData.get(hostId);
            Info newInfo = new Info();
            newInfo.setStartTime(time);
            itemData.put(item.name, newInfo);
        } else {
            completeData.get(hostId).get(item.name).setStartTime(time);
        }
    }

    @Override
    public void addTrashData(long trashData) {
        this.trashData += trashData;
    }

    @Override
    public void addRedundantData(long redundantData) {
        this.redundantData += redundantData;
    }

    @Override
    public void addLostData(long lostData) {
        this.lostData += lostData;
    }

    @Override
    public void done() {

        String output = "";
        for (String device : completeData.keySet()) {
            for (String item : completeData.get(device).keySet()) {
                double start = completeData.get(device).get(item).getStartTime();
                double end = completeData.get(device).get(item).getEndTime();
                double timeElapsed = end - start;
                output += "\n"+device+" : "+Double.toString(timeElapsed);
            }
        }
        output += "\nTrashData : "+trashData;
        output += "\nRedundantData : "+redundantData;
        output += "\nLostData : "+lostData;


        write(output);
        // this gets called at the very end!!
        super.done();
    }
}
