package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import core.XMLConverter;

public class Main extends JFrame implements ActionListener {

	private static JTextArea statusField;
	private JTextField tf;

	private JFileChooser jfc = new JFileChooser();
	private String workdir = "";
	private JButton browseBtn, runBtn;

	public Main() {
		super("XMLtoEXCEL");
		init();
		this.setSize(400, 200);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void init() {
		setLayout(null);
		JLabel lb = new JLabel("Working Directory : ");
		lb.setBounds(7, 10, 120, 20);
		tf = new JTextField();
		tf.setBounds(130, 10, 230, 20);
		tf.setEditable(false);
		browseBtn = new JButton("...");
		browseBtn.setBounds(360, 10, 30, 20);
		browseBtn.setActionCommand("BROWSE");
		browseBtn.addActionListener(this);

		statusField = new JTextArea();
		statusField.setEditable(false);
		statusField.setBounds(7, 40, 380, 100);

		runBtn = new JButton("Run");
		runBtn.setBounds(160, 140, 60, 30);
		runBtn.setActionCommand("RUN");
		runBtn.addActionListener(this);

		jfc.setDialogTitle("경로를 선택해주세요.");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);

		this.add(lb);
		this.add(tf);
		this.add(browseBtn);
		this.add(statusField);
		this.add(runBtn);
	}

	public static void logln(String str) {
		statusField.append(str + "\n");
	}

	public static void log(String str) {
		statusField.append(str);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("BROWSE")) {
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				tf.setText(jfc.getSelectedFile().toString());
				String osname = System.getProperty("os.name").toLowerCase();
				workdir = jfc.getSelectedFile().getAbsolutePath();
				if(osname.contains("win")){
					
					workdir = workdir.replace("\\", "\\\\");
					workdir += "\\\\";
				}
				else{
					workdir += "/";
				}
//				try {
//					workdir = URLEncoder.encode(workdir,"UTF-8");
//				} catch (UnsupportedEncodingException e1) {
//					e1.printStackTrace();
//				}
				System.out.println(workdir);
				
				//System.out.println(workdir);
			}
		} else {
			runBtn.setEnabled(false);
			browseBtn.setEnabled(false);
			new Thread() {
				public void run() {
					XMLConverter cvt = new XMLConverter(workdir);
					cvt.run();
					runBtn.setEnabled(true);
					browseBtn.setEnabled(true);
				}
			}.start();
		}
	}

	public static void main(String args[]) {
		new Main();
	}

}
