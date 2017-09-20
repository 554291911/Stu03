package cn.edu.xiyou.sys.core;

public class StateView {
	private boolean opened = true;
	private boolean selected =false;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
}
