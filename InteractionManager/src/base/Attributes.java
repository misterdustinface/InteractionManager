package base;

import java.util.Set;
import java.util.function.BooleanSupplier;

import datastructures.Table;

public class Attributes {
	
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
	
	public boolean is(String attributeName) {
		Object attribute = getValueOf(attributeName);
		if (attribute instanceof BooleanSupplier) {
			return ((BooleanSupplier) attribute).getAsBoolean();
		} else if (attribute instanceof Boolean) {
			return (boolean) attribute;
		} else {
			return false;
		}
	}
	
	public void increment(String attributeName, Object value) {
		Number A = (Number)getValueOf(attributeName);
		Number B = (Number)value;
		setValueOf(attributeName, A.doubleValue() + B.doubleValue());
	}
	
	public void decrement(String attributeName, Object value) {
		Number A = (Number)getValueOf(attributeName);
		Number B = (Number)value;
		setValueOf(attributeName, A.doubleValue() - B.doubleValue());
	}
	
	public Number getNumberValueOf(String attributeName) {
		return (Number)attributes.get(attributeName);
	}
		
}