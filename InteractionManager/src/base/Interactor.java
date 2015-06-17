package base;

import java.util.HashSet;

abstract public class Interactor extends Subject implements Instigator {
	
	private HashSet<Subject> associatedSubjects;
	
	public Interactor() {
		associatedSubjects = new HashSet<Subject>();
	}
	
	public void associate(Subject s) {
		associatedSubjects.add(s);
	}
	
	public void disassociate(Subject s) {
		associatedSubjects.remove(s);
	}
	
	public void request(String request) {
		super.request(request);
		interactWithAssociatedSubjects();
	}
	
	private void interactWithAssociatedSubjects() {
		for (Subject s : associatedSubjects) {
			disturb(s);
		}
	}
	
}
