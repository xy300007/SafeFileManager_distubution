package sfm.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import sfm.control.ControlCore;
import sfm.model.SafeFile;

public class FileChooseFrame extends JFrame {
	int width = 400;
	int height = 180;
	private ControlCore controlCore;
	private MainFrame mainFrame;

	JLabel chooseJLabel;
	JComboBox<String> chooseFileJComboBox;
	JButton choosButton;
	JButton returnMainFrameButton;
	JFrame selFrame = this;

	FileChooseFrame(String title) {
		super(title);
	}

	void fileChooseFrameInit() {
		try {

			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");// Nimbus风格，新出来的外观，jdk6

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(null);
		this.setResizable(false);
		// 设置程序显示在屏幕中间
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);
		// 加控件
		chooseJLabel = new JLabel("请选择：");
		chooseJLabel.setBounds(40, 25, 50, 35);
		this.add(chooseJLabel);

		chooseFileJComboBox = new JComboBox();
		ArrayList<SafeFile> files = getControlCore().files;
		for (SafeFile safeFile : files) {
			chooseFileJComboBox.addItem(safeFile.getFileName());
		}
		chooseFileJComboBox.setBounds(110, 25, 250, 35);
		this.add(chooseFileJComboBox);

		choosButton = new JButton("保存到D盘根目录");
		choosButton.setBounds(80, 80, 180, 30);
		choosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((JButton) e.getSource()).getText().equals("保存到D盘根目录")) {
					int fileId = chooseFileJComboBox.getSelectedIndex();
					controlCore.donloadFile(fileId);
				}
			}
		});
		this.add(choosButton);
		returnMainFrameButton = new JButton("返回");
		returnMainFrameButton.setBounds(270, 80, 60, 30);
		returnMainFrameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setVisible(true);
				selFrame.setVisible(false);
			}
		});
		this.add(returnMainFrameButton);
		
		// 替换窗口的icon
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
		Image logoImage = logoIcon.getImage();
		this.setIconImage(logoImage);
		this.setVisible(true);

	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public ControlCore getControlCore() {
		return controlCore;
	}

	public void setControlCore(ControlCore controlCore) {
		this.controlCore = controlCore;
	}

}
