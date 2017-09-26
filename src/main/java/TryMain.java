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

public class TryMain {
    private static GraphZoomScrollPane panel;
    private static Button button;
    private static JPanel scaleGrid;
    private static TreeGenerator treeGenerator = new TreeGenerator(3, 200, false, false);
    private static VisualizationViewer<Integer, String> vv;

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = new GraphZoomScrollPane(printTree(treeGenerator));
        button = new Button("Generate");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setView(frame);
            }
        });

        final ScalingControl scaler = new CrossoverScalingControl();
        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scaler.scale(TryMain.vv, 1.1F, TryMain.vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scaler.scale(TryMain.vv, 0.9090909F, TryMain.vv.getCenter());
            }
        });
        scaleGrid = new JPanel(new GridLayout(1, 0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
        scaleGrid.add(plus);
        scaleGrid.add(minus);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(button, BorderLayout.SOUTH);
        contentPane.add(scaleGrid);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    private static void setView(JFrame frame) {
        panel = new GraphZoomScrollPane(printTree(treeGenerator));
        Container contentPane = frame.getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(button, BorderLayout.SOUTH);
        contentPane.add(scaleGrid);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    private static VisualizationViewer printTree(TreeGenerator treeGenerator) {
        Tree<Integer, String> tree = treeGenerator.generate();
        while (tree.getVertexCount() <= 10) {
            tree = treeGenerator.generate();
        }
        Layout<Integer, String> layout = new TreeLayout<>(tree);
        vv = new VisualizationViewer<>(layout);
        vv.setBackground(Color.WHITE);
        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(tree));
        vv.getRenderContext().setVertexLabelTransformer(new NodeIdFactory(tree));
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.setPreferredSize(new Dimension(1400, 700));

        DefaultModalGraphMouse<Integer, String> graphMouse = new DefaultModalGraphMouse<>();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        return vv;
    }
}
