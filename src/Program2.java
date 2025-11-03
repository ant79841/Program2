import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *  runner class for Program2.
 * Run Program2.main() to start a race.
 *
 *
 */
public class Program2 {

    public static void main(String[] args) {
        // Create racetrack (example size: 11 rows x 15 cols)
        Racetrack track = new Racetrack(11, 15);

        // Create three cars (their move order is the order of construction)
        GridCar car1 = new GridCar('1');
        GridCar car2 = new GridCar('2');
        GridCar car3 = new GridCar('3');

        // Place cars at highest-weight cells (farthest from finish)
        List<int[]> starts = track.getHighestWeightPositions(3);
        car1.updateCoordinates(starts.get(0)[0], starts.get(0)[1]);
        car2.updateCoordinates(starts.get(1)[0], starts.get(1)[1]);
        car3.updateCoordinates(starts.get(2)[0], starts.get(2)[1]);

        // set initial weight position and place on track
        car1.setWeightPosition(track.getWeight(car1.getRow(), car1.getCol()));
        car2.setWeightPosition(track.getWeight(car2.getRow(), car2.getCol()));
        car3.setWeightPosition(track.getWeight(car3.getRow(), car3.getCol()));

        car1.setCarMove(track);
        car2.setCarMove(track);
        car3.setCarMove(track);

        // initial display
        System.out.println("Cars Start Race");
        track.printTrack();

        // race loop
        boolean someoneWon = false;
        List<GridCar> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        while (!someoneWon) {
            // move each car in their creation order
            for (GridCar c : cars) {
                System.out.println("*****CAR " + c.getIdNumber() + "'s TURN!*****");
                c.move(track);
                // detect winner
                if (c.getWinner()) {
                    someoneWon = true;
                    break;
                }
            }

            // after each round, display
            System.out.println("Cars Moving with Winner (if any)");
            track.printTrack();

            // update and display sorted place by weightPosition (lower weight = ahead)
            cars.sort(Comparator.comparingInt(GridCar::getWeightPosition));
            System.out.println("Cars Information");
            int place = 1;
            for (GridCar c : cars) {
                c.DisplayCarInfo(place++);
            }

            // stop if someone won
            if (someoneWon) break;
        }

        System.out.println("Race ended.");
    }
}
