/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package core;

import routing.MessageRouter;

import java.util.Random;

/**
 * A constant bit-rate connection between two DTN nodes.
 */
public class CBRConnection extends Connection {
	private int speed;
	private double transferDoneTime;

    public double start_transfer_time = 0;
    public double end_transfer_time = 0;
    public int pseudo_transferred = 0;
	/**
	 * Creates a new connection between nodes and sets the connection
	 * state to "up".
	 * @param fromNode The node that initiated the connection
	 * @param fromInterface The interface that initiated the connection
	 * @param toNode The node in the other side of the connection
	 * @param toInterface The interface in the other side of the connection
	 * @param connectionSpeed Transfer speed of the connection (Bps) when 
	 *  the connection is initiated
	 */
	public CBRConnection(DTNHost fromNode, NetworkInterface fromInterface, 
			DTNHost toNode,	NetworkInterface toInterface, int connectionSpeed) {
		super(fromNode, fromInterface, toNode, toInterface);
		this.speed = connectionSpeed;
		this.transferDoneTime = 0;

	}

	/**
	 * Sets a message that this connection is currently transferring. If message
	 * passing is controlled by external events, this method is not needed
	 * (but then e.g. {@link #finalizeTransfer()} and 
	 * {@link #isMessageTransferred()} will not work either). Only a one message
	 * at a time can be transferred using one connection.
	 * @param from The host sending the message
	 * @param m The message
	 * @return The value returned by 
	 * {@link MessageRouter#receiveMessage(Message, DTNHost)}
	 */
	public int startTransfer(DTNHost from, Message m) {
		assert this.msgOnFly == null : "Already transferring " + 
			this.msgOnFly + " from " + this.msgFromNode + " to " + 
			this.getOtherNode(this.msgFromNode) + ". Can't " + 
			"start transfer of " + m + " from " + from;

		this.msgFromNode = from;
		Message newMessage = m.replicate();
		//int retVal = getOtherNode(from).receiveMessage(newMessage, from);
        int retVal = MessageRouter.RCV_OK;
		if (retVal == MessageRouter.RCV_OK) {
			this.msgOnFly = newMessage;

            this.speed = this.fromInterface.getTransmitSpeed();

            //System.out.println("from speed: "+this.speed);
            //int othspeed =  this.toInterface.getTransmitSpeed();
            //System.out.println("to speed: "+othspeed);
            //if (othspeed  this.speed) {
            //    this.speed = othspeed;
            //}

            if (this.speed == 0) {
                this.fromInterface.update();
                this.speed = this.fromInterface.getTransmitSpeed();
            }
            System.out.println("Speed: "+this.speed);
            //System.out.println("m size: "+m.getSize());
            //System.out.println("newMessage size: "+newMessage.getSize());
            start_transfer_time = SimClock.getTime();
            //System.out.println("Current time: "+SimClock.getTime());
			this.transferDoneTime = SimClock.getTime() +
			(1.0*m.getSize()) / this.speed;
            //System.out.println("transferDoneTime :"+transferDoneTime);
            end_transfer_time = Math.ceil(transferDoneTime);
		}

        getOtherNode(msgFromNode).receiveMessage(m, msgFromNode);

		return retVal;
	}

