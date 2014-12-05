package routing;

import core.*;

import java.util.List;

/**
 * Created by Aurelius on 12/4/14.
 */
public class MobileRouter extends MessageRouter {

    public MobileRouter(Settings s) {
        super(s);
    }

    protected MobileRouter(MessageRouter r) {
        super(r);
    }

    protected List<Connection> getConnections() {
        return getHost().getConnections();
    }

    // try to send a chunk to all connections
    /*protected Connection tryChunkToAllConnections(Message m) {
        List<Connection> connections = getConnections();

        if (connections.size() == 0) {
            return null;
        }

        int retVal;
        for (int i=0; i<connections.size(); i++) {
            Connection con = connections.get(i);
            retVal = startTransfer(m, con);
        }
    }*/

    /*@Override
    public void init(DTNHost host, List<MessageListener> mListeners) {
        super.init(host, mListeners);
        //this.sendingConnections = new ArrayList<Connection>();
    }*/

    protected int startTransfer(Message m, Connection con) {

        int retVal;
        //System.out.println("Preparing to send chunk...");
        //check if connection is ready for transfer (i.e., another message is not already being sent
        if (!con.isReadyForTransfer()) {
            return TRY_LATER_BUSY;
        }

        System.out.println("Actually initiating transfer...");
        retVal = con.startTransfer(getHost(), m);
        /*if (retVal == RCV_OK) {
            //TODO: add to sending connections
            addToSendingConnections(con);
        }*/

        return retVal;

    }

    @Override
    public int receiveMessage(Message m, DTNHost from) {
        //System.out.println("receiveMessage called");
        return super.receiveMessage(m, from);
    }

    /*protected void addToSendingConnections(Connection con) {
        this.sendingConnections.add(con);
    }*/

    @Override
    public void changedConnection(Connection con) {
        if (con.isUp()) {
            // Swap beacon information
            DTNHost me = getHost();
            DTNHost other = con.getOtherNode(me);

            me.updateBeaconFromConnection(other.getName(), other.getBeacon());
            other.updateBeaconFromConnection(me.getName(), me.getBeacon());

        }
    }

    @Override
    public MessageRouter replicate() {
        return new MobileRouter(this);
    }

    @Override
    public void update() {
        // update the connections
        // call update on the app

        super.update();

        Chunk chk = getHost().chunkToSend;
        String id = chk.itemId+chk.chunkId;
        Message newMessage = new Message(getHost(), getHost(), id, chk.size);
        newMessage.setAppID(chk.appId);
        newMessage.chk = new Chunk(chk);
        //System.out.println("Preparing to send chunk...");
        for (int i=0; i<this.getConnections().size(); ++i) {
            Connection con = this.getConnections().get(i);

            if (con.isMessageTransferred()) {
                if (con.getMessage() != null) {
                    //transferDone(con);
                    con.finalizeTransfer();
                } /* else: some other entity aborted transfer */
            }


            startTransfer(newMessage, con);
        }

        //tryChunkToAllConnections(m);
    }
}
