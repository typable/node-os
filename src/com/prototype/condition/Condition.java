package com.prototype.condition;

import util.log.Logger.Messages;


public class Condition {

	public static boolean notNull(Object object, String objectName) {

		if(object == null) {

			throw new NullPointerException(Messages.UNDEFINED.getMessage(objectName));
		}

		return true;
	}
}
