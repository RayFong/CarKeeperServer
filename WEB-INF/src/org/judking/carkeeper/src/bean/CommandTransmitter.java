package org.judking.carkeeper.src.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.judking.carkeeper.src.model.PddDataModel;

public class CommandTransmitter {
	public static String vin;
	private Date date;
	private Map<String, PddDataModel> commands;

	@Override
	public String toString() {
		return "CommandTransmitter [vin=" + vin + ", date=" + date
				+ ", commands=" + commands + "]";
	}

	public CommandTransmitter()		{
		date = new Date();
	}
	
	public CommandTransmitter(Map<String, PddDataModel> commands)		{
		this();
		this.commands = commands;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<String, PddDataModel> getCommands() {
		return commands;
	}

	public void setCommands(Map<String, PddDataModel> commands) {
		this.commands = commands;
	}

}
