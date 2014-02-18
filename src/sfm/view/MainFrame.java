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
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");// Nimbus����³�������ۣ�jdk6

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(null);
		this.setResizable(false);
		// ���ó�����ʾ����Ļ�м�
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);
		/********************************************************
		 * ������Ϊjframe���ӿؼ�
		 ********************************************************/
		// �ļ�ѡ��ť
		fileUpdateButton = new JButton("�ϴ��ļ�");
		fileUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg;
				fDialog = new JFileChooser(); // �ļ�ѡ����
				fDialog.setDialogTitle("��ѡ��Ҫ�ϴ����ļ���");
				int result = fDialog.showOpenDialog(selFrame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fDialog.getSelectedFile();
					String fname = file.getName();
					String filePath = file.getAbsolutePath();
					String fileSize = Long.toString(file.length());	//�ֽ���
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
					String storeTime = df.format(new Date());
					String storeName = "file"+Long.toString((new Date()).getTime());
					//����һ���µ��ļ�����
					SafeFile newFile = new SafeFile(); 
					newFile.setFileName(fname);
					newFile.setFileSize(fileSize);
					newFile.setStoreTime(storeTime);
					newFile.setStoreName(storeName);
					controlCore.files.add(newFile);
					//�ϴ��ļ�
					controlCore.updateFile(filePath, storeName);
					//���沢�������ݵ�table
					try {
						controlCore.saveXmlData();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					dtm.addRow(new Object[] {fname,controlCore.fileSizeStringTraslate(fileSize),storeTime,storeName});
					//������ʾ
					msg = "�ļ��ϴ��ɹ�";
					JOptionPane.showMessageDialog(selFrame, msg); //��ʾ��
				}
			}
		});
		fileUpdateButton.setBounds(40, 260, 200, 100);
		this.add(fileUpdateButton);
		//�ļ����ذ�ť
		fileDowloadButton = new JButton("�ļ�����");
		fileDowloadButton.setBounds(90, 385, 100, 50);
		fileDowloadButton.addActionListener(new DowloadActionListener(this));
		this.add(fileDowloadButton);
		// ��� ������Ϊ���ɱ༭
		dtm = new DefaultTableModel(0, 4);
		fileTable = new JTable(dtm) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}; // ����
		fileTable.setEnabled(false);
		fileTable.setGridColor(Color.black);// ����������Ϊ��ɫ
		fileTable.setShowGrid(true); // ���������߿ɼ�
		fileTable.setBounds(0, 0, 500, 400);// ����table��С
		fileTable.setRowHeight(20); // �����и�
		//dtm = (DefaultTableModel) fileTable.getModel();
		Object[] columnTitle = { "�ļ��� ", "�ļ���С", "�洢����", "���ܺ��ļ���" };
		dtm.setColumnIdentifiers(columnTitle);// ������
		// ��TableModel�������ƺ��ġ����������µ���
		controlCore.setDtm(dtm);
		// �����п�
		fileTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		fileTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		fileTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		fileTable.getColumnModel().getColumn(3).setPreferredWidth(130);
		// dtm.addRow(new Object[] { "������", "", "Ů", "2012", "���" });
		JScrollPane jScrollPane = new JScrollPane(fileTable);
		jScrollPane.setBounds(280, 50, 500, 400);
		this.add(jScrollPane);
		controlCore.readXmlFileData();
		// ���ñ���
		ImageIcon i = new ImageIcon(getClass().getResource("/img/bg.jpg"));
		JLabel beijingtu = new JLabel(i);
		this.getLayeredPane().add(beijingtu, new Integer(Integer.MIN_VALUE));
		beijingtu.setBounds(0, 0, i.getIconWidth(), i.getIconHeight());
		((JPanel) this.getContentPane()).setOpaque(false);
		// ���ô��ڵ�icon
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
		Image logoImage = logoIcon.getImage();
		this.setIconImage(logoImage);
		/*********************************************************/
		this.setVisible(true);
	}

	// -----------------------------------------------------
	//���ذ�ť�ļ�����
	class DowloadActionListener implements ActionListener{
		MainFrame mainFrame;
		public DowloadActionListener(MainFrame mainFrame){
			this.mainFrame = mainFrame;
		}
		public void actionPerformed(ActionEvent e) {
			if(((JButton)e.getSource()).getText().equals("�ļ�����")){
				fileChooseFrame = new FileChooseFrame("�ļ�ѡ��");
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
