package os.type.holder;

import java.lang.reflect.Method;

import os.event.Event;


public class EventHolder extends MethodHolder {

	private Event event;
	private os.type.Event definition;

	public EventHolder(Event event, os.type.Event definition, Object instance, Method method) {

		super(instance, method);

		this.event = event;
		this.definition = definition;
	}

	public Event getEvent() {

		return event;
	}

	public void setEvent(Event event) {

		this.event = event;
	}

	public os.type.Event getDefinition() {

		return definition;
	}

	public void setDefinition(os.type.Event definition) {

		this.definition = definition;
	}
}
