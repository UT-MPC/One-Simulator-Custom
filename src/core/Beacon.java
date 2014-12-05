package core;

import java.util.HashMap;

/**
 * Created by Aurelius on 12/4/14.
 */
public class Beacon {

    public String userId;
    public HashMap<String, BitVector> bvMap; // map of itemId to bit vector
    long timestamp;

    public Beacon(String userId, HashMap<String, BitVector> bvMap, long timestamp) {
        this.userId = userId;
        this.bvMap = bvMap;
        this.timestamp = timestamp;
    }
}
