import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeModel {
    private Tree<Integer, String> tree;
    private List<Integer> apexList;
    private int[] randomHist;
    private List<String> treeTable;
    private List<String> apexTable;

    public TreeModel(Tree<Integer, String> tree, List<Integer> apexList, int[] randomHist, List<String> treeTable, List<String> apexTable) {
        this.tree = tree;
        this.apexList = apexList;
        this.randomHist = randomHist;
        this.treeTable = treeTable;
        this.apexTable = apexTable;
    }

    public TreeModel() {
        this(new DelegateTree<Integer, String>(), new ArrayList<Integer>(), new int[0], new ArrayList<String>(), new ArrayList<String>());
    }

    public Tree<Integer, String> getTree() {
        return tree;
    }

    public void setTree(Tree<Integer, String> tree) {
        this.tree = tree;
    }

    public List<Integer> getApexList() {
        return apexList;
    }

    public void setApexList(List<Integer> apexList) {
        this.apexList = apexList;
    }

    public int[] getRandomHist() {
        return randomHist;
    }

    public void setRandomHist(int[] randomHist) {
        this.randomHist = randomHist;
    }
    public List<String> getTreeTable() {
        return treeTable;
    }

    public void setTreeTable(List<String> treeTable) {
        this.treeTable = treeTable;
    }

    public List<String> getApexTable() {
        return apexTable;
    }

    public void setApexTable(List<String> apexTable) {
        this.apexTable = apexTable;
    }
}
