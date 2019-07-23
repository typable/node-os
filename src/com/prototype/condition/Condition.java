package com.prototype.condition;

import com.prototype.logger.Logger.Messages;


public class Condition {

	public static boolean notNull(Object object, String objectName) {

		if(object == null) {

			throw new NullPointerException(Messages.UNDEFINED.getMessage(objectName));
		}

		return true;
	}
}
