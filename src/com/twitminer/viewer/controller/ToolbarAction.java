package com.twitminer.viewer.controller;

import javax.swing.AbstractAction;
import javax.swing.Icon;

public abstract class ToolbarAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6580744566405419891L;

	public ToolbarAction() {
		super();
	}

	public ToolbarAction(String name) {
		super(name);
		this.initialize();
		// TODO Auto-generated constructor stub
	}

	public ToolbarAction(String name, Icon icon) {
		super(name, icon);
		this.initialize();
		// TODO Auto-generated constructor stub
	}
	
	public ToolbarAction(String name, Icon icon, String tooltipText) {
		this(name, icon);
		setTooltip(tooltipText);
	}
	
	public ToolbarAction(String name, Icon icon, String tooltipText, int mnemonic) {
		this(name, icon, tooltipText);
		setMnemonic(mnemonic);
	}
	
	protected void initialize() {
		//do nothing
	}
	
	public void setTooltip(String tooltipText) {
		putValue(SHORT_DESCRIPTION, tooltipText);
	}
		
	public void setMnemonic(int mnemonic) {
		putValue(MNEMONIC_KEY, mnemonic);
	}
	
	public void setIcon(Icon icon) {
		putValue(LARGE_ICON_KEY, icon);
	}
	
	public void setName(String name) {
		putValue(NAME, name);
	}
	
	public String getTooltip() {
		return (String)getValue(SHORT_DESCRIPTION);
	}
	
	public String getName() {
		return (String)getValue(NAME);
	}
	
	public int getMnemonic() {
		return (Integer)getValue(MNEMONIC_KEY);
	}
	
	public Icon getIcon() {
		return (Icon)getValue(LARGE_ICON_KEY);
	}

}
