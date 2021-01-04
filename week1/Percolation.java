import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final boolean[][] grid;
    private final int topNode;
    private final int bottomNode;
    private final int topLeftCorner;
    private final int topRightCorner;
    private final int bottomLeftCorner;
    private final int bottomRightCorner;
    private final WeightedQuickUnionUF uf1;
    private final WeightedQuickUnionUF uf2;

    private int openSitesCounter;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N <= 0");
        }
        this.n = n;
        grid = new boolean[n][n];
        int ufElementsCount = n * n + 2;
        topNode = 0;
        bottomNode = ufElementsCount - 1;
        topLeftCorner = 1;
        topRightCorner = n;
        bottomLeftCorner = n * n - n + 1;
        bottomRightCorner = n * n;
        uf1 = new WeightedQuickUnionUF(ufElementsCount);
        uf2 = new WeightedQuickUnionUF(ufElementsCount);
        for (int i = 1; i <= n; i++) {
            uf1.union(i, topNode);
            uf2.union(i, topNode);
            int tailNode = ufElementsCount - 1 - i;
            uf1.union(tailNode, bottomNode);
        }
    }

    public void open(int row, int col) {
        validate(row);
        validate(col);
        if (isOpen(row, col)) return;
        setGridElement(row, col, true);
        openSitesCounter++;
        int ufElement = getUfElement(row, col);
        int[] successors = getSuccessors(ufElement);
        for (int i = 0; i < successors.length; i++) {
            int successor = successors[i];
            int sRow = getRow(successor);
            int sCol = getCol(successor);
            if (!isOpen(sRow, sCol)) {
                continue;
            }
            if (isFull(sRow, sCol)) {
                uf1.union(ufElement, successor);
                uf2.union(ufElement, successor);
            }
            else {
                uf1.union(successor, ufElement);
                uf2.union(successor, ufElement);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        return getGridElement(row, col);
    }

    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);
        int node = getUfElement(row, col);
        return isOpen(getRow(node), getCol(node))
                && (uf2.find(topNode) == uf2.find(node));
    }

    public int numberOfOpenSites() {
        return openSitesCounter;
    }

    public boolean percolates() {
        if (n != 1) {
            return uf1.find(topNode) == uf1.find(bottomNode);
        }
        return isFull(1, 1);
    }

    private boolean getGridElement(int row, int col) {
        return grid[row - 1][col - 1];
    }

    private void setGridElement(int row, int col, boolean value) {
        grid[row - 1][col - 1] = value;
    }

    private void validate(int value) {
        if (value <= 0 || value > n) {
            throw new IllegalArgumentException("Value is not in scope [1.." + n + "]");
        }
    }

    private int getCol(int node) {
        return (node % n == 0) ? n : (node % n);
    }

    private int getRow(int node) {
        if (n == 1) return 1;
        return (node % n == 0) ? (node / n) : (1 + node / n);
    }

    private int getUfElement(int row, int col) {
        return (row - 1) * n + col;
    }

    private int[] getSuccessors(int node) {
        if (isCornerNode(node)) {
            return getCornerSuccessors(node);
        }
        else if (isBorderNode(node)) {
            return getBorderSuccessors(node);
        }
        else {
            return new int[] { node - 1, node + 1, node - n, node + n };
        }
    }

    private int[] getBorderSuccessors(int node) {
        if (node > topLeftCorner && node < topRightCorner) {
            return new int[] { node - 1, node + 1, node + n };
        }
        else if (node > bottomLeftCorner && node < bottomRightCorner) {
            return new int[] { node - 1, node + 1, node - n };
        }
        else if (node % n == 1) {
            // left border
            return new int[] { node + n, node - n, node + 1 };
        }
        else if (node % n == 0) {
            // right border
            return new int[] { node + n, node - n, node - 1 };
        }
        else {
            throw new IllegalArgumentException("Node [" + node + "] is not a border node");
        }
    }

    private int[] getCornerSuccessors(int node) {
        if (node == topLeftCorner) {
            return new int[] { node + 1, node + n };
        }
        else if (node == topRightCorner) {
            return new int[] { node - 1, node + n };
        }
        else if (node == bottomLeftCorner) {
            return new int[] { node - n, node + 1 };
        }
        else if (node == bottomRightCorner) {
            return new int[] { node - n, node - 1 };
        }
        else {
            throw new IllegalArgumentException("Node [" + node + "] is not a corner node");
        }
    }

    private boolean isBorderNode(int node) {
        return (node > topLeftCorner && node < topRightCorner)
                || (node > bottomLeftCorner && node < bottomRightCorner)
                || ((node % n == 1 || node % n == 0) && !isCornerNode(node));
    }

    private boolean isCornerNode(int node) {
        return node == topLeftCorner
                || node == topRightCorner
                || node == bottomLeftCorner
                || node == bottomRightCorner;
    }

    // test client (optional)
    public static void main(String[] args) {
    }

}
