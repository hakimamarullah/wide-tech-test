/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/13/2024 4:34 PM
@Last Modified 6/13/2024 4:34 PM
Version 1.0
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {

    public static void main(String[] args) {
        List<Map<String, String>> data = new ArrayList<>();
        data.add(createEntry("a", "0"));
        data.add(createEntry("b", "a"));
        data.add(createEntry("c", "a"));
        data.add(createEntry("cc", "a"));
        data.add(createEntry("d", "b"));
        data.add(createEntry("e", "b"));
        data.add(createEntry("f", "c"));
        data.add(createEntry("g", "c"));
        data.add(createEntry("gg", "cc"));
        data.add(createEntry("h", "d"));
        data.add(createEntry("i", "d"));
        data.add(createEntry("ii", "gg"));
        data.add(createEntry("j", "h"));
        data.add(createEntry("k", "h"));
        data.add(createEntry("hh", "ii"));
        data.add(createEntry("kk", "hh"));

        TreeData tree = new TreeData();
        tree.buildTree(data);

        System.out.println(tree.maxDepth("a"));
        System.out.println(tree.maxDepth("cc"));
        System.out.println(tree.maxDepth("e"));
    }



    private static Map<String, String> createEntry(String id, String upline) {
        Map<String, String> entry = new HashMap<>();
        entry.put("id", id);
        entry.put("upline", upline);
        return entry;
    }
}

class TreeNode {
    List<TreeNode> child;

    String key;

    public TreeNode(String key) {
        this.key = key;
        child = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        this.child.add(child);
    }
}

class TreeData {
    TreeNode root;
    Map<String, TreeNode> tree = new HashMap<>();

    public void buildTree(List<Map<String, String>> nodes) {
        for (Map<String, String> node : nodes) {
            String id = node.get("id");
            String upline = node.get("upline");

            TreeNode treeNode = new TreeNode(id);
            tree.put(id, treeNode);

            if (upline.equals("0")) {
                root = tree.get(id);
            }

        }

        for (Map<String, String> node : nodes) {
            String id = node.get("id");
            String upline = node.get("upline");



            if (!upline.equals("0")) {
                TreeNode parent = tree.get(upline);
                TreeNode child = tree.get(id);

                parent.addChild(child);
            }

        }
    }

    public int maxDepth(String rootKey) {
        return maxDepth(tree.get(rootKey));
    }

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int maxDepth = 0;
        for (TreeNode child : root.child) {
            maxDepth = Math.max(maxDepth, maxDepth(child));
        }
        return maxDepth + 1;
    }
}
