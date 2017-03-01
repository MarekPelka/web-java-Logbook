import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ClientFrame
{

	JFrame						frmLogclient;
	private Client				client;
	private JPanel				pnlTypeOfSendingLog;
	private JPanel				pnlMain;
	private JLabel				lblWybierzLiczbKolumn;
	private JSpinner			spinner;
	private JPanel				pnlCommit;
	private JScrollPane			scrollPane;
	private JPanel				pnlRows;
	private JLabel				lblData;
	private JTextField			txtData;
	private JLabel				label;
	private JLabel				label_1;
	private JTextField			txtAdress;
	private JTextField			txtPri;
	private JButton				btnCommit;

	private List<JLabel>		infoNames;
	private List<JTextField>	infoValiues;
	private JPanel				pnlVievport;
	private JButton				btnSendLog;
	private JRadioButton		rdbtnCzasowe;
	private JRadioButton		rdbtnRczne;
	private JRadioButton		rdbtnHybrydowe;
	private JPanel				pnlRdbChosser;
	private JButton				btnRozpocznij;
	private JRadioButton		rdbtnKeylogger;
	private final JSpinner		spinner_1	= new JSpinner();
	private JLabel				lblMilisekundy;
	private ButtonGroup			group;
	private Component			horizontalStrut;
	private JLabel				lblNazwaSessji;

	/**
	 * Create the application.
	 */
	public ClientFrame(Client c)
	{
		client = c;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmLogclient = new JFrame();
		frmLogclient.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				client.addTrayListener();
			}
		});
		BorderLayout borderLayout = (BorderLayout) frmLogclient.getContentPane().getLayout();
		borderLayout.setVgap(5);
		borderLayout.setHgap(5);
		frmLogclient.setTitle("LogClient");
		frmLogclient.setBounds(100, 100, 450, 300);
		frmLogclient.setMinimumSize(new Dimension(370, 210));

		pnlTypeOfSendingLog = new JPanel();
		frmLogclient.getContentPane().add(pnlTypeOfSendingLog, BorderLayout.EAST);
		pnlTypeOfSendingLog.setLayout(new BorderLayout(5, 5));

		pnlRdbChosser = new JPanel();
		pnlTypeOfSendingLog.add(pnlRdbChosser, BorderLayout.NORTH);
		pnlRdbChosser.setLayout(new GridLayout(0, 1, 5, 0));

		rdbtnHybrydowe = new JRadioButton("Hybrydowe");
		rdbtnHybrydowe.setActionCommand("Hybrydowe");
		rdbtnHybrydowe.setEnabled(false);
		pnlRdbChosser.add(rdbtnHybrydowe);

		rdbtnRczne = new JRadioButton("R\u0119czne");
		rdbtnRczne.setActionCommand("R\u0119czne");
		rdbtnRczne.setEnabled(false);
		pnlRdbChosser.add(rdbtnRczne);

		rdbtnCzasowe = new JRadioButton("Czasowe");
		rdbtnCzasowe.setActionCommand("Czasowe");
		rdbtnCzasowe.setEnabled(false);
		pnlRdbChosser.add(rdbtnCzasowe);

		rdbtnKeylogger = new JRadioButton("KeyLogger");
		rdbtnKeylogger.setActionCommand("KeyLogger");
		rdbtnKeylogger.setEnabled(false);
		pnlRdbChosser.add(rdbtnKeylogger);

		group = new ButtonGroup();
		group.add(rdbtnHybrydowe);
		group.add(rdbtnRczne);
		group.add(rdbtnCzasowe);
		group.add(rdbtnKeylogger);

		lblMilisekundy = new JLabel("Milisekundy:");
		lblMilisekundy.setEnabled(false);
		pnlRdbChosser.add(lblMilisekundy);
		spinner_1.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner_1.setEnabled(false);
		pnlRdbChosser.add(spinner_1);

		ActionListener aRdbtnChange = new ActionListener()
		{
			public void actionPerformed(ActionEvent b)
			{
				if ("Hybrydowe".equals(b.getActionCommand()))
				{
					lblMilisekundy.setEnabled(true);
					spinner_1.setEnabled(true);
				} else if ("R\u0119czne".equals(b.getActionCommand()))
				{
					lblMilisekundy.setEnabled(false);
					spinner_1.setEnabled(false);
				} else if ("Czasowe".equals(b.getActionCommand()))
				{
					lblMilisekundy.setEnabled(true);
					spinner_1.setEnabled(true);
				} else if ("KeyLogger".equals(b.getActionCommand()))
				{
					lblMilisekundy.setEnabled(false);
					spinner_1.setEnabled(false);
				}
			}
		};

		rdbtnHybrydowe.addActionListener(aRdbtnChange);
		rdbtnRczne.addActionListener(aRdbtnChange);
		rdbtnCzasowe.addActionListener(aRdbtnChange);
		rdbtnKeylogger.addActionListener(aRdbtnChange);

		btnRozpocznij = new JButton("Rozpocznij");
		btnRozpocznij.setEnabled(false);
		btnRozpocznij.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				if ("Rozpocznij".equals(a.getActionCommand()))
				{
					if ("Hybrydowe".equals(group.getSelection().getActionCommand()))
					{
						btnSendLog.setEnabled(true);
						lblMilisekundy.setEnabled(false);
						spinner_1.setEnabled(false);
						client.startSendingHybridLog(Integer.toUnsignedLong((int) spinner_1.getValue()));
					} else if ("R\u0119czne".equals(group.getSelection().getActionCommand()))
					{
						btnSendLog.setEnabled(true);
					} else if ("Czasowe".equals(group.getSelection().getActionCommand()))
					{
						lblMilisekundy.setEnabled(false);
						spinner_1.setEnabled(false);
						client.startSendingTimeLog(Integer.toUnsignedLong((int) spinner_1.getValue()));
					} else if ("KeyLogger".equals(group.getSelection().getActionCommand()))
					{
						client.startSendingKeybordHook();
					}
					btnRozpocznij.setText(" Zatrzymaj ");
					return;
				} else
					client.stopSending();
				btnSendLog.setEnabled(false);
				btnRozpocznij.setText("Rozpocznij");
				return;
			}
		});
		pnlTypeOfSendingLog.add(btnRozpocznij, BorderLayout.SOUTH);

		horizontalStrut = Box.createHorizontalStrut(90);
		pnlTypeOfSendingLog.add(horizontalStrut, BorderLayout.CENTER);

		pnlMain = new JPanel();
		frmLogclient.getContentPane().add(pnlMain, BorderLayout.CENTER);
		pnlMain.setLayout(new BorderLayout(5, 5));

		pnlCommit = new JPanel();
		pnlCommit.setBorder(new EmptyBorder(0, 5, 0, 0));
		pnlMain.add(pnlCommit, BorderLayout.NORTH);
		pnlCommit.setLayout(new BorderLayout(5, 5));

		lblWybierzLiczbKolumn = new JLabel("Wybierz liczb\u0119 kolumn:");
		lblWybierzLiczbKolumn.setHorizontalAlignment(SwingConstants.CENTER);
		pnlCommit.add(lblWybierzLiczbKolumn, BorderLayout.WEST);

		spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				addField((Integer) spinner.getValue());
			}
		});
		pnlCommit.add(spinner, BorderLayout.CENTER);
		spinner.setModel(new SpinnerNumberModel(new Integer(3), new Integer(3), null, new Integer(1)));

		btnCommit = new JButton("Commit");
		btnCommit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int isAllSet = 1;
				for (JTextField t : infoValiues)
				{
					if (t.getText().equals(""))
						isAllSet = 0;
				}
				if (isAllSet > 0)
				{
					spinner.setEnabled(false);
					btnCommit.setEnabled(false);
					txtData.setEnabled(true);
					txtAdress.setEnabled(true);
					txtPri.setEnabled(true);
					txtPri.setEditable(true);
					List<String> databaseNames = new ArrayList<String>();
					for (int i = 0; i < infoValiues.size(); i++)
					{
						String temp = infoValiues.get(i).getText();
						infoNames.get(i).setText(temp);
						databaseNames.add(temp.replace(" ", "_"));
						infoValiues.get(i).setText("");
					}
					String tmp;
					if (txtAdress.getText().equals(""))
						tmp = "Default";
					else
						tmp = txtAdress.getText();
					client.commit(databaseNames, tmp);
					label.setText("Adres");
					txtAdress.setEnabled(false);
					txtAdress.setEditable(false);
					txtPri.setText("0");
					rdbtnHybrydowe.setEnabled(true);
					rdbtnRczne.setEnabled(true);
					rdbtnRczne.setSelected(true);
					rdbtnCzasowe.setEnabled(true);
					rdbtnKeylogger.setEnabled(true);
					btnRozpocznij.setEnabled(true);
					txtAdress.setText(client.getInetAdress().toString());
				}
			}
		});
		pnlCommit.add(btnCommit, BorderLayout.EAST);

		lblNazwaSessji = new JLabel("Nazwa sesji: ");
		pnlCommit.add(lblNazwaSessji, BorderLayout.SOUTH);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		pnlMain.add(scrollPane, BorderLayout.CENTER);

		pnlVievport = new JPanel();
		pnlVievport.setLayout(new BorderLayout(0, 0));

		scrollPane.setViewportView(pnlVievport);

		pnlRows = new JPanel();

		pnlVievport.add(pnlRows, BorderLayout.NORTH);

		GridBagLayout gbl_pnlRows = new GridBagLayout();

		gbl_pnlRows.columnWeights = new double[] { 0.0, 1.0 };
		gbl_pnlRows.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		pnlRows.setLayout(gbl_pnlRows);

		lblData = new JLabel("Data");
		lblData.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblData = new GridBagConstraints();
		gbc_lblData.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
		gbc_lblData.insets = new Insets(0, 5, 5, 5);
		gbc_lblData.gridx = 0;
		gbc_lblData.gridy = 0;
		pnlRows.add(lblData, gbc_lblData);

		txtData = new JTextField();
		txtData.setEnabled(false);
		txtData.setEditable(false);
		GridBagConstraints gbc_txtData = new GridBagConstraints();
		gbc_txtData.anchor = GridBagConstraints.NORTH;
		gbc_txtData.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtData.insets = new Insets(0, 0, 5, 5);
		gbc_txtData.gridx = 1;
		gbc_txtData.gridy = 0;
		pnlRows.add(txtData, gbc_txtData);
		txtData.setColumns(10);

		final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		ActionListener timerListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String time = timeFormat.format(Calendar.getInstance().getTime());
				txtData.setText(time);
				if (!btnCommit.isEnabled())
					client.hartbeat();
			}
		};
		Timer timer = new Timer(1000, timerListener);
		// to make sure it doesn't wait one second at the start
		timer.setInitialDelay(0);
		timer.start();

		label = new JLabel("Typ");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		pnlRows.add(label, gbc_label);

		txtAdress = new JTextField();
		txtAdress.setColumns(10);
		GridBagConstraints gbc_txtAdress = new GridBagConstraints();
		gbc_txtAdress.anchor = GridBagConstraints.NORTH;
		gbc_txtAdress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAdress.insets = new Insets(0, 0, 5, 5);
		gbc_txtAdress.gridx = 1;
		gbc_txtAdress.gridy = 1;
		pnlRows.add(txtAdress, gbc_txtAdress);

		label_1 = new JLabel("Prioritet");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 5, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		pnlRows.add(label_1, gbc_label_1);

		txtPri = new JTextField();
		txtPri.setEditable(false);
		txtPri.setEnabled(false);
		txtPri.setColumns(10);
		GridBagConstraints gbc_txtPri = new GridBagConstraints();
		gbc_txtPri.insets = new Insets(0, 0, 5, 5);
		gbc_txtPri.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPri.anchor = GridBagConstraints.NORTH;
		gbc_txtPri.gridx = 1;
		gbc_txtPri.gridy = 2;
		pnlRows.add(txtPri, gbc_txtPri);

		btnSendLog = new JButton("Wyslij log");
		btnSendLog.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				List<String> info = new ArrayList<String>();
				for (JTextField temp : infoValiues)
				{
					info.add(temp.getText());
				}
				client.sendManualLog(Double.parseDouble(txtPri.getText()), info);
			}
		});
		btnSendLog.setEnabled(false);
		pnlMain.add(btnSendLog, BorderLayout.SOUTH);

		infoNames = new ArrayList<JLabel>();
		infoValiues = new ArrayList<JTextField>();
	}

	public void setSessionName(String name)
	{
		lblNazwaSessji.setText(lblNazwaSessji.getText() + " " + name);
	}

	public void addField(int fieldNumber)
	{
		if (!infoNames.isEmpty() && !infoValiues.isEmpty())
		{
			infoNames.clear();
			infoValiues.clear();
			pnlRows.removeAll();

			GridBagLayout gbl_panel_3 = new GridBagLayout();
			gbl_panel_3.columnWeights = new double[] { 0.0, 1.0 };
			gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, 0.0 };
			pnlRows.setLayout(gbl_panel_3);

			lblData = new JLabel("Data");
			lblData.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_lblData = new GridBagConstraints();
			gbc_lblData.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
			gbc_lblData.insets = new Insets(0, 5, 5, 5);
			gbc_lblData.gridx = 0;
			gbc_lblData.gridy = 0;
			pnlRows.add(lblData, gbc_lblData);

			txtData = new JTextField();
			txtData.setColumns(10);
			txtData.setEditable(false);
			;
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.anchor = GridBagConstraints.NORTH;
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 0;
			pnlRows.add(txtData, gbc_textField);

			label = new JLabel("Typ");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.anchor = GridBagConstraints.EAST;
			gbc_label.insets = new Insets(0, 5, 5, 5);
			gbc_label.gridx = 0;
			gbc_label.gridy = 1;
			pnlRows.add(label, gbc_label);

			txtAdress = new JTextField();
			txtAdress.setColumns(10);
			GridBagConstraints gbc_textField_1 = new GridBagConstraints();
			gbc_textField_1.anchor = GridBagConstraints.NORTH;
			gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_1.insets = new Insets(0, 0, 5, 5);
			gbc_textField_1.gridx = 1;
			gbc_textField_1.gridy = 1;
			pnlRows.add(txtAdress, gbc_textField_1);

			label_1 = new JLabel("Prioritet");
			label_1.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_label_1 = new GridBagConstraints();
			gbc_label_1.anchor = GridBagConstraints.EAST;
			gbc_label_1.insets = new Insets(0, 5, 5, 5);
			gbc_label_1.gridx = 0;
			gbc_label_1.gridy = 2;
			pnlRows.add(label_1, gbc_label_1);

			txtPri = new JTextField();
			txtPri.setEditable(false);
			txtPri.setColumns(10);
			GridBagConstraints gbc_textField_2 = new GridBagConstraints();
			gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_2.anchor = GridBagConstraints.NORTH;
			gbc_textField_2.insets = new Insets(0, 0, 5, 5);
			gbc_textField_2.gridx = 1;
			gbc_textField_2.gridy = 2;
			pnlRows.add(txtPri, gbc_textField_2);

			pnlRows.validate();
			pnlRows.repaint();
		}

		for (int i = 0; i < fieldNumber - 3; i++)
		{
			infoNames.add(new JLabel("Nadaj nazwe"));
			GridBagConstraints gbc_label_1 = new GridBagConstraints();
			gbc_label_1.anchor = GridBagConstraints.EAST;
			gbc_label_1.insets = new Insets(0, 5, 5, 5);
			gbc_label_1.gridx = 0;
			gbc_label_1.gridy = i + 3;
			pnlRows.add(infoNames.get(i), gbc_label_1);

			infoValiues.add(new JTextField());
			infoValiues.get(i).setEditable(true);
			infoValiues.get(i).setColumns(10);
			GridBagConstraints gbc_textField_2 = new GridBagConstraints();
			gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_2.anchor = GridBagConstraints.NORTH;
			gbc_textField_2.insets = new Insets(0, 0, 5, 5);
			gbc_textField_2.gridx = 1;
			gbc_textField_2.gridy = i + 3;
			pnlRows.add(infoValiues.get(i), gbc_textField_2);

		}
		scrollPane.validate();
		scrollPane.repaint();
	}

	public List<String> getList()
	{
		//TODO: Remember: Lista.stream().map(i->i.gettext()).college(Collectors.toList())
		ArrayList<String> temp = new ArrayList<String>();
		for (JLabel l : infoNames)
			temp.add(l.getText());
		return temp;
	}

}
