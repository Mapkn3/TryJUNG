import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

public class RandomHistogram {
    private DefaultCategoryDataset dataset;
    private JFreeChart randomHistogram;
    private Paint randomHistogramColor;

    public RandomHistogram() {
        dataset = new DefaultCategoryDataset();
    }

    public void setDataset(int[] randomData) {
        dataset.clear();
        for (int i = 0; i < randomData.length; i++) {
            dataset.addValue(randomData[i], "Random number", String.valueOf(i));
        }
        updateRandomHistogram();
    }

    public void setRandomHistogramColor(Paint paint) {
        this.randomHistogramColor = paint;
    }

    public void setColor() {
        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, this.randomHistogramColor);
        randomHistogram.getCategoryPlot().setRenderer(renderer);
    }

    public JFreeChart getRandomHistogram() {
        return this.randomHistogram;
    }

    public ChartPanel getHistogram() {
        ChartPanel chartPanel = new ChartPanel(randomHistogram);
        chartPanel.setPreferredSize(new Dimension(200, 100));
        return chartPanel;
    }

    private void updateRandomHistogram() {
        this.randomHistogram = ChartFactory.createBarChart(
                "Random number histogram",
                "Number",
                "Count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true,  false);
        setColor();
    }
}
