package server.mapmodel;

import server.map.BattlefieldThread;
import server.map.sections.Section;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alex Wieser on 06.04.2017.
 */
public class FieldModel {
	/**
	 * The position of the field
	 */
	int position;

	/**
	 * The owner of the field
	 */
	int owner;

	/**
	 * The amout of units this field starts with
	 */
	int startUnits;

	/**
	 * Which {@link Section.State} the Field has
	 */
	String state;

	/**
	 * What Class this field has. The Class must extend {@link Section}
	 */
	String fieldClass;

	/**
	 * Creates this field on the battlefield
	 * @param battlefield
	 */
	public void apply(BattlefieldThread battlefield) {
		if (owner != 0) {
			applyOwner(battlefield);
		}
		if (state != null) {
			applyState(battlefield);
		}
		if (fieldClass != null) {
			applyClass(battlefield);
		}
	}

	/**
	 * Constructs a Section from the given Classname and transforms the Section at {@code position} into it
	 *
	 * @param battlefield
	 */
	private void applyClass(BattlefieldThread battlefield) {
		try {
			Class c = Class.forName(fieldClass);
			Constructor constructor = c.getConstructors()[0];
			battlefield.transformSectionTo(position, (Section) constructor.newInstance(battlefield, battlefield.getSectionByPos(position)));
		} catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the field at {@code position} to the given State
	 *
	 * @param battlefield
	 */
	private void applyState(BattlefieldThread battlefield) {
		battlefield.getSectionByPos(position).setState(Section.State.valueOf(state));
	}

	/**
	 * Sets the owner at the field at {@code position} to the given owner and sets the unitCount to the given unitCount
	 *
	 * @param battlefield
	 */
	private void applyOwner(BattlefieldThread battlefield) {
		Section section = battlefield.getSectionByPos(position);
		section.setOwner(owner);
		section.setUnits(startUnits);
	}

}
