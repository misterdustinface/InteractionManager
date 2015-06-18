package base;

import java.util.Set;
import java.util.function.Consumer;

import datastructures.Table;

public class Actor {

	final private Table<Consumer> actions;
	final private static Consumer NULL_CONSUMER = (input)-> {};
	final private static Object NULL_INPUT = new Object();	
	
	public Actor() {
		actions = new Table<Consumer>();
	}
	
	final public void learnAction(String action) {
		learnAction(action, NULL_CONSUMER);
	}
	
	final public void learnAction(String action, Consumer implementation) {
		actions.insert(action, implementation);
	}
	
	final public void forgetAction(String action) {
		actions.remove(action);
	}
	
	final public void performAction(String action) {
		performAction(action, NULL_INPUT);
	}
	
	@SuppressWarnings("unchecked")
	final public void performAction(String action, Object input) {
		if (actions.contains(action)) {
			actions.get(action).accept(input);
		} else {
			throw new InvalidActionException(action);
		}
	}
	
	final public Set<String> getActions() {
		return actions.getNames();
	}
	
}