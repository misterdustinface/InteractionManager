package base;

import java.util.Set;

import datastructures.Table;

final public class Attributes {
	
	final private Table<Object> attributes;
	
	public Attributes() {
		attributes = new Table<Object>();
	}
	
	public void setValueOf(String name, Object value) {
		attributes.insert(name, value);
	}
	
	public Object getValueOf(String attributeName) {
		return attributes.get(attributeName);
	}
	
	public Set<String> getAttributes() {
		return attributes.getNames();
	}
	
	public void toggle(String booleanAttribute) {
		setValueOf(booleanAttribute, !is(booleanAttribute));
	}
	
	public boolean is(String booleanAttribute) {
		return (boolean)getValueOf(booleanAttribute);
	}
	
}