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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GraphView extends JFrame {
    private int k;
    private int count;
    private boolean isRegular;
    private boolean isRuleA;
    private int r;
    private final ScalingControl scaler = new CrossoverScalingControl();

    private int[] randomHist;
    private List<Integer> apexList;
    private List<Integer> regularApexList;
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
        this.k = 5;
        this.count = 150;
        this.isRegular = false;
        this.isRuleA = false;
        this.r = 200;

        this.isStart = true;

        this.randomHist = new int[this.k];
        this.apexList = new ArrayList<>();
        this.regularApexList = new ArrayList<>();

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
        Tree<Integer, String> tree = generator.generate(isStart ? regularApexList : apexList, randomHist);

        while (tree.getVertexCount() <= 10) {
            tree = generator.generate(isStart ? regularApexList : apexList, randomHist);
        }
        this.randomHistogram.setDataset(randomHist);
        if (isStart) {
            this.apexLineChart.setRegularApexList(regularApexList);
        }
        this.apexLineChart.setApexList(apexList);
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
        JPanel controlPanel = new JPanel(new BorderLayout());

        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                $$$setupUI$$$();
                init();

            }
        });

        controlPanel.add(generateButton);
        return controlPanel;
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
