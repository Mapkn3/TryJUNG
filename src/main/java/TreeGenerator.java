import edu.uci.ics.jung.graph.DelegateTree;

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

    public TreeModel generate() {
        TreeModel treeModel = new TreeModel();
        List<Integer> apex = new ArrayList<>();
        int[] randomHist = new int[this.k];
        List<String> treeTable = new ArrayList<>();
        List<String> treeRow = new ArrayList<>();
        List<String> treeApex = new ArrayList<>();
        Random random = new Random();
        apex.add(0);
        boolean isEnd = false;
        int id = 1;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        List<Integer> childList = new ArrayList<>();
        DelegateTree<Integer, String> tree = new DelegateTree<>();
        tree.addVertex(id);
        apex.add(countApex(tree));
        queue.addLast(id);
        treeRow.add("1-0");
        treeTable.add(treeRow.toString());
        treeRow.clear();
        while (!isEnd || !queue.isEmpty()) {
            if (queue.isEmpty() && childList.isEmpty()) {
                break;
            }
            if (queue.isEmpty()) {
                treeTable.add(treeRow.toString());
                treeRow.clear();
                queue.addAll(childList);
                childList.clear();
            }
            int parent_id = queue.pollFirst();
            int countChild = isRegular ? (this.k - 1) : random.nextInt(this.k);
            if (countChild == 0) {
                treeApex.add(parent_id + "-" + tree.getParent(parent_id));
            }
            randomHist[countChild]++;
            for (int i = 0; i < countChild; i++) {
                id++;
                tree.addChild(id + "-" + parent_id, parent_id, id);
                apex.add(countApex(tree));
                childList.add(id);
                treeRow.add(id + "-" + parent_id);
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
        treeApex.addAll(treeRow);
        treeRow.clear();
        treeModel.setTree(tree);
        treeModel.setApexList(apex);
        treeModel.setRandomHist(randomHist);
        treeModel.setTreeTable(treeTable);
        treeModel.setApexTable(treeApex);
        return treeModel;
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
