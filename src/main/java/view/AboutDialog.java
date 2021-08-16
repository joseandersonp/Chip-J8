package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public AboutDialog() {

		setTitle("About");
		setBounds(100, 100, 258, 168);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel chip8Label = new JLabel("JCHIP-8");
		chip8Label.setBounds(10, 11, 222, 25);
		chip8Label.setFont(new Font(chip8Label.getFont().getName(), Font.PLAIN, 20));
		contentPanel.add(chip8Label);

		JLabel versionLabel = new JLabel("Version: 0.1-BETA");
		versionLabel.setBounds(20, 40, 212, 14);
		contentPanel.add(versionLabel);

		JLabel buildLabel = new JLabel("Build Date: 08/05/2021");
		buildLabel.setBounds(20, 55, 212, 14);
		contentPanel.add(buildLabel);

		JLabel creatorLabel = new JLabel("By J.Anderson (aka Joapeer)");
		creatorLabel.setFont(new Font(creatorLabel.getFont().getName(), Font.PLAIN, 12));
		creatorLabel.setBounds(20, 75, 212, 14);
		contentPanel.add(creatorLabel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton closeButton = new JButton("Close");
		buttonPane.add(closeButton);
		closeButton.addActionListener(e -> {
			setVisible(false);
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}
}
