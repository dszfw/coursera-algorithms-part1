import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final Board initialBoard;
    private Stack<Board> solutionGameTree;

    public Solver(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        this.initialBoard = board;
        solvePuzzle();
    }

    private void solvePuzzle() {
        Comparator<Node> cr = (n1, n2) ->
                n1.board.manhattan() + n1.moves
                        - n2.board.manhattan() - n2.moves;
        MinPQ<Node> pq = new MinPQ<>(1, cr);
        MinPQ<Node> tpq = new MinPQ<>(1, cr);
        pq.insert(new Node(initialBoard));
        tpq.insert(new Node(initialBoard.twin()));
        Board prevBoard = null;
        Board tPrevBoard = null;
        while (true) {
            Node node = pq.delMin();
            Node tNode = tpq.delMin();
            Board board = node.board;
            Board tBoard = tNode.board;

            if (board.isGoal()) {
                solutionGameTree = new Stack<>();
                solutionGameTree.push(board);
                while (node.parent != null) {
                    node = node.parent;
                    solutionGameTree.push(node.board);
                }
                break;
            }

            if (tBoard.isGoal()) {
                break;
            }

            for (Board b : board.neighbors()) {
                if (b.equals(prevBoard)) {
                    continue;
                }
                pq.insert(node.move(b));
            }

            for (Board b : tBoard.neighbors()) {
                if (b.equals(tPrevBoard)) {
                    continue;
                }
                tpq.insert(tNode.move(b));
            }

            prevBoard = board;
            tPrevBoard = tBoard;
        }
    }

    /**
     * is the initial board solvable? (see below)
     */
    public boolean isSolvable() {
        return solutionGameTree != null;
    }

    /**
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        return solutionGameTree != null ? solutionGameTree.size() - 1 : -1;
    }

    /**
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        return solutionGameTree;
    }

    private static class Node {
        private Board board;
        private Node parent;
        private int moves;

        public Node() {
        }

        public Node(Board board) {
            this.board = board;
        }

        public Node move(Board board) {
            Node node = new Node();
            node.board = board;
            node.parent = this;
            node.moves = this.moves + 1;
            return node;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
