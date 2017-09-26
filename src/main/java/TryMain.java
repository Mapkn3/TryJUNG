import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TryMain {
    private static GraphZoomScrollPane panel;
    private static Button button;
    private static TreeGenerator treeGenerator = new TreeGenerator(3, 200, true, false);
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GraphZoomScrollPane(printTree(treeGenerator));
        button = new Button("Generate");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setView(frame);
            }
        });
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(button, BorderLayout.SOUTH);
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
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<>(layout);
        vv.setBackground(Color.WHITE);
        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(tree));
        vv.getRenderContext().setVertexLabelTransformer(new NodeIdFactory(tree));
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.setPreferredSize(new Dimension(1400, 750));
        return vv;
    }
}
