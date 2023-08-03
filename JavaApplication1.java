package javaapplication1;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class JavaApplication1 extends ApplicationFrame {
    public JavaApplication1(String title) {
    super(title);
    JFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart Demo",
            "Category",
            "Value",
            getCategDataset(),
            PlotOrientation.HORIZONTAL,
            true,
            true,
            false
    );
    ChartPanel chartPanel = new ChartPanel(chart, false);
    chartPanel.setPreferredSize(new Dimension(500, 270));
    setContentPane(chartPanel);
    }
    public DefaultCategoryDataset getCategDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Row 1", "Column 1");
        dataset.addValue(5.0, "Row 1", "Column 2");
        dataset.addValue(3.0, "Row 1", "Column 3");
        dataset.addValue(2.0, "Row 2", "Column 1");
        dataset.addValue(3.0, "Row 2", "Column 2");
        dataset.addValue(2.0, "Row 2", "Column 3");
        return dataset;
    }
    public static void insertCmCsv(String fileName){
        
    //String fileName="fo28JUL2023bhav.csv";
        try
        {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/nsecm2023", "root","deepak");
            Statement statement = connection.createStatement();
            String tblName =new String(fileName.substring(0,11));
            
            String sql4 = "CREATE TABLE  IF NOT EXISTS " + tblName.toLowerCase() + " (  SYMBOL text DEFAULT NULL,   SERIES text DEFAULT NULL, "
                    + " OPEN float(19,2) DEFAULT NULL,  HIGH float(19,2) DEFAULT NULL,  LOW float(19,2) DEFAULT NULL,  CLOSE float(19,2) DEFAULT NULL,"
                    + " LAST float(19,2) DEFAULT NULL,  PREVCLOSE float(19,2) DEFAULT NULL,  TOTTRDQTY int(11) DEFAULT NULL,"
                    + " TOTTRDVAL float(19,2) DEFAULT NULL, TIMESTAMP text DEFAULT NULL, TOTALTRADES int(11) DEFAULT NULL,"
                    + " ISIN text DEFAULT NULL)";                  
            statement.executeUpdate(sql4);
            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                    
                    String sql3 = "INSERT INTO " + tblName.toLowerCase() + " (SYMBOL,SERIES,OPEN,HIGH,LOW,CLOSE,LAST,PREVCLOSE,TOTTRDQTY,TOTTRDVAL,TIMESTAMP,TOTALTRADES,ISIN) VALUES ('"  
                                  + columns[0].replaceAll("\\W","") +  "','"
                                  + columns[1].replaceAll("\\W","") +  "','"
                                  + columns[2] +  "','"
                                  + columns[3] +  "','"
                                  + columns[4] +  "','"
                                  + columns[5] +  "','"
                                  + columns[6] +  "','"
                                  + columns[7] +  "','"
                                  + columns[8] +  "','"
                                  + columns[9] +  "','"
                                  + columns[10] +  "','"
                                  + columns[11] +  "','"                                
                                  + columns[12].replaceAll("\\W","") +  "')";
                         statement.executeUpdate(sql3);
                         //System.out.println(sql3);    
                }
                ++count;
            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
    }
    public static void insertFnoCsv(String fileName){
        
    //String fileName="fo28JUL2023bhav.csv";
        try
        {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/nsefo2023", "root","deepak");
            Statement statement = connection.createStatement();
            String tblName =new String(fileName.substring(0,11));
            String sql1 = "CREATE TABLE  IF NOT EXISTS " + tblName.toLowerCase() + " (  INSTRUMENT text DEFAULT NULL,   SYMBOL text DEFAULT NULL,  EXPIRY_DT text DEFAULT NULL, "
                    + " STRIKE_PR int(11) DEFAULT NULL,  OPTION_TYP text DEFAULT NULL,  OPEN float(19,2) DEFAULT NULL,  HIGH float(19,2) DEFAULT NULL,"
                    + " LOW float(19,2) DEFAULT NULL,  CLOSE float(19,2) DEFAULT NULL,  SETTLE_PR float(19,2) DEFAULT NULL,"
                    + " CONTRACTS int(11) DEFAULT NULL, VAL_INLAKH float(19,2) DEFAULT NULL, OPEN_INT int(11) DEFAULT NULL,"
                    + " CHG_IN_OI int(11) DEFAULT NULL, TIMESTAMP text DEFAULT NULL)";                  
            statement.executeUpdate(sql1);
            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                    
                    String sql2 = "INSERT INTO " + tblName.toLowerCase() + " (INSTRUMENT,   SYMBOL ,  EXPIRY_DT , "
                    + " STRIKE_PR ,  OPTION_TYP ,  OPEN ,  HIGH ,"
                    + " LOW ,  CLOSE ,  SETTLE_PR,"
                    + " CONTRACTS , VAL_INLAKH, OPEN_INT,"
                    + " CHG_IN_OI, TIMESTAMP) VALUES ('"  + columns[0].replaceAll("\\W","") +  "','"
                                  + columns[1].replaceAll("\\W","") +  "','"
                                  + columns[2].replaceAll("\\W","") +  "','"
                                  + columns[3] +  "','"
                                  + columns[4] +  "','"
                                  + columns[5] +  "','"
                                  + columns[6] +  "','"
                                  + columns[7] +  "','"
                                  + columns[8] +  "','"
                                  + columns[9] +  "','"
                                  + columns[10] +  "','"
                                  + columns[11] +  "','"
                                  + columns[12] +  "','"
                                  + columns[13] +  "','"
                                  + columns[14].replaceAll("\\W","") +  "')";
                         statement.executeUpdate(sql2);
                         //System.out.println(sql2);    
                }
                ++count;
            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                
                //insertCmCsv("cm24JUL2023bhav.csv");
                //insertFnoCsv("fo28JUL2023bhav.csv");
            }
        });

    }
    private static  void createAndShowGUI() {
        /*
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1340,690);
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);*/
        
        JavaApplication1 formObject = new JavaApplication1("OI");
        formObject.add(new MyPanel());
        formObject.pack();
        RefineryUtilities.centerFrameOnScreen(formObject);
        formObject.setVisible(true);
        
    }
}
class MyPanel extends JPanel {

