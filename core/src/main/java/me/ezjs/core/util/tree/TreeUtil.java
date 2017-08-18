package me.ezjs.core.util.tree;

import java.util.List;

/**
 * Created by Vict on 2017/1/19.
 */
public class TreeUtil {
    public static void print(TreeNode root) {
        System.out.println(getBlankByLevel(0) + root.getObj().getValue() + " > " + root.getObj().toString());
        List<TreeNode> nodes = root.getNodes();
        print(nodes, 1);
    }

    private static void print(List<TreeNode> nodes, int level) {
        for (int i = 0; i < nodes.size(); i++) {
            TreeNode node = nodes.get(i);
            System.out.println(getBlankByLevel(level) + node.getObj().getValue() + " > " + node.getObj().toString());
            print(node.getNodes(), level + 1);
        }
    }

    private static String getBlankByLevel(int level) {
        String b = "+";
        for (int i = 0; i < level; i++) {
            b += "--";
        }
        return b;
    }

    public static TreeNode4View parse2ViewData(TreeNode root) {
        TreeNode4View viewRoot = new TreeNode4View(root);
        viewRoot.setFullLabel(viewRoot.getLabel());
        parseChildren(root, viewRoot);
        return viewRoot;
    }

    private static void parseChildren(TreeNode root, TreeNode4View viewRoot) {
        List<TreeNode> nodes = root.getNodes();
        for (TreeNode node : nodes) {
            TreeNode4View v = new TreeNode4View(node);
            v.setFullLabel(viewRoot.getFullLabel() + " / " + v.getLabel());
            viewRoot.add(v);
            parseChildren(node, v);
        }
    }
}
