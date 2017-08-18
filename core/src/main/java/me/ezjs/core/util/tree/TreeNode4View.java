package me.ezjs.core.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zjs-yd on 2017/1/22.
 */
public class TreeNode4View {
    private Integer value;//主键
    private String label;//展示名称
    private String code;//层级代码 #1#23#45
    private String fullLabel;// 各个层级的名称组合
    private List<TreeNode4View> nodes;

    public TreeNode4View() {
        nodes = new ArrayList<>();
    }

    public TreeNode4View(TreeNode root) {
        this();
        TreeNodeInterface obj = root.getObj();
        this.value = obj.getValue();
        this.label = obj.getLabel();
        this.code = obj.getCode();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TreeNode4View> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode4View> nodes) {
        this.nodes = nodes;
    }

    public void add(TreeNode4View node) {
        nodes.add(node);
    }

    public String getFullLabel() {
        return fullLabel;
    }

    public void setFullLabel(String fullLabel) {
        this.fullLabel = fullLabel;
    }
}
