import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.List;

public class ApexLineChart {
    private List<Integer> regularApexList;
    private List<Integer> apexList;

    public ApexLineChart() {
        this.regularApexList = null;
        this.apexList = null;
    }

    public void setRegularApexList(List<Integer> regularApexList) {
        this.regularApexList = regularApexList;
    }

    public void setApexList(List<Integer> apex) {
        this.apexList = apex;
    }

    public ChartPanel getApexLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (this.regularApexList != null) {
            int count = this.regularApexList.size();
            dataset.addValue(0, "RegularApex", String.valueOf(0));
            for (int i = 1; i < count; i++) {
                dataset.addValue(((i*1.0)/this.regularApexList.get(i)), "RegularApex", String.valueOf(i));
            }
        }
        if (this.apexList != null) {
            int count = this.apexList.size();
            dataset.addValue(0, "Apex", String.valueOf(0));
            for (int i = 1; i < count; i++) {
                dataset.addValue(((i*0.1)/this.apexList.get(i)), "Apex", String.valueOf(i));
            }
        }
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Alpha line chart",
                "Count",
                "Alpha",
                dataset,
                PlotOrientation.VERTICAL,
                false,true, false);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(550, 200));
        return chartPanel;
    }

}
