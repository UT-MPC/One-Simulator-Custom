package core;

/**
 * Created by Aurelius on 12/4/14.
 */
public class VariableConnection extends Connection {
    /**
     * Creates a new connection between nodes and sets the connection
     * state to "up".
     *
     * @param fromNode      The node that initiated the connection
     * @param fromInterface The interface that initiated the connection
     * @param toNode        The node in the other side of the connection
     * @param toInterface   The interface in the other side of the connection
     */
    public VariableConnection(DTNHost fromNode, NetworkInterface fromInterface, DTNHost toNode, NetworkInterface toInterface) {
        super(fromNode, fromInterface, toNode, toInterface);
    }

    @Override
    public int startTransfer(DTNHost from, Message m) {
        return 0;
    }

    @Override
    public int getRemainingByteCount() {
        return 0;
    }

    @Override
    public boolean isMessageTransferred() {
        return false;
    }

    @Override
    public double getSpeed() {
        return 0;
    }
}
