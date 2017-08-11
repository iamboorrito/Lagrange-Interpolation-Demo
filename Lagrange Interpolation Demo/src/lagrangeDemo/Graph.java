package lagrangeDemo;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Graph extends JPanel implements MouseListener, MouseMotionListener {
		
	private static final long serialVersionUID = 1275589541313159546L;
	private LagrangePoly poly;
	private BasicStroke thinStroke, thickStroke;
	
	//Initialization
	public Graph(){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setBackground(Color.WHITE);
		this.setDoubleBuffered(true);
		this.setPreferredSize(new Dimension(700, 700));
		this.setToolTipText("Hold Shift to move points and Control + Click to remove points");
		
		poly = new LagrangePoly();
		thinStroke = new BasicStroke(1);
		thickStroke = new BasicStroke(2);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(thinStroke);
		
		//Store values of frequently called methods
		int width = this.getWidth();
		int height = this.getHeight();
		
		//Get spacing, if too small don't draw lines
		int dw = 25;
		
		{   //Draw Gridlines
			g2.setColor(Color.LIGHT_GRAY);

			//Draw vertical gridlines
			for(int i = 0; i < width; i += dw){
				g2.drawLine(i, 0, i, height);
			}
			
			//Draw horizontal gridlines
			for(int i = 0; i < height; i += dw){
				g2.drawLine(0, i, width, i);
			}
			
			g2.setColor(Color.BLACK);
		}
		
		g2.setStroke(thickStroke);

		//Draw the polynomial
		for(int i = 0; i < width-1; i++){
			g2.draw(new Line2D.Double(i, poly.eval(i), i+1, poly.eval(i+1)));
			
		}
		
		g2.setColor(Color.BLUE);
		
		//Draw the control points
		for(Point2D.Double p : poly.getPoints()){
			g2.draw(new Ellipse2D.Double(p.getX()-5, p.getY()-5, 10, 10));
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
		//Delete Points with control+click
		if(e.isControlDown()){
				ArrayList<Point2D.Double> p = poly.getPoints();
				int n = p.size();
				
				//No points to move
				if(n == 0)
					return;
				
				double min = 1000;
				double dist;
				int m = n-1;
				
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				
				//Get closest point to mouse cursor
				for(int i = 0; i < n; i++){
					dist = p.get(i).distance(mouse);
					if(dist < min){
						m = i;
						min = dist;
					}
				}

				p.remove(m);
				poly.genCoeff();
		}else{
			//Add point with normal click
			poly.add(new Point2D.Double(e.getX(), e.getY()));
		}
		
		repaint();
	}
	
	//For moving closest point to mouse cursor
	@Override
	public void mouseMoved(MouseEvent e) {
		
		if(e.isShiftDown()){
			
			ArrayList<Point2D.Double> p = poly.getPoints();
			int n = p.size();
			
			//No points to move
			if(n == 0)
				return;
			
			//For finding the min distance to cursor
			double min = 1000;
			double dist;
			int m = n-1;
			
			Point2D.Double mouse = new Point2D.Double(e.getX(), e.getY());
			
			//Get closest point to mouse cursor
			for(int i = 0; i < n; i++){
				dist = p.get(i).distance(mouse);
				if(dist < min){
					m = i;
					min = dist;
				}
			}
			//Set location and calculate P(x)
			p.get(m).setLocation(e.getX(), e.getY());
			poly.genCoeff();
			
			//Show results
			repaint();
		}	
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
