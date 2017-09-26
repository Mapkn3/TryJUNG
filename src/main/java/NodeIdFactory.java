import com.google.common.base.Function;
import edu.uci.ics.jung.graph.Tree;

public class NodeIdFactory implements Function<Object, String> {
    private Tree<Integer, String> tree;
    public NodeIdFactory(Tree<Integer, String> tree) {
        this.tree = tree;
    }

    @Override
    public String apply(Object o) {
        Integer parentId = tree.getParent(Integer.parseInt(o.toString()));
        return o.toString() + "-" + (parentId == null ? 0 : parentId);
    }
}