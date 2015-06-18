package base;

import java.util.Set;
import java.util.function.Consumer;


public class Subject extends Attributes {
	
	private Actor actor;
	
	public Subject() {
		actor = new Actor();
	}

	public void request(String request) {
		try {
			actor.performAction(request);
		} catch (InvalidActionException uae) {
			uae.printStackTrace();
		}
	}
	
	public void request(String request, Object input) {
		try {
			actor.performAction(request, input);
		} catch (InvalidActionException uae) {
			uae.printStackTrace();
		}
	}
	
	final public void learnAction(String action) {
		actor.learnAction(action);
	}
	
	final public void learnAction(String action, Consumer implementation) {
		actor.learnAction(action, implementation);
	}
	
	final public void forgetAction(String action) {
		actor.forgetAction(action);
	}
	
	final public Set<String> getActions() {
		return actor.getActions();
	}

}
