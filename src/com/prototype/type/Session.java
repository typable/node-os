package com.prototype.type;

import com.core.lang.Property;


public class Session extends Property<Object>
{
	public static final String USID = "usid";

	private String uid;

	public Session(String uid)
	{
		this.uid = uid;
	}

	public String getUid()
	{
		return uid;
	}
}
