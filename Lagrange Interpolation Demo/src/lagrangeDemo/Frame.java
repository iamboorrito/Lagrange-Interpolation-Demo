package lagrangeDemo;
import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = 4983727800375272097L;

	public Frame(){
		super("Polynomial Interpolation");
		
		this.setSize(700, 500);
		
		Graph g = new Graph();
		
		this.add(g);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	public static void main(String[]args){
		new Frame();
	}
}
