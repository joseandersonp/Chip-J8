package app;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import view.ChipJ8UI;

public class App {

public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		new ChipJ8UI();
	}
	
}
