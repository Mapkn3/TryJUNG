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
        this.isRegular =isRegular;
        this.isRuleA = isRuleA;
    }

    public Tree<Integer, String> generate() {
        Random random = new Random();
        boolean isEnd = false;
        int id = 1;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        List<Integer> childList = new ArrayList<>();
        DelegateTree<Integer, String> tree = new DelegateTree<>();
        tree.addVertex(id);
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
            int countChild = isRegular ? this.k : random.nextInt(this.k);
            for (int i = 0; i < countChild; i++) {
                id++;
                tree.addChild(id + "-" + parent_id, parent_id, id);
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
}
