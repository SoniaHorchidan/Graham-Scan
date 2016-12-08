import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GrahamScan extends JFrame{
	
	static Stack<Point2D.Double> List = new Stack<Point2D.Double>();
	static Point2D.Double []points;
	static Point2D.Double []copyPoints;
		
	public GrahamScan(){
		super("Proiect geometrie computationala");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.WHITE);
	  
		solve();		
	}
	
	  public void drawPoly(Graphics g){
		  	Graphics2D g2d = (Graphics2D) g;
	    	g2d.setColor(Color.BLACK);
		    Stroke stroke = new BasicStroke(4f);
		    g2d.setStroke(stroke); 
		    
		    for(int i = 0; i < copyPoints.length - 1; i ++)
		    	g2d.draw(new Line2D.Double(copyPoints[i], copyPoints[i + 1]));
		    g2d.draw(new Line2D.Double(copyPoints[0], copyPoints[ copyPoints.length - 1]));
		    
		    g2d.setColor(Color.RED);
		    g2d.setStroke(stroke); 
		    stroke = new BasicStroke(2f);
		    for(int i = 0; i < List.size() - 1; i ++)
		    	g2d.draw(new Line2D.Double(List.get(i), List.get(i + 1)));
		    g2d.draw(new Line2D.Double(List.get(0), List.get(List.size() - 1)));

	    }
	  public void paint(Graphics g) {
			super.paint(g);
			drawPoly(g);
	 }
	
	public boolean isCounterClock(Point2D.Double P, Point2D.Double Q, Point2D.Double R){
		//return 0 ==>  coliniare; >0 ==> CounterClock;  < 0 ==> Clock
		return Q.x * R.y + P.x * Q.y + P.y * R.x - P.y * Q.x - Q.y * R.x - P.x * R.y > 0;
	}
	public void solve(){
		sortByY();
		
		sortByPolarAngle();
		
		List.push(points[0]);
		List.push(points[1]);
		
		for(int i = 2; i < points.length; i ++){
			List.push(points[i]);
			while(List.size() >= 3 && !isCounterClock(List.get(List.size() - 3), List.get(List.size() - 2), List.get(List.size() - 1))){
				Point2D.Double p = List.peek();
				List.pop();
				List.pop();
				List.push(p);
			}
		}
	}
	
	public void sortByY(){
		Arrays.sort(points, new Comparator<Point2D.Double>(){
			public int compare(Point2D.Double A, Point2D.Double B){
				if(A.y - B.y < 0)
					return -1;
				else 
					if(A.y == B.y)
						return 0;
				return 1;
			}
		});
	}
	
	public void sortByPolarAngle(){
		Arrays.sort(points, 1, points.length, new Comparator<Point2D.Double>(){
			public int compare(Point2D.Double A, Point2D.Double B){
				double x1 = A.x - points[0].x;
				double x2 = B.x - points[0].x;
				double y1 = A.y - points[0].y;
				double y2 = B.y - points[0].y;
				if(Math.atan2(y1, x1) - Math.atan2(y2,x2) > 0)
					return 1;
				else 
					if(Math.atan2(y1, x1) - Math.atan2(y2,x2) < 0)
						return -1;
				return 0;
			}
		});
	}
	
	public void print(){
		for(Point2D.Double p : List)
			System.out.println(p);
	}
	public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
		System.out.println("Dati numarul de varfuri ale poligonului: ");
	        int n = scanner.nextInt();
	        points = new Point2D.Double[n + 1];
	        copyPoints = new Point2D.Double[n];
	        System.out.println("Dati coordonatele punctelor poligonului: ");
	        for (int i = 0; i < n; i++) {
	            double x = scanner.nextDouble();
	            double y =scanner.nextDouble();
	            points[i] = new Point2D.Double(x, y);
	        }
	        
	        for(int i = 0; i < points.length - 1; i ++)
	        	copyPoints[i] = points[i];
	        
	        System.out.println("Dati coordonatele punctului exterior: ");
	        double x = scanner.nextDouble();
                double y =scanner.nextDouble();
                points[n] = new Point2D.Double(x, y);
	        
	        GrahamScan grahamscan = new GrahamScan();
	        grahamscan.print();
	        
	        SwingUtilities.invokeLater(new Runnable() {
	              public void run() {
	                  new GrahamScan().setVisible(true);
	              }
	         });
	        
	    }
}
