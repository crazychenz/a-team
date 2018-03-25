package clueless;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class CLI_GUI extends JFrame {

	private JPanel contentPane;
	JTextArea textArea = new JTextArea(5,5);
	JPanel panel = new JPanel();
	JLabel label;
	private final JTextField textField = new JTextField();
	private final JPanel panel_1 = new JPanel();
	private final JButton btnEnter = new JButton("enter");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CLI_GUI frame = new CLI_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CLI_GUI() {
		textField.setColumns(10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		textArea.setEditable(false);
		contentPane.add(textArea, BorderLayout.SOUTH);		
		panel.setPreferredSize(new Dimension(3,3));
		contentPane.add(panel, BorderLayout.EAST);
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		panel_1.add(textField);
		
		contentPane.add(panel_1, BorderLayout.WEST);
		
		panel_1.add(btnEnter);
		//panel.add(), arg1);
		
	}
	
	
	public void textArea1(String text) {
        System.out.print(text);
        textArea.append(text); // this is not appending to textarea.
    }
	

	public void DisplayImage(String url) {
		// TODO Auto-generated method stub
		label = new JLabel("", new ImageIcon(url), JLabel.CENTER);
		panel.add(label);
	}
	
	

}
