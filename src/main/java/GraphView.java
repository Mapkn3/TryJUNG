import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class GraphView extends JFrame {
    private TreeModel treeModel;
    private int k;
    private int count;
    private boolean isRegular;
    private boolean isRuleA;
    private int r;
    private int counter;
    private final ScalingControl scaler = new CrossoverScalingControl();

    private boolean isStart;
    private RandomHistogram randomHistogram;
    private ApexLineChart apexLineChart;

    private JPanel graphViewPanel;
    private JPanel scalePanel;
    private JPanel controlPanel;
    private JPanel histPanel;
    private JPanel diagramPanel;
    private JPanel mainPanel;
    private JPanel graphPanel;
    private TreeGenerator treeGenerator;
    private VisualizationViewer<Integer, String> vv;


    public GraphView() {
        this(5, 150, false, 200);
    }

    public GraphView(int k, int count, boolean isRuleA, int r) {
        this.k = k;
        this.count = count;
        this.isRuleA = isRuleA;
        this.r = r;

        this.counter = 0;
        this.isRegular = false;
        this.isStart = true;

        this.randomHistogram = new RandomHistogram();
        this.apexLineChart = new ApexLineChart();

        this.treeGenerator = new TreeGenerator(k, count, isRegular, isRuleA);
        $$$setupUI$$$();
        init();
    }

    private void init() {
        isStart = false;
        setContentPane(mainPanel);
        pack();
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private Tree<Integer, String> generateTree(TreeGenerator treeGenerator) {
        TreeGenerator generator = treeGenerator;
        if (isStart) {
            generator = new TreeGenerator(k, count, true, isRuleA);
        }
        treeModel = generator.generate();
        while (treeModel.getTree().getVertexCount() <= 10) {
            treeModel = generator.generate();
        }
        Tree<Integer, String> tree = treeModel.getTree();

        this.randomHistogram.setRandomHistogramColor(ChartColor.LIGHT_GREEN);
        this.randomHistogram.setDataset(treeModel.getRandomHist());

        this.apexLineChart.setRegularApexLineColor(ChartColor.DARK_BLUE);
        this.apexLineChart.setApexLineColor(ChartColor.LIGHT_CYAN);
        if (isStart) {
            this.apexLineChart.setRegularApexList(treeModel.getApexList());
        } else {
            this.apexLineChart.setApexList(treeModel.getApexList());
        }

        Field[] fileds = ChartColor.class.getFields();
        for (Field field : fileds) {
            if (field.toString().contains("ChartColor")){
                System.out.println(field.getName());
            }
        }

        List<String> treeTable = treeModel.getTreeTable();
        for (String row : treeTable) {
            System.out.println(row);
        }
        List<String> apexTable = treeModel.getApexTable();
        System.out.println("Apex: " + apexTable);
        return tree;
    }

    private void generateTreeViewer(TreeGenerator treeGenerator) {
        Tree<Integer, String> tree = generateTree(treeGenerator);
        Layout<Integer, String> layout = new TreeLayout<>(tree);
        this.vv = new VisualizationViewer<>(layout);
        this.vv.setBackground(Color.WHITE);
        this.vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(tree));
        this.vv.getRenderContext().setVertexLabelTransformer(new NodeIdFactory(tree));
        this.vv.setVertexToolTipTransformer(new ToStringLabeller());

        DefaultModalGraphMouse<Integer, String> graphMouse = new DefaultModalGraphMouse<>();
        this.vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
    }

    private JPanel createScalePanel() {
        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plusButtonAction();
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minusButtonAction();
            }
        });

        JPanel scalePanel = new JPanel(new GridLayout(1, 0));
        scalePanel.setBorder(BorderFactory.createTitledBorder("Zoom"));
        scalePanel.add(plus);
        scalePanel.add(minus);

        return scalePanel;
    }

    private void plusButtonAction() {
        scaler.scale(vv, 1.1F, this.vv.getCenter());
    }

    private void minusButtonAction() {
        scaler.scale(vv, 0.9090909F, this.vv.getCenter());
    }

    private JPanel createGraphPanel() {
        generateTreeViewer(this.treeGenerator);
        return new GraphZoomScrollPane(vv);
    }

    private JPanel createControlPanel() {
        saveTree();

        JPanel controlPanel = new JPanel(new BorderLayout());

        JButton autogenerateButton = new JButton("Autogenerate " + ((this.counter < this.r)?"next " + String.valueOf(this.r - this.counter) + " tree":"is off"));
        autogenerateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTreeRTimes();
            }
        });

        JButton generateButton = new JButton("Generate " + ((this.counter < this.r)?"tree №" + String.valueOf(++this.counter):"is off"));
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTrees(1);
            }
        });

        JButton closeButton = new JButton("Use Python");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("python extract.py");
                    System.out.println("Document created.");
                    dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        controlPanel.add(generateButton, BorderLayout.WEST);
        controlPanel.add(autogenerateButton, BorderLayout.CENTER);
        if (this.counter == this.r) {
            controlPanel.add(closeButton, BorderLayout.EAST);
        }
        return controlPanel;
    }

    private void generateTreeRTimes() {
        generateTrees(this.r-this.counter+1);
    }

    private void generateTrees(int count) {
        if (this.counter < this.r) {
            for (int i = 0; i < count; i++) {
                $$$setupUI$$$();
                init();
            }
        }
    }

    private JPanel createRandomHistogramPanel() {
        return randomHistogram.getHistogram();
    }

    private JPanel createApexLineChartPanel() {
        return apexLineChart.getApexLineChart();
    }

    private void createUIComponents() {
        graphPanel = createGraphPanel();
        scalePanel = createScalePanel();
        controlPanel = createControlPanel();
        histPanel = createRandomHistogramPanel();
        diagramPanel = createApexLineChartPanel();
    }

    private void saveTree() {
        File folder = new File("C:\\Users\\Kondrat\\Desktop\\Trees\\");
        if (isStart) {
            for(File f : folder.listFiles()) {
                f.delete();
            }
        }
        folder.mkdir();
        File file = new File(folder.getPath()+"\\"+String.valueOf(this.counter)+".txt");
        List<Integer> apexList = treeModel.getApexList();
        try(FileWriter writer = new FileWriter(file, false)) {
            int countVertex = apexList.size()-1;
            int countApex = apexList.get(countVertex);
            double alpha = (countVertex*1.0)/countApex;
            int height = treeModel.getTree().getHeight();
            writer.write(String.valueOf(alpha)+"\n");
            writer.append(String.valueOf(countVertex)).append("\n");
            writer.append(String.valueOf(countApex)).append("\n");
            writer.append(String.valueOf(height)).append("\n");
            List<String> treeTable = treeModel.getTreeTable();
            for (String row : treeTable) {
                writer.append(row).append("\n");
            }
            List<String> apexTable = treeModel.getApexTable();
            writer.append("Apex: ").append(String.valueOf(apexTable)).append("\n");
            writer.flush();

            int widthImg = 640;
            int heightImg = 480;
            File apexLineChartFile = new File( folder.getPath()+"\\"+String.valueOf(this.counter)+"_a.jpeg" );
            ChartUtilities.saveChartAsJPEG(apexLineChartFile, apexLineChart.getLineChart(), widthImg, heightImg);
            File randomHistogramFile = new File( folder.getPath()+"\\"+String.valueOf(this.counter)+"_h.jpeg" );
            ChartUtilities.saveChartAsJPEG(randomHistogramFile, randomHistogram.getRandomHistogram(), widthImg, heightImg);
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        graphViewPanel = new JPanel();
        graphViewPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(graphViewPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 500), new Dimension(400, 500), null, 2, false));
        graphViewPanel.add(scalePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        graphViewPanel.add(controlPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        graphViewPanel.add(graphPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mainPanel.add(diagramPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mainPanel.add(histPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
