package applications;

import core.Application;
import core.DTNHost;
import core.Message;
import core.Settings;

public class MobileHostApplication extends Application {

	/** Application ID */
	public static final String APP_ID = "MobileHostApplication";
	
	public MobileHostApplication(Settings s) {
		super.setAppID(APP_ID);
	}
	
	public MobileHostApplication (MobileHostApplication a) {
		super(a);
	}
	
	@Override
	public Message handle(Message msg, DTNHost host) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(DTNHost host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Application replicate() {
		// TODO Auto-generated method stub
		return new MobileHostApplication(this);
	}

}
