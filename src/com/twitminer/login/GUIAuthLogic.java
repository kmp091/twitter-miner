package com.twitminer.login;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import twitter4j.auth.RequestToken;

import com.twitminer.gui.TwitterAuth;

public class GUIAuthLogic extends AuthLogic implements ClipboardOwner {

	TwitterAuth authDialog;
	
	public GUIAuthLogic (RequestToken reqToken) {
		authDialog = new TwitterAuth();
		generateUrl(reqToken);
		assignUserCredentials();
		handleAuthButton();
		authDialog.setModal(true);
		authDialog.setVisible(true);
	}
	
	private void generateUrl(final RequestToken reqToken) {
		authDialog.getAccessTwitterButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				
				try {
					desktop.browse(getAuthenticationURI(reqToken));
					//authDialog.getSecondStepPanel().setVisible(true);
					//authDialog.getThirdStepPanel().setVisible(true);
					//authDialog.getFourthStepPanel().setVisible(true);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		});
	}
	
	private void assignUserCredentials() {
		final String username = "TheDataDummy";
		final String password = "iEatAlgosForBreakfast";
		
		authDialog.getCopyUsernameButton().addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				setClipboardContents(username);
			}
			
		});
		authDialog.getCopyPasswordButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setClipboardContents(password);
			}
			
		});
	}
	
	private void handleAuthButton() {
		authDialog.getAuthButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAuthenticationCode(authDialog.getTokenField().getText());
				authDialog.dispose();				
			}
			
		});
	}
	
	private void setClipboardContents (String content) {
		StringSelection selection = new StringSelection(content);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, this);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}
	
}
