import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;

public class TryMain {
    public static void main(String[] args) {
        TreeGenerator treeGenerator = new TreeGenerator(3, 200, true, false);
        Tree<Integer, String> tree = treeGenerator.generate();
        Layout<Integer, String> layout = new RadialTreeLayout<>(tree);
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<>(layout);
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(tree));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.setVertexToolTipTransformer(new ToStringLabeller());

        JFrame frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
