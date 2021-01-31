import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private final int n;
    private int hammingDistance;
    private int manhattanDistance;
    private Tile blank;
    private Tile tile1;
    private Tile tile2;

    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        n = tiles.length;
        this.tiles = tiles;
        tile1 = getRandomTile(tiles);
        tile2 = getRandomTile(tiles, tile1);
        init(tiles);
    }

    public String toString() {
        StringBuilder boardStrBuilder = new StringBuilder();
        boardStrBuilder.append(tiles.length);
        for (int[] rows : tiles) {
            StringBuilder rowStrBuilder = new StringBuilder();
            for (int tile : rows) {
                rowStrBuilder.append("\t");
                rowStrBuilder.append(tile);
            }
            boardStrBuilder.append("\n");
            boardStrBuilder.append(rowStrBuilder);
        }
        return boardStrBuilder.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hammingDistance;
    }

    public int manhattan() {
        return manhattanDistance;
    }

    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if ((i == n - 1) && (j == n - 1)) {
                    return tile == 0;
                }
                if (tile != i * n + j + 1) {
                    return false;
                }
            }
        }
        return false;
    }

    public Iterable<Board> neighbors() {
        List<Board> boards = new ArrayList<>(4);
        if (blank.row + 1 < n) {
            boards.add(nextBoard(1, 0));
        }
        if (blank.row - 1 >= 0) {
            boards.add(nextBoard(-1, 0));
        }
        if (blank.col + 1 < n) {
            boards.add(nextBoard(0, 1));
        }
        if (blank.col - 1 >= 0) {
            boards.add(nextBoard(0, -1));
        }
        return boards;
    }

    public Board twin() {
        int[][] tilesArr = copyOf(tiles);
        tilesArr[tile1.row][tile1.col] = tile2.value;
        tilesArr[tile2.row][tile2.col] = tile1.value;
        return new Board(tilesArr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return n == board.n &&
                Arrays.deepEquals(tiles, board.tiles);
    }

    private void init(int[][] tilesArr) {
        int hammingCount = 0;
        int manhattanCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tilesArr[i][j];
                tiles[i][j] = tile;
                if (tile == 0) {
                    blank = new Tile(i, j, 0);
                    continue;
                }
                if (tile != i * n + j + 1) {
                    hammingCount++;
                }
                int expectedRow = getRow(tile);
                int expectedCol = getCol(tile);
                if (expectedRow != i || expectedCol != j) {
                    manhattanCount += Math.abs(i - expectedRow) + Math.abs(j - expectedCol);
                }
            }
        }
        hammingDistance = hammingCount;
        manhattanDistance = manhattanCount;
    }

    private int getCol(int tail) {
        return ((tail % n == 0) ? n : (tail % n)) - 1;
    }

    private int getRow(int tail) {
        return ((tail % n == 0) ? (tail / n) : (1 + tail / n)) - 1;
    }

    private Board nextBoard(int dRow, int dCol) {
        int[][] tilesArr = copyOf(this.tiles);
        tilesArr[blank.row][blank.col] = tilesArr[blank.row + dRow][blank.col + dCol];
        tilesArr[blank.row + dRow][blank.col + dCol] = 0;
        Board board = new Board(tilesArr);
        return board;
    }

    private static int[][] copyOf(int[][] arr) {
        int[][] ints = new int[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                ints[i][j] = arr[i][j];
            }
        }
        return ints;
    }

    private static Tile getRandomTile(int[][] tilesArr) {
        int n = tilesArr.length;
        while (true) {
            int row = StdRandom.uniform(n);
            int col = StdRandom.uniform(n);
            if (tilesArr[row][col] != 0) {
                return new Tile(row, col, tilesArr[row][col]);
            }
        }
    }

    private static Tile getRandomTile(int[][] tilesArr, Tile except) {
        while (true) {
            Tile tile = getRandomTile(tilesArr);
            if (!tile.equals(except)) {
                return tile;
            }
        }
    }

    private static class Tile {
        private int row;
        private int col;
        private int value;

        public Tile(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row &&
                    col == tile.col &&
                    value == tile.value;
        }
    }

    public static void main(String[] args) {
    }

}
