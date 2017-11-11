import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.List;

public class ApexLineChart {
    private List<Integer> regularApexList;
    private List<Integer> apexList;
    private JFreeChart lineChart;
    private Paint apexLineColor;
    private Paint regularApexLineColor;

    public ApexLineChart() {
        this.regularApexList = null;
        this.apexList = null;
    }

    public void setRegularApexList(List<Integer> regularApexList) {
        this.regularApexList = regularApexList;
        updateApexLineChart();
    }

    public void setApexList(List<Integer> apex) {
        this.apexList = apex;
        updateApexLineChart();
    }

    public void setApexLineColor(Paint paint) {
        this.apexLineColor = paint;
    }

    public void setRegularApexLineColor(Paint paint) {
        this.regularApexLineColor = paint;
    }

    public void setLineColor() {
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesPaint(0, this.regularApexLineColor);
        renderer.setSeriesPaint(1, this.apexLineColor);
        lineChart.getCategoryPlot().setRenderer(renderer);
    }

    public List<Integer> getRegularApexList() {
        return regularApexList;
    }

    public List<Integer> getApexList() {
        return apexList;
    }

    public JFreeChart getLineChart() {
        return this.lineChart;
    }

    public ChartPanel getApexLineChart() {
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(550, 200));
        return chartPanel;
    }

    private void updateApexLineChart() {
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
                dataset.addValue(((i*1.0)/this.apexList.get(i)), "Apex", String.valueOf(i));
            }
        }
        this.lineChart = ChartFactory.createLineChart(
                "Alpha line chart",
                "Count",
                "Alpha",
                dataset,
                PlotOrientation.VERTICAL,
                false,true, false);
        setLineColor();
    }
}
