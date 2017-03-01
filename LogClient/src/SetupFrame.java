import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SetupFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	private SetupFrame setupFrame;

	/**
	 * Create the frame.
	 */
	public SetupFrame()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 110);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setupFrame = this;
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String temp[] = new String[2];
				temp[0] = textField.getText();
				temp[1] = textField_1.getText();
				Client c = new Client(temp);
				c.start(c);
				setupFrame.dispose();
			}
		});
		contentPane.add(btnApply, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(0, 1, 0, 1));
		contentPane.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.WEST);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblAdresIp = new JLabel("Adres IP: ");
		panel.add(lblAdresIp);
		
		JLabel lblPort = new JLabel("Port: ");
		panel.add(lblPort);
		
		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		panel_1.add(textField_1);
		textField_1.setColumns(10);
	}

}
