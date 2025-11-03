import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * - weight = Manhattan distance to finish (smaller = closer to finish)
 * - map holds characters: 'X' border, 'F' finish, ' ' empty, 'T' trail, or car id.
 */
public class Racetrack {
    private final int rows;
    private final int cols;
    private final int[][] weight;
    private final char[][] map;
    private final int finishRow;
    private final int finishCol;

    public Racetrack(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        weight = new int[rows][cols];
        map = new char[rows][cols];

        // Choose finish roughly near right-middle
        finishRow = rows / 2;
        finishCol = cols - 2;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1) {
                    map[r][c] = 'X';      // border
                    weight[r][c] = Integer.MAX_VALUE / 2;
                } else {
                    map[r][c] = ' ';      // empty
                }
                // compute Manhattan distance as weight (lower = closer)
                weight[r][c] = Math.abs(r - finishRow) + Math.abs(c - finishCol);
            }
        }
        // mark finish
        map[finishRow][finishCol] = 'F';
        weight[finishRow][finishCol] = 0;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    // Return weight for given cell
    public int getWeight(int r, int c) {
        if (r < 0 || c < 0 || r >= rows || c >= cols) return Integer.MAX_VALUE / 2;
        return weight[r][c];
    }

    // Set the display character for a cell (e.g., car id, 'T', 'F' etc.)
    public void setTrack(int r, int c, char ch) {
        if (r < 0 || c < 0 || r >= rows || c >= cols) return;
        // don't overwrite border 'X'
        if (map[r][c] == 'X') return;
        map[r][c] = ch;
    }

    // Return a list of top N positions with highest weights (farthest from finish)
    public List<int[]> getHighestWeightPositions(int n) {
        List<int[]> list = new ArrayList<>();
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                // skip the finish cell
                if (r == finishRow && c == finishCol) continue;
                list.add(new int[]{r, c});
            }
        }
        // sort descending by weight (largest distance first)
        Collections.sort(list, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(getWeight(b[0], b[1]), getWeight(a[0], a[1]));
            }
        });
        List<int[]> out = new ArrayList<>();
        int i = 0;
        for (int[] p : list) {
            out.add(p);
            i++;
            if (i >= n) break;
        }
        return out;
    }

    // Print track: print char for non-empty, weight for empty
    public void printTrack() {
        System.out.println();
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                char ch = map[r][c];
                if (ch != ' ') {
                    sb.append(String.format("%3s", ch));
                } else {
                    sb.append(String.format("%3d", weight[r][c]));
                }
            }
            System.out.println(sb.toString());
        }
        System.out.println();
    }

    // Helper to check if a cell is occupied by a car. 'F' and 'T' and ' ' are not considered occupied.
    public boolean isOccupied(int r, int c) {
        if (r < 0 || c < 0 || r >= rows || c >= cols) return true;
        char ch = map[r][c];
        if (ch == ' ' || ch == 'F' || ch == 'T') return false;
        // if border X treat as occupied
        if (ch == 'X') return true;
        return true; // any other char (car id) = occupied
    }

    // Return if cell is the finish cell
    public boolean isFinish(int r, int c) {
        return (r == finishRow && c == finishCol);
    }
}
