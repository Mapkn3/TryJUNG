import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeGenerator {
    private int k;
    private int count;
    private boolean isRegular;
    private boolean isRuleA;

    public TreeGenerator(int k, int count, boolean isRegular, boolean isRuleA) {
        this.k = k;
        this.count = count;
        this.isRegular = isRegular;
        this.isRuleA = isRuleA;
    }

    public Tree<Integer, String> generate(List<Integer> apex, int[] randomGist) {
        Random random = new Random();
        for (int i = 0; i < randomGist.length; i++) {
            randomGist[i] = 0;
        }
        apex.clear();
        apex.add(0);
        boolean isEnd = false;
        int id = 1;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        List<Integer> childList = new ArrayList<>();
        DelegateTree<Integer, String> tree = new DelegateTree<>();
        tree.addVertex(id);
        apex.add(countApex(tree));
        queue.addLast(id);
        while (!isEnd || !queue.isEmpty()) {
            if (queue.isEmpty() && childList.isEmpty()) {
                break;
            }
            if (queue.isEmpty()) {
                queue.addAll(childList);
                childList.clear();
            }
            int parent_id = queue.pollFirst();
            int countChild = isRegular ? (this.k - 1) : random.nextInt(this.k);
            randomGist[countChild]++;
            for (int i = 0; i < countChild; i++) {
                id++;
                tree.addChild(id + "-" + parent_id, parent_id, id);
                apex.add(countApex(tree));
                childList.add(id);
                if (id == this.count) {
                    isEnd = true;
                }
                if (this.isRuleA && isEnd) {
                    childList.clear();
                    queue.clear();
                    break;
                }
            }
        }
        return tree;
    }

    private int countApex(DelegateTree<Integer, String> tree) {
        int count = 0;
        for (Integer node : tree.getVertices()) {
            if (tree.isLeaf(node)) {
                count++;
            }
        }
        return count;
    }
}