    @Override
    public void finalizeTransfer() {
        System.out.println("CBRConnection --> finalizeTransfer");
        assert this.msgOnFly != null : "Nothing to finalize in " + this;
        assert msgFromNode != null : "msgFromNode is not set";

        Random rng = new Random();
        double lost = rng.nextDouble();
        double time_elapsed = end_transfer_time - start_transfer_time;
        //double actual_speed = (double)pseudo_transferred/time_elapsed;
        //System.out.println("Actual speed: "+actual_speed);
        double probability = (double)pseudo_transferred/(double)this.msgOnFly.getSize();
        System.out.println("Pseudo transferred: "+pseudo_transferred);
        double distance = this.fromNode.getLocation().distance(this.toNode.getLocation());
        System.out.println("distance : "+distance);
        System.out.println("Probability: "+probability);
        System.out.println("Lost: "+lost);
        if (probability >= lost) {
            // successful transfer;
            System.out.println("* * * * * * * * * * * * * * * * * Success * * * * * * * * * * * * * * * * * *");
            System.out.println(msgFromNode+" sent chunk to "+getOtherNode(msgFromNode).getName());
            this.bytesTransferred += msgOnFly.getSize();

            getOtherNode(msgFromNode).messageTransferred(this.msgOnFly.getId(),
                    msgFromNode);
        } else {
            // message dropped
            //getOtherNode(msgFromNode).lostData += msgOnFly.getSize();
            System.out.println("Dropped");
            for (DisseminateListener dl : getOtherNode(msgFromNode).getRouter().dListeners) {
                dl.addLostData(msgOnFly.getSize());
            }
        }
        end_transfer_time = 0;
        start_transfer_time = 0;
        pseudo_transferred = 0;
        clearMsgOnFly();
    }

       // Tomasz, on each update interval update the probability calculation
    public void update() {
        int curSpeed = this.fromInterface.getTransmitSpeed();
        int othspeed =  this.toInterface.getTransmitSpeed();
        if (othspeed < curSpeed) {
            curSpeed = othspeed;
        }

        // Tomasz
        // modify current speed to reflect path loss
        double ref_point = 100;
        double distance = this.fromNode.getLocation().distance(this.toNode.getLocation());
        //System.out.println("distance : "+distance);
        double pathLoss = ref_point/(Math.pow(distance, alpha));
        if (pathLoss > 1.0) {
            pathLoss = 1.0;
        }
        //System.out.println("alpha : "+alpha);
        //System.out.println("pathLoss : "+pathLoss);
        //System.out.println("curSpeed (before computation) : "+curSpeed);
        curSpeed = (int) Math.round((double)curSpeed * pathLoss);
        //System.out.println("curSpeed during transfer : "+curSpeed);
        if (SimClock.getTime() > start_transfer_time) {
            //System.out.println("Current time: " + SimClock.getTime());
            pseudo_transferred += curSpeed;
            //System.out.println("Distance: " + distance);
            //System.out.println("Pseudo_transferred: " + pseudo_transferred);
        }
    }

	/**
	 * Aborts the transfer of the currently transferred message.
	 */
	public void abortTransfer() {
		assert msgOnFly != null : "No message to abort at " + msgFromNode;
		getOtherNode(msgFromNode).messageAborted(this.msgOnFly.getId(),
				msgFromNode,getRemainingByteCount());
		clearMsgOnFly();
		this.transferDoneTime = 0;
	}

	/**
	 * Gets the transferdonetime
	 */
	public double getTransferDoneTime() {
		return transferDoneTime;
	}
	
	/**
	 * Returns true if the current message transfer is done.
	 * @return True if the transfer is done, false if not
	 */
	public boolean isMessageTransferred() {
		return getRemainingByteCount() == 0;
	}

	/**
	 * returns the current speed of the connection
	 */
	public double getSpeed() {
		return this.speed;
	}

	/**
	 * Returns the amount of bytes to be transferred before ongoing transfer
	 * is ready or 0 if there's no ongoing transfer or it has finished
	 * already
	 * @return the amount of bytes to be transferred
	 */
	public int getRemainingByteCount() {
		int remaining;

		if (msgOnFly == null) {
			return 0;
		}

		remaining = (int)((this.transferDoneTime - SimClock.getTime()) 
				* this.speed);

		return (remaining > 0 ? remaining : 0);
	}

	/**
	 * Returns a String presentation of the connection.
	 */
	public String toString() {
		return super.toString() + (isTransferring() ?  
				" until " + String.format("%.2f", this.transferDoneTime) : "");
	}

}
