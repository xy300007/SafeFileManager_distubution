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
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");// Nimbus风格，新出来的外观，jdk6

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(null);
		this.setResizable(false);
		//设置程序显示在屏幕中间
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
		//文本域
		userNameLabel = new JLabel("用户名：");
		userPasswordLabel = new JLabel("密文：    ");
		userNameLabel.setBounds(110, 120, 60, 30);
		userPasswordLabel.setBounds(110, 170, 60, 30);
		this.add(userNameLabel);
		this.add(userPasswordLabel);
		//输入框
		userNameText = new JTextField(50);
		userPassword = new JPasswordField(50);
		userNameText.setBounds(170, 120, 100, 25);
		userPassword.setBounds(170, 170, 100, 25);
		this.add(userNameText);
		this.add(userPassword);
		//加图片
		//ImageIcon ii = new ImageIcon("src/sfm/view/login_bg.jpg");
		ImageIcon ii = new ImageIcon(getClass().getResource("/img/login_bg.jpg"));
		JLabel lb = new JLabel(ii);
		lb.setBounds(0, 0, 400, 110);
		this.add(lb);
		//按钮
		loginButton = new JButton("登陆");
		resetButton = new JButton("清空");
		loginButton.setBounds(100, 220, 80, 25);
		resetButton.setBounds(200, 220, 80, 25);
		this.add(loginButton);
		this.add(resetButton);
		//给按钮加事件
		ResetActionListener testActionListener = new ResetActionListener(userNameText,userPassword);
		resetButton.addActionListener(testActionListener);
		LoginActionListener loginActionListener = new LoginActionListener(this);
		loginButton.addActionListener(loginActionListener);
		//替换窗口的icon
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
		Image logoImage = logoIcon.getImage();
		this.setIconImage(logoImage);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		String firstTitle = "安全文件管理器  作者：杨昕";
		LoginFrame loginFrame = new LoginFrame(firstTitle);
		loginFrame.frameInits();
	}
	
//-----------------------------------------------------
//清空输入框的监听类
	class ResetActionListener implements ActionListener{
		JTextField userName;
		JPasswordField password;
		public ResetActionListener(JTextField userName,JPasswordField password){
			this.userName = userName;
			this.password = password;
		}
		public void actionPerformed(ActionEvent e) {
			if(((JButton)e.getSource()).getText().equals("清空")){
				userName.setText(null);
				password.setText(null);
			}
		}
	}
//-----------------------------------------------------
//登陆框的监听类
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
			if(((JButton)e.getSource()).getText().equals("登陆")){
				pw = new String(password.getPassword());
				uname = userName.getText();
				if("".equals(uname) && "".equals(pw)){
					JOptionPane.showMessageDialog(null, "用户名密文都为空", "登陆出错", JOptionPane.INFORMATION_MESSAGE);
				}else{
					int result = controlCore.loginJudge(uname,pw);
					if(result == 1){
						//跳入mainframe
						MainFrame mainFrame = new MainFrame("安全文件管理器  作者：杨昕");
						mainFrame.setControlCore(controlCore);
						mainFrame.frameInits();
						loginFrame.setVisible(false);
					}else{
						JOptionPane.showMessageDialog(null, "账户信息输入错误", "登入出错", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	
}
