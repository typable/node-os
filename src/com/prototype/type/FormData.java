package com.prototype.type;

import com.prototype.http.constants.MediaType;


public class FormData
{
	private MediaType type;
	private String disposition;
	private byte[] data;

	public FormData(MediaType type, String disposition)
	{
		this.type = type;
		this.disposition = disposition;
	}

	public MediaType getType()
	{
		return type;
	}

	public void setType(MediaType type)
	{
		this.type = type;
	}

	public String getDisposition()
	{
		return disposition;
	}

	public void setDisposition(String disposition)
	{
		this.disposition = disposition;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}
}
