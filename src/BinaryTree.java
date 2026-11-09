import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

public class BinaryTree {

    TreeNode root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    // ===== 1. Construccion =====

    public void insertRoot(Object data) {
        if (root == null) {
            root = new TreeNode(data);
        }
    }

    public TreeNode search(Object data) {
        return search(root, data);
    }

    private TreeNode search(TreeNode node, Object data) {
        if (node == null || Objects.equals(node.data, data)) {
            return node;
        }
        TreeNode found = search(node.left, data);
        return found != null ? found : search(node.right, data);
    }

    public boolean insertLeft(Object parent, Object data) {
        TreeNode p = search(parent);
        if (p == null || p.left != null) {
            return false;
        }
        p.left = new TreeNode(data);
        return true;
    }

    public boolean insertRight(Object parent, Object data) {
        TreeNode p = search(parent);
        if (p == null || p.right != null) {
            return false;
        }
        p.right = new TreeNode(data);
        return true;
    }

    // Lee la raiz, luego por nivel los hijos de cada nodo
    // Linea vacia = ese hijo no existe
    public void buildTree(Scanner scanner) {
        root = null;
        System.out.print("Raiz: ");
        String value = readValue(scanner);
        if (value == null) {
            return;
        }
        insertRoot(value);

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            System.out.print("Hijo izquierdo de " + current.data + ": ");
            value = readValue(scanner);
            if (value != null) {
                current.left = new TreeNode(value);
                queue.add(current.left);
            }

            System.out.print("Hijo derecho de " + current.data + ": ");
            value = readValue(scanner);
            if (value != null) {
                current.right = new TreeNode(value);
                queue.add(current.right);
            }
        }
    }

    private String readValue(Scanner scanner) {
        String line = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        return (line.isEmpty() || line.equals("-")) ? null : line;
    }

    // ===== 2. Visualizacion =====

    public void printTree() {
        show(false);
    }

    private void show(boolean withFactors) {
        if (root == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        List<StringBuilder> rows = new ArrayList<>();
        int cell = maxLength(root, withFactors) + 1;
        draw(root, 0, new int[]{0}, cell, withFactors, rows);
        for (StringBuilder row : rows) {
            System.out.println(row);
        }
    }

    // Dibuja el subarbol y devuelve la columna del centro
    private int draw(TreeNode node, int depth, int[] next, int cell, boolean wf, List<StringBuilder> rows) {
        if (node == null) {
            return -1;
        }
        int leftCol = draw(node.left, depth + 1, next, cell, wf, rows);
        int col = next[0]++ * cell + cell / 2;
        int rightCol = draw(node.right, depth + 1, next, cell, wf, rows);

        String text = label(node, wf);
        int start = col - text.length() / 2;
        int end = start + text.length();
        put(rows, 2 * depth, start, text);
        if (leftCol >= 0) {
            put(rows, 2 * depth + 1, leftCol + 1, "/");
            put(rows, 2 * depth, leftCol + 2, "_".repeat(start - leftCol - 2));
        }
        if (rightCol >= 0) {
            put(rows, 2 * depth + 1, rightCol - 1, "\\");
            put(rows, 2 * depth, end, "_".repeat(rightCol - 1 - end));
        }
        return col;
    }

    private String label(TreeNode node, boolean withFactors) {
        return withFactors ? node.data + "(" + balanceFactor(node) + ")" : String.valueOf(node.data);
    }

    private int maxLength(TreeNode node, boolean withFactors) {
        if (node == null) {
            return 0;
        }
        return Math.max(label(node, withFactors).length(),
                Math.max(maxLength(node.left, withFactors), maxLength(node.right, withFactors)));
    }

    private void put(List<StringBuilder> rows, int r, int c, String s) {
        while (rows.size() <= r) {
            rows.add(new StringBuilder());
        }
        StringBuilder row = rows.get(r);
        while (row.length() < c + s.length()) {
            row.append(' ');
        }
        row.replace(c, c + s.length(), s);
    }

    // ===== 3. Recorridos =====

    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorder(root, sb);
        return sb.toString().trim();
    }

    private void preorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.data).append(" ");
        preorder(node.left, sb);
        preorder(node.right, sb);
    }

    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        return sb.toString().trim();
    }

    private void inorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inorder(node.left, sb);
        sb.append(node.data).append(" ");
        inorder(node.right, sb);
    }

    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorder(root, sb);
        return sb.toString().trim();
    }

    private void postorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        postorder(node.left, sb);
        postorder(node.right, sb);
        sb.append(node.data).append(" ");
    }

    // ===== 4. Factores de equilibrio =====

    public int height(TreeNode node) {
        return node == null ? 0 : 1 + Math.max(height(node.left), height(node.right));
    }

    // altura(derecho) - altura(izquierdo)
    public int balanceFactor(TreeNode node) {
        return node == null ? 0 : height(node.right) - height(node.left);
    }

    public boolean isBalanced() {
        return unbalancedNodes().isEmpty();
    }

    public List<Object> unbalancedNodes() {
        List<Object> result = new ArrayList<>();
        collectUnbalanced(root, result);
        return result;
    }

    private void collectUnbalanced(TreeNode node, List<Object> result) {
        if (node == null) {
            return;
        }
        if (Math.abs(balanceFactor(node)) > 1) {
            result.add(node.data);
        }
        collectUnbalanced(node.left, result);
        collectUnbalanced(node.right, result);
    }

    public void printBalanceFactors() {
        show(true);
    }

    // ===== 5. Balanceo AVL =====

    public TreeNode rotateRight(TreeNode node) {
        if (node == null || node.left == null) {
            return node;
        }
        TreeNode newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        return newRoot;
    }

    public TreeNode rotateLeft(TreeNode node) {
        if (node == null || node.right == null) {
            return node;
        }
        TreeNode newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        return newRoot;
    }

    public TreeNode rotateLeftRight(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    public TreeNode rotateRightLeft(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    public void balance() {
        if (root == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        System.out.println("--- Arbol original (factor de equilibrio entre parentesis) ---");
        printBalanceFactors();
        List<Object> unbalanced = unbalancedNodes();
        if (unbalanced.isEmpty()) {
            System.out.println("\nTodos los nodos tienen factor -1, 0 o 1: el arbol ya es AVL.");
            return;
        }
        System.out.println("\nNodos que requieren balanceo: " + unbalanced);
        System.out.println("Rotaciones aplicadas:");
        root = balanceNode(root);
        System.out.println("\n--- Arbol despues de balancear ---");
        printBalanceFactors();
    }

    // Balancea de abajo hacia arriba
    private TreeNode balanceNode(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.left = balanceNode(node.left);
        node.right = balanceNode(node.right);

        int bf = balanceFactor(node);
        if (bf >= -1 && bf <= 1) {
            return node;
        }

        Object name = node.data;
        String type;
        if (bf > 1 && balanceFactor(node.right) < 0) {
            type = "doble derecha-izquierda";
            node = rotateRightLeft(node);
        } else if (bf > 1) {
            type = "simple a la izquierda";
            node = rotateLeft(node);
        } else if (balanceFactor(node.left) > 0) {
            type = "doble izquierda-derecha";
            node = rotateLeftRight(node);
        } else {
            type = "simple a la derecha";
            node = rotateRight(node);
        }
        System.out.println("  - Nodo " + name + " (factor " + bf + "): rotacion " + type);
        return balanceNode(node);
    }
}
