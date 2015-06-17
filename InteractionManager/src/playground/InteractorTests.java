package playground;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import base.Instigator;
import base.Interactor;
import base.Subject;

public class InteractorTests {
	
	@Test
	public void test_canPressButton_expectButtonAcknowledge() {
		Subject button = new DetachedButton();
		
		assertFalse(button.is("Pressed"));
		button.request("PRESS");
		assertTrue(button.is("Pressed"));
		button.request("RELEASE");
		assertFalse(button.is("Pressed"));
	}
	
	@Test
	public void test_invalidActionsPerformedUponButton_expectButtonDoesNotChangeState() {
		Subject button = new DetachedButton();
		
		assertFalse(button.is("Pressed"));
		button.request("FLICK");
		assertFalse(button.is("Pressed"));
		button.request("FLICK");
		assertFalse(button.is("Pressed"));
	}
	
	@Test
	public void test_fingerCanFlickLightswitch_expectLightswitchAcknowledge() {
		Instigator finger = (subject)-> { subject.request("FLICK"); };
		Subject lightswitch = new DetachedLightSwitch();
		
		assertFalse(lightswitch.is("PassingCurrent"));
		finger.disturb(lightswitch);
		assertTrue(lightswitch.is("PassingCurrent"));
		finger.disturb(lightswitch);
		assertFalse(lightswitch.is("PassingCurrent"));
	}
	
	@Test
	public void test_fingerFlickLightswitch_expectLampTurnsOn() {
		Instigator finger = (subject)-> { subject.request("FLICK"); };
		Interactor lightswitch = new LightSwitch();
		Subject lamp = new DetachedLamp();
		
		lightswitch.associate(lamp);
		
		assertFalse(lamp.is("Powered"));
		finger.disturb(lightswitch);
		assertTrue(lamp.is("Powered"));
		finger.disturb(lightswitch);
		assertFalse(lamp.is("Powered"));
		
	}
	
	@Test
	public void test_pressLightswitch_expectLampDoesNotChangeState() {
		Interactor lightswitch = new LightSwitch();
		Subject lamp = new DetachedLamp();
		
		lightswitch.associate(lamp);
		
		assertFalse(lamp.is("Powered"));
		lightswitch.request("PRESS");
		assertFalse(lamp.is("Powered"));
		lightswitch.request("PRESS");
		assertFalse(lamp.is("Powered"));
	}

}

class DetachedButton extends Subject {

	public DetachedButton() {
		setValueOf("Pressed", false);
		
		learnAction("PRESS",   ()-> { setValueOf("Pressed", true); });
		learnAction("HOLD",    ()-> { setValueOf("Pressed", true); });
		learnAction("RELEASE", ()-> { setValueOf("Pressed", false); });
	}

}

class DetachedLightSwitch extends Subject {
	
	public DetachedLightSwitch() {
		setValueOf("PassingCurrent", false);
		
		learnAction("FLICK", ()-> { toggle("PassingCurrent"); });
	}
}

class LightSwitch extends Interactor {
	
	public LightSwitch() {
		setValueOf("PassingCurrent", false);
		
		learnAction("FLICK", ()-> { toggle("PassingCurrent"); });
	}
	
	public void disturb(Subject s) {
		s.request( is("PassingCurrent") ? "POWER_ON" : "POWER_OFF" );
	}
	
}

class DetachedLamp extends Subject {

	public DetachedLamp() {
		setValueOf("Powered", false);
		
		learnAction("POWER_ON",  ()-> { setValueOf("Powered", true); });
		learnAction("POWER_OFF", ()-> { setValueOf("Powered", false); });
	}

}

class Zombie extends Interactor {
	
	public Zombie() {
		setValueOf("Alive", true);
		setValueOf("Grounded", true);
		
		learnAction("KILL",   ()-> { setValueOf("Alive", false); });
		learnAction("REVIVE", ()-> { setValueOf("Alive", true); });
		learnAction("JUMP",   ()-> { setValueOf("Grounded", false); });
		learnAction("LAND",   ()-> { setValueOf("Grounded", true); });
	}
	
	public void disturb(Subject s) {
		s.request("ATTACK");
	}

	protected boolean shouldAcceptRequest(String request) {
		return (request == "JUMP" && is("Grounded"));
	}
	
}
