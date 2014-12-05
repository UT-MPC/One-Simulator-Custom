package core;

/**
 * Created by Aurelius on 12/5/14.
 */
public class Info {

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    private double startTime = 0.0;
    private double endTime = 0.0;
    //private long trashDataRecv = 0;
    //private long redundantDataRecv = 0;
    //private long usefulDataRecv = 0;
    //private long lostData = 0;

    public Info() {}

}
