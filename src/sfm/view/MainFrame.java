package sfm.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sfm.control.ControlCore;
import sfm.model.SafeFile;

public class MainFrame extends JFrame {
	int width = 800;
	int height = 550;
	ControlCore controlCore;

	MainFrame(String title) {
		super(title);
	}

	public ControlCore getControlCore() {
		return controlCore;
	}

	public void setControlCore(ControlCore controlCore) {
		this.controlCore = controlCore;
	}

	JTable fileTable;
	DefaultTableModel dtm;

	JButton fileUpdateButton;
	JButton fileDowloadButton;
	JFileChooser fDialog;
	FileChooseFrame fileChooseFrame;
	JFrame selFrame = this;
	void frameInits() {
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
		/********************************************************
		 * 接下来为jframe增加控件
		 ********************************************************/
		// 文件选择按钮
		fileUpdateButton = new JButton("上传文件");
		fileUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg;
				fDialog = new JFileChooser(); // 文件选择器
				fDialog.setDialogTitle("请选择要上传的文件：");
				int result = fDialog.showOpenDialog(selFrame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fDialog.getSelectedFile();
					String fname = file.getName();
					String filePath = file.getAbsolutePath();
					String fileSize = Long.toString(file.length());	//字节数
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					String storeTime = df.format(new Date());
					String storeName = "file"+Long.toString((new Date()).getTime());
					//生成一个新的文件对象
					SafeFile newFile = new SafeFile(); 
					newFile.setFileName(fname);
					newFile.setFileSize(fileSize);
					newFile.setStoreTime(storeTime);
					newFile.setStoreName(storeName);
					controlCore.files.add(newFile);
					//上传文件
					controlCore.updateFile(filePath, storeName);
					//保存并载入数据到table
					try {
						controlCore.saveXmlData();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					dtm.addRow(new Object[] {fname,controlCore.fileSizeStringTraslate(fileSize),storeTime,storeName});
					//给个提示
					msg = "文件上传成功";
					JOptionPane.showMessageDialog(selFrame, msg); //提示框
				}
			}
		});
		fileUpdateButton.setBounds(40, 260, 200, 100);
		this.add(fileUpdateButton);
		//文件下载按钮
		fileDowloadButton = new JButton("文件下载");
		fileDowloadButton.setBounds(90, 385, 100, 50);
		fileDowloadButton.addActionListener(new DowloadActionListener(this));
		this.add(fileDowloadButton);
		// 表格 且设置为不可编辑
		dtm = new DefaultTableModel(0, 4);
		fileTable = new JTable(dtm) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}; // 生成
		fileTable.setEnabled(false);
		fileTable.setGridColor(Color.black);// 设置网格线为黑色
		fileTable.setShowGrid(true); // 设置网格线可见
		fileTable.setBounds(0, 0, 500, 400);// 设置table大小
		fileTable.setRowHeight(20); // 设置行高
		//dtm = (DefaultTableModel) fileTable.getModel();
		Object[] columnTitle = { "文件名 ", "文件大小", "存储日期", "加密后文件名" };
		dtm.setColumnIdentifiers(columnTitle);// 设置列
		// 将TableModel传给控制核心。用来增加新的行
		controlCore.setDtm(dtm);
		// 设置列宽
		fileTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		fileTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		fileTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(130);
		// dtm.addRow(new Object[] { "李清照", "", "女", "2012", "你好" });
		JScrollPane jScrollPane = new JScrollPane(fileTable);
		jScrollPane.setBounds(280, 50, 500, 400);
		this.add(jScrollPane);
		controlCore.readXmlFileData();
		// 设置背景
		ImageIcon i = new ImageIcon(getClass().getResource("/img/bg.jpg"));
		JLabel beijingtu = new JLabel(i);
		this.getLayeredPane().add(beijingtu, new Integer(Integer.MIN_VALUE));
		beijingtu.setBounds(0, 0, i.getIconWidth(), i.getIconHeight());
		((JPanel) this.getContentPane()).setOpaque(false);
		// 设置窗口的icon
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
		Image logoImage = logoIcon.getImage();
		this.setIconImage(logoImage);
		/*********************************************************/
		this.setVisible(true);
	}

	// -----------------------------------------------------
	//下载按钮的监听类
	class DowloadActionListener implements ActionListener{
		MainFrame mainFrame;
		public DowloadActionListener(MainFrame mainFrame){
			this.mainFrame = mainFrame;
		}
		public void actionPerformed(ActionEvent e) {
			if(((JButton)e.getSource()).getText().equals("文件下载")){
				fileChooseFrame = new FileChooseFrame("文件选择");
				fileChooseFrame.setControlCore(controlCore);
				fileChooseFrame.setMainFrame(mainFrame);
				fileChooseFrame.fileChooseFrameInit();
				mainFrame.setVisible(false);
			}
		}
	}
	public static void alertMsg(String msgTitle,String msgContent){
		JOptionPane.showMessageDialog(null, msgContent, msgTitle, JOptionPane.INFORMATION_MESSAGE);
	}
}
