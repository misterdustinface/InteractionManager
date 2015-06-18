package playground;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.BooleanSupplier;

import org.junit.Test;

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
	public void test_fingerFlickLightswitch_expectLampTurnsOn() {
		Interactor lightswitch = new LightSwitch();
		Subject lamp = new DetachedLamp();
		
		lightswitch.associate(lamp);
		
		assertFalse(lamp.is("Powered"));
		lightswitch.request("FLICK");
		assertTrue(lamp.is("Powered"));
		lightswitch.request("FLICK");
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
	
	@Test
	public void test_monstersFightToTheDeath_expectStrongerMonsterSurvives() {
		Subject strongMonster = new Monster();
		Subject weakerMonster = new Monster();
		strongMonster.increment("Strength", 10);
		
		int initialMonsterHealth = strongMonster.getNumberValueOf("Health").intValue();
		assertEquals(initialMonsterHealth, weakerMonster.getNumberValueOf("Health").intValue());
		
		int numberOfAttacks = 0;
		
		while (weakerMonster.is("Alive")) {
			strongMonster.request("ATTACK", weakerMonster);
			weakerMonster.request("ATTACK", strongMonster);
			
			numberOfAttacks++;
			
			int expectedHealthOfStrongMonster = initialMonsterHealth - (numberOfAttacks * weakerMonster.getNumberValueOf("Strength").intValue());
			int expectedHealthOfWeakerMonster = initialMonsterHealth - (numberOfAttacks * strongMonster.getNumberValueOf("Strength").intValue());
			
			assertEquals(expectedHealthOfStrongMonster, strongMonster.getNumberValueOf("Health").intValue());
			assertEquals(expectedHealthOfWeakerMonster, weakerMonster.getNumberValueOf("Health").intValue());
		}
		
		assertTrue(strongMonster.is("Alive"));
	}

	@Test
	public void test_monsterJump_expectNotGrounded() {
		Subject monster = new Monster();
		
		assertTrue(monster.is("Grounded"));
		monster.request("JUMP");
		monster.request("UPDATE");
		assertFalse(monster.is("Grounded"));
	}
	
	@Test
	public void test_monsterJumpThenImmediatelyPeak_expectGrounded() {
		Subject monster = new Monster();
		
		assertTrue(monster.is("Grounded"));
		monster.request("JUMP");
		monster.request("PEAK");
		monster.request("UPDATE");
		assertTrue(monster.is("Grounded"));
	}
	
	@Test
	public void test_monsterJumpThenPeakAfterJump_expectNotGroundedUntilReachesFloor() {
		Subject monster = new Monster();
		
		assertTrue(monster.is("Grounded"));
		monster.request("JUMP");
		monster.request("UPDATE");
		assertFalse(monster.is("Grounded"));
		
		monster.request("PEAK");
		while(monster.getNumberValueOf("yPos").floatValue() > monster.getNumberValueOf("Floor").floatValue()) {
			assertFalse(monster.is("Grounded"));
			monster.request("UPDATE");
		}
		
		assertTrue(monster.is("Grounded"));
	}
}

class DetachedButton extends Subject {

	public DetachedButton() {
		setValueOf("Pressed", false);
		
		learnAction("PRESS",   (input)-> { setValueOf("Pressed", true); });
		learnAction("HOLD",    (input)-> { setValueOf("Pressed", true); });
		learnAction("RELEASE", (input)-> { setValueOf("Pressed", false); });
	}

}

class DetachedLightSwitch extends Subject {
	
	public DetachedLightSwitch() {
		setValueOf("PassingCurrent", false);
		
		learnAction("FLICK", (input)-> { toggle("PassingCurrent"); });
	}
}

class LightSwitch extends Interactor {
	
	public LightSwitch() {
		setValueOf("PassingCurrent", false);
		
		learnAction("FLICK", (input)-> { toggle("PassingCurrent"); });
	}
	
	public void disturbAssociatedSubject(Subject s) {
		s.request( is("PassingCurrent") ? "POWER_ON" : "POWER_OFF" );
	}
	
}

class DetachedLamp extends Subject {

	public DetachedLamp() {
		setValueOf("Powered", false);
		
		learnAction("POWER_ON",  (input)-> { setValueOf("Powered", true); });
		learnAction("POWER_OFF", (input)-> { setValueOf("Powered", false); });
	}

}

class Monster extends Subject {
	
	public Monster() {
		setValueOf("Health", 100);
		setValueOf("Strength", 10);
		setValueOf("Gravity", 9);
		
		setValueOf("Floor", 0);
		setValueOf("yPos", getValueOf("Floor"));
		setValueOf("Grounded", new BooleanSupplier() {
			public boolean getAsBoolean() {
				return getNumberValueOf("yPos").floatValue() <= getNumberValueOf("Floor").floatValue();
			}
		});
		
		setValueOf("Alive", new BooleanSupplier() {
			public boolean getAsBoolean() {
				return getNumberValueOf("Health").floatValue() > 0;
			}
		});
		setValueOf("yVel", 0);
		setValueOf("JumpSpeed", 10);
		
		learnAction("KILL", (input)-> { 
			setValueOf("Health", 0);
		});
		learnAction("REVIVE", (input)-> { 
			setValueOf("Health", 100);
		});
		
		learnAction("JUMP", (input)-> { 
			if (is("Grounded")) {
				increment("yVel", getValueOf("JumpSpeed"));
			}
		});
		learnAction("PEAK", (input)-> { 
			setValueOf("yVel", 0);
		});
		learnAction("HURT", (damage)-> { 
			decrement("Health", damage);
		});
		learnAction("ATTACK", (enemy)-> {
			((Subject)enemy).request("HURT", getValueOf("Strength"));
		});
		
		learnAction("UPDATE", (input)-> {
			increment("yPos", getValueOf("yVel"));
			if (!is("Grounded")) {
				decrement("yVel", getValueOf("Gravity"));
			}
		});
	}
	
}

