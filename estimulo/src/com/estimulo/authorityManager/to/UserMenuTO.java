package com.estimulo.authorityManager.to;

public class UserMenuTO {
	private int no;
	private int menuLevel;
	private int menuOrder;
	private String menuName;
	private int leaf;
	private String url;
	private String isAccessDenied;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getLeaf() {
		return leaf;
	}

	public void setLeaf(int leaf) {
		this.leaf = leaf;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsAccessDenied() {
		return isAccessDenied;
	}

	public void setIsAccessDenied(String isAccessDenied) {
		this.isAccessDenied = isAccessDenied;
	}
}
