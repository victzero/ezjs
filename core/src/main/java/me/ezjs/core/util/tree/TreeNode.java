package me.ezjs.core.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vict on 2017/1/19.
 */
public class TreeNode {

    TreeNodeInterface obj;

    List<TreeNode> nodes;

    public TreeNode() {
        nodes = new ArrayList<>();
    }

    public TreeNode(TreeNodeInterface obj) {
        this();
        this.obj = obj;
    }

    public TreeNodeInterface getObj() {
        return obj;
    }

    public void setObj(TreeNodeInterface obj) {
        this.obj = obj;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public void add(TreeNode node) {
        nodes.add(node);
    }

}
