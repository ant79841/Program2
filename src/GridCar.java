import java.util.ArrayList;
import java.util.List;

/**
 * Move strategy:
 *  - Enumerate reachable cells within rowVelocity and colVelocity ranges.
 *  - Skip borders and occupied cells (except allow moving to finish).
 *  - Choose the reachable cell with the smallest weight (closest to finish).
 */
public class GridCar extends Car {

    public GridCar(char id) {
        super(id);
    }

    @Override
    public void move(Racetrack track) {
        // If already winner, do nothing
        if (this.getWinner()) return;

        int curR = this.getRow();
        int curC = this.getCol();
        int rRange = Math.min(this.getRowVelocity(), this.getMaxSpeed());
        int cRange = Math.min(this.getColVelocity(), this.getMaxSpeed());

        List<int[]> candidates = new ArrayList<>();
        for (int dr = -rRange; dr <= rRange; dr++) {
            for (int dc = -cRange; dc <= cRange; dc++) {
                int nr = curR + dr;
                int nc = curC + dc;
                // skip same cell
                if (nr == curR && nc == curC) continue;
                // skip out of bounds
                if (nr < 0 || nc < 0 || nr >= track.getRows() || nc >= track.getCols()) continue;
                // skip border or occupied (allow finish even if it's 'F')
                if (track.isOccupied(nr, nc) && !track.isFinish(nr, nc)) continue;
                candidates.add(new int[]{nr, nc});
            }
        }

        if (candidates.isEmpty()) {
            // no move possible
            return;
        }

        // choose candidate with smallest weight
        int bestR = -1, bestC = -1;
        int bestW = Integer.MAX_VALUE;
        for (int[] p : candidates) {
            int w = track.getWeight(p[0], p[1]);
            if (w < bestW) {
                bestW = w;
                bestR = p[0];
                bestC = p[1];
            }
        }

        if (bestR >= 0) {
            // move to best position
            this.updateCarInfo(track, bestR, bestC);
            // set winner if landed on finish
            if (track.isFinish(bestR, bestC)) {
                this.setWinner(true);
                System.out.println("Car " + this.getIdNumber() + " WINS!");
            }
        }
    }
}
