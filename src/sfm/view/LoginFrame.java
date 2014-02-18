package sfm.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;
import sfm.control.ControlCore;

public class LoginFrame extends JFrame{
	int width = 400;
	int height = 300;
	ControlCore controlCore = new ControlCore();
	LoginFrame(String title){
		super(title);
	}
	JLabel userNameLabel;
	JLabel userPasswordLabel;
	JTextField userNameText;
	JPasswordField userPassword;
	JButton loginButton;
	JButton resetButton;
	void frameInits(){	
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
		//���ó�����ʾ����Ļ�м�
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
		//�ı���
		userNameLabel = new JLabel("�û�����");
		userPasswordLabel = new JLabel("���ģ�    ");
		userNameLabel.setBounds(110, 120, 60, 30);
		userPasswordLabel.setBounds(110, 170, 60, 30);
		this.add(userNameLabel);
		this.add(userPasswordLabel);
		//�����
		userNameText = new JTextField(50);
		userPassword = new JPasswordField(50);
		userNameText.setBounds(170, 120, 100, 25);
		userPassword.setBounds(170, 170, 100, 25);
		this.add(userNameText);
		this.add(userPassword);
		//��ͼƬ
		//ImageIcon ii = new ImageIcon("src/sfm/view/login_bg.jpg");
		ImageIcon ii = new ImageIcon(getClass().getResource("/img/login_bg.jpg"));
		JLabel lb = new JLabel(ii);
		lb.setBounds(0, 0, 400, 110);
		this.add(lb);
		//��ť
		loginButton = new JButton("��½");
		resetButton = new JButton("���");
		loginButton.setBounds(100, 220, 80, 25);
		resetButton.setBounds(200, 220, 80, 25);
		this.add(loginButton);
		this.add(resetButton);
		//����ť���¼�
		ResetActionListener testActionListener = new ResetActionListener(userNameText,userPassword);
		resetButton.addActionListener(testActionListener);
		LoginActionListener loginActionListener = new LoginActionListener(this);
		loginButton.addActionListener(loginActionListener);
		//�滻���ڵ�icon
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
		Image logoImage = logoIcon.getImage();
		this.setIconImage(logoImage);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		String firstTitle = "��ȫ�ļ�������  ���ߣ����";
		LoginFrame loginFrame = new LoginFrame(firstTitle);
		loginFrame.frameInits();
	}
	
//-----------------------------------------------------
//��������ļ�����
	class ResetActionListener implements ActionListener{
		JTextField userName;
		JPasswordField password;
		public ResetActionListener(JTextField userName,JPasswordField password){
			this.userName = userName;
			this.password = password;
		}
		public void actionPerformed(ActionEvent e) {
			if(((JButton)e.getSource()).getText().equals("���")){
				userName.setText(null);
				password.setText(null);
			}
		}
	}
//-----------------------------------------------------
//��½��ļ�����
	class LoginActionListener implements ActionListener{
		LoginFrame loginFrame;
		JTextField userName;
		JPasswordField password;
		String  uname;
		String 	pw;
		public LoginActionListener(LoginFrame loginFrame){
			this.userName = loginFrame.userNameText;
			this.password = loginFrame.userPassword;
			this.loginFrame = loginFrame;
		}
		public void actionPerformed(ActionEvent e) {
			if(((JButton)e.getSource()).getText().equals("��½")){
				pw = new String(password.getPassword());
				uname = userName.getText();
				if("".equals(uname) && "".equals(pw)){
					JOptionPane.showMessageDialog(null, "�û������Ķ�Ϊ��", "��½����", JOptionPane.INFORMATION_MESSAGE);
				}else{
					int result = controlCore.loginJudge(uname,pw);
					if(result == 1){
						//����mainframe
						MainFrame mainFrame = new MainFrame("��ȫ�ļ�������  ���ߣ����");
						mainFrame.setControlCore(controlCore);
						mainFrame.frameInits();
						loginFrame.setVisible(false);
					}else{
						JOptionPane.showMessageDialog(null, "�˻���Ϣ�������", "�������", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	
}
