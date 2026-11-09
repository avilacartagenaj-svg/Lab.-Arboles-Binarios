import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Ejemplo: arbol del enunciado
        //            A
        //          /   \
        //         B     C
        //        / \   / \
        //       D   E F   G
        //      / \
        //     H   I
        TreeNode a = new TreeNode("A");
        BinaryTree tree = new BinaryTree(a);

        a.left = new TreeNode("B");
        a.right = new TreeNode("C");
        a.left.left = new TreeNode("D");
        a.left.right = new TreeNode("E");
        a.right.left = new TreeNode("F");
        a.right.right = new TreeNode("G");
        a.left.left.left = new TreeNode("H");
        a.left.left.right = new TreeNode("I");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("""

                    ========== MENU ==========
                    1. Construir arbol
                    2. Mostrar arbol
                    3. Recorrido Preorden
                    4. Recorrido Inorden
                    5. Recorrido Posorden
                    6. Mostrar factores de equilibrio
                    7. Balancear arbol (AVL)
                    8. Salir
                    ==========================
                    Seleccione una opcion:\s""");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    tree.buildTree(scanner);
                    tree.printTree();
                }
                case "2" -> tree.printTree();
                case "3" -> System.out.println("Preorden: " + tree.preorder());
                case "4" -> System.out.println("Inorden: " + tree.inorder());
                case "5" -> System.out.println("Posorden: " + tree.postorder());
                case "6" -> tree.printBalanceFactors();
                case "7" -> tree.balance();
                case "8" -> {
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
}
