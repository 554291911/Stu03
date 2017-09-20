package cn.edu.xiyou.sys.core;

public class FunctionTreeView {
	private String id;
	private String text;
	private String icon;
	private boolean children=false;
	private StateView state;
	
	public StateView getState() {
		return state;
	}
	public void setState(StateView state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isChildren() {
		return children;
	}
	public void setChildren(boolean children) {
		this.children = children;
	}
}
