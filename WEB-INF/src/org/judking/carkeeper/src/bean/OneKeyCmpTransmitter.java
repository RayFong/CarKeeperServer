package org.judking.carkeeper.src.bean;

public class OneKeyCmpTransmitter {
	private String cmd;
	private String currentAvg;
	private String otherAvg;

	
	public OneKeyCmpTransmitter() {
	}

	public OneKeyCmpTransmitter(String cmd, String currentAvg, String otherAvg) {
		this.cmd = cmd;
		this.currentAvg = currentAvg;
		this.otherAvg = otherAvg;
	}

	@Override
	public String toString() {
		return "OneKeyCmpTransmitter [cmd=" + cmd + ", currentAvg="
				+ currentAvg + ", otherAvg=" + otherAvg + "]";
	}
	
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getCurrentAvg() {
		return currentAvg;
	}
	public void setCurrentAvg(String currentAvg) {
		this.currentAvg = currentAvg;
	}
	public String getOtherAvg() {
		return otherAvg;
	}
	public void setOtherAvg(String otherAvg) {
		this.otherAvg = otherAvg;
	}
	
}