    RedSquare redSquare = new RedSquare();

    public MyPanel() {

        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                moveSquare(e.getX(),e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e){
                moveSquare(e.getX(),e.getY());
            }
        });

    }

    private void moveSquare(int x, int y){

        // Current square state, stored as final variables 
        // to avoid repeat invocations of the same methods.
        final int CURR_X = redSquare.getX();
        final int CURR_Y = redSquare.getY();
        final int CURR_W = redSquare.getWidth();
        final int CURR_H = redSquare.getHeight();
        final int OFFSET = 1;

        if ((CURR_X!=x) || (CURR_Y!=y)) {

            // The square is moving, repaint background 
            // over the old square location. 
            repaint(CURR_X,CURR_Y,CURR_W+OFFSET,CURR_H+OFFSET);

            // Update coordinates.
            redSquare.setX(x);
            redSquare.setY(y);

            // Repaint the square at the new location.
            repaint(redSquare.getX(), redSquare.getY(), 
                    redSquare.getWidth()+OFFSET, 
                    redSquare.getHeight()+OFFSET);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(1340,690);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.drawString("This is my custom Panel!",10,20);

        redSquare.paintSquare(g);
    }  
}

class RedSquare{

    private int xPos = 50;
    private int yPos = 50;
    private int width = 20;
    private int height = 20;

    public void setX(int xPos){ 
        this.xPos = xPos;
    }

    public int getX(){
        return xPos;
    }

    public void setY(int yPos){
        this.yPos = yPos;
    }

    public int getY(){
        return yPos;
    }

    public int getWidth(){
        return width;
    } 

    public int getHeight(){
        return height;
    }

    public void paintSquare(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(xPos,yPos,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(xPos,yPos,width,height);  
    }
}