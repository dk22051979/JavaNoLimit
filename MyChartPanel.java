import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.JFreeChart;

public class MyChartPanel extends JPanel
{
  List charts = new ArrayList();

  public MyChartPanel(LayoutManager paramLayoutManager)
  {
    super(paramLayoutManager);
  }

  public void addChart(JFreeChart paramJFreeChart)
  {
    this.charts.add(paramJFreeChart);
  }

  public JFreeChart[] getCharts()
  {
    int i = this.charts.size();
    JFreeChart[] arrayOfJFreeChart = new JFreeChart[i];
    for (int j = 0; j < i; j++)
      arrayOfJFreeChart[j] = ((JFreeChart)this.charts.get(j));
    return arrayOfJFreeChart;
  }
}