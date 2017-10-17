import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

public class RandomHistogram {
    private DefaultCategoryDataset dataset;

    public RandomHistogram() {
        dataset = new DefaultCategoryDataset();
    }

    public void setDataset(int[] randomData) {
        dataset.clear();
        for (int i = 0; i < randomData.length; i++) {
            dataset.addValue(randomData[i], "Random number", String.valueOf(i));
        }
    }

    public ChartPanel getHistogram() {
        JFreeChart randomHistogram = ChartFactory.createBarChart(
                "Random number histogram",
                "Number",
                "Count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true,  false);
        ChartPanel chartPanel = new ChartPanel(randomHistogram);
        chartPanel.setPreferredSize(new Dimension(200, 100));
        return chartPanel;
    }
}
