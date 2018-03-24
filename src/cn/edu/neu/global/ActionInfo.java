package cn.edu.neu.global;

public class ActionInfo {

	private String action;
	private boolean isCollected;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isCollected() {
		return isCollected;
	}
	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}
	public ActionInfo(String action, boolean isCollected) {
		this.action = action;
		this.isCollected = isCollected;
	}
}
