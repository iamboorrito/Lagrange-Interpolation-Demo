package lagrangeDemo;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class LagrangePoly {

	private ArrayList<Point2D.Double> points;
	private ArrayList<Double> coefficients;
	
	/**
	 * Constructs the 0 polynomial.
	 */
	public LagrangePoly(){
		points = new ArrayList<Point2D.Double>();
		coefficients = new ArrayList<Double>();
		coefficients.add(0.0);
	}
	
	/**
	 * Constructs the interpolating polynomial through the prescribed 
	 * points x[] and y[].
	 * @param x the x values sampled
	 * @param y the values to interpolate, y = f(x)
	 */
	public LagrangePoly(double[] x, double[] y){
		points = new ArrayList<Point2D.Double>(x.length);
		coefficients = new ArrayList<Double>();
		coefficients.add(0.0);
		
		for(int i = 0; i < x.length; i++){
			points.add(new Point2D.Double(x[i], y[i]));
		}
		
		genCoeff();
	}
	
	/**
	 * Constructs the Lagrange Polynomial passing through the given points.
	 * @param points The points to interpolate.
	 */
	public LagrangePoly(ArrayList<Point2D.Double> points){
		this.points = points;
		coefficients = new ArrayList<Double>(points.size());
		
		genCoeff();
		
	}
	
	/**
	 * Returns whether a point is interpolated by this polynomial.
	 * @param p The point to check.
	 * @return True if p is interpolated by P(x).
	 */
	public boolean contains(Point2D.Double p){
		return points.contains(p);
	}
	
	/**
	 * Calculates the coefficients for P(x) = a + b (x - x0) + c (x - x0)(x-x1) + ...
	 */
	public void genCoeff() {
		
		coefficients.clear();
		
		ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>(coefficients.size());
		
		for(Point2D.Double p : points){
			temp.add(p);
			coefficients.add(divDiff(temp));
		}
	}

	/**
	 * Adds a point to be interpolated.
	 * @param p The point (x, f(x)) that this polynomial must pass through.
	 */
	public void add(Point2D.Double p){
		
		points.add(p);
		genCoeff();
	}
	
	/**
	 * Alternative method to add a point to the interpolating polynomial.
	 * @param x
	 * @param y Value of function P(x) must match
	 */
	public void add(double x, double y){
		
		points.add(new Point2D.Double(x, y));
		genCoeff();
	}
	
	/**
	 * Removes a control point.
	 * @param p The point to be freed.
	 */
	public void remove(Point2D.Double p){
		points.remove(p);
		genCoeff();
	}
	
	/**
	 * Getter method for the control points of P(x).
	 * @return
	 */
	public ArrayList<Point2D.Double> getPoints(){
		return points;
	}
	
	/**
	 * Calculates Newton's Divided Difference for the list of points.
	 * @param ps The points to interpolate.
	 * @return f[x0, x1, ..., xn]
	 */
	public double divDiff(List<Point2D.Double> ps){
		
		if(ps.size() == 1){
			return ps.get(0).getY();
		}
		
		int n = ps.size();
		
		return (divDiff(ps.subList(0, n-1))-
				divDiff(ps.subList(1, n))
				) / (
				ps.get(0).getX() - ps.get(n-1).getX() );
	}
	
	/**
	 * Calculates P(x) for a given x.
	 * @param x A real number in the interpolation interval of P.
 	 * @return P(x)
	 */
	public double eval(double x){
		
		if(coefficients.size() == 0)
			return 0;
		
		double sum = 0;
		
		//Calculate using nested form
		for(int i = coefficients.size()-1; i > 0; i--){
			sum = (x-points.get(i-1).getX())*(sum + coefficients.get(i));
		}		
		
		return sum+coefficients.get(0); 

	}

	public String toString(){
		
		StringBuilder b = new StringBuilder();
		
		if(coefficients.size() == 0){
			b.append("y = 0");
			return b.toString();
		}
		
		if(coefficients.size() == 1){
			b.append(coefficients.get(0));
			return b.toString();
		}
			
		b.append(coefficients.get(0));
		b.append(" + ");
		for(int i = 1; i < coefficients.size(); i++){
			b.append("(" + String.format("%.3f", coefficients.get(i)) + ")");
			for(int j = 0; j < i; j++){
				b.append("(x - " + String.format("%.3f", points.get(j).getX()) + ")");
			}
			if(i != coefficients.size()-1)
				b.append(" + ");
		}
		
		return b.toString();
	}

}
