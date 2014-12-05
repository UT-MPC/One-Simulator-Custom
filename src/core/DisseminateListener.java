package core;

/**
 * Created by Aurelius on 12/5/14.
 */
public interface DisseminateListener {

    public void startItem(String hostId, Item item, double time);

    public void endItem(String hostId, Item item, double time);

    public void addTrashData(long trashData);

    public void addRedundantData(long RedundantData);

    public void addLostData(long lostData);
}
