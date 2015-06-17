package base;

import java.util.Set;

public class Subject extends Actor {
	
	private Attributes attributes;
	
	public Subject() {
		attributes = new Attributes();
	}
	
	public void request(String request) {
		try {
			performAction(request);
		} catch (InvalidActionException uae) {
			uae.printStackTrace();
		}
	}

	final public Object getValueOf(String attributeName) {
		return attributes.getValueOf(attributeName);
	}
	
	final public Set<String> getAttributes() {
		return attributes.getAttributes();
	}
	
	final public boolean is(String attributeName) {
		return attributes.is(attributeName);
	}
	
	protected void setValueOf(String attributeName, Object attribute) {
		attributes.setValueOf(attributeName, attribute);
	}
	
	protected void toggle(String attributeName) {
		attributes.toggle(attributeName);
	}

}
