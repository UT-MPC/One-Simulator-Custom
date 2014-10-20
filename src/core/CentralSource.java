package core;

import java.util.List;

import movement.MovementModel;
import routing.MessageRouter;

public class CentralSource extends DTNHost {
	
	public CentralSource(List<MessageListener> msgLs,
			List<MovementListener> movLs, String groupId,
			List<NetworkInterface> interf, ModuleCommunicationBus comBus,
			MovementModel mmProto, MessageRouter mRouterProto) {
		super(msgLs, movLs, groupId, interf, comBus, mmProto, mRouterProto);
	}

}
