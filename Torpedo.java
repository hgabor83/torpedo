import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/

class Ship {
	int[] position = new int[2];
	char dir;
	int id;

	public Ship(int[] position, char dir, int id) {
		this.position = position;
		this.dir = dir;
		this.id = id;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public char getDir() {
		return dir;
	}

	public void setDir(char dir) {
		this.dir = dir;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Ship [position=" + Arrays.toString(position) + ", dir=" + dir + ", id=" + id + "]";
	}

	public boolean move(char[][] cellValues) {
		int[] startPosition = this.getPosition();
		System.err.println("Actual poz before move: " + startPosition[0] + " " + startPosition[1]);
		int x = startPosition[0];
		int y = startPosition[1];
		if (((x + 1) < cellValues.length) && (cellValues[y][x + 1] == '.')) {
			this.setPosition(new int[] { x + 1, y });
			this.setDir('E');
			cellValues[y][x + 1] = 'B';
		} else if (((y + 1) < cellValues.length) && (cellValues[y + 1][x] == '.')) {
			this.setPosition(new int[] { x, y + 1 });
			this.setDir('S');
			cellValues[y + 1][x] = 'B';
		} else if (((x - 1) > -1) && (cellValues[y][x - 1] == '.')) {
			this.setPosition(new int[] { x - 1, y });
			this.setDir('W');
			cellValues[y][x - 1] = 'B';
		} else if (((y - 1) > -1) && (cellValues[y - 1][x] == '.')) {
			this.setPosition(new int[] { x, y - 1 });
			this.setDir('N');
			cellValues[y - 1][x] = 'B';
		} else
			return false;
		return true;

	}

}

class Player {

	public static int distanceShips(int[] myPosition, int[] enemyPosition) {
		return Math.max(Math.abs(myPosition[0] - enemyPosition[0]), Math.abs(myPosition[1] - enemyPosition[1]));
	}

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int width = in.nextInt();
		int height = in.nextInt();
		int myId = in.nextInt();
		if (in.hasNextLine()) {
			in.nextLine();
		}

		// height=y width=x
		char[][] cellValues = new char[height][width];

		for (int y = 0; y < height; y++) {
			String line = in.nextLine();
			for (int x = 0; x < width; x++) {
				cellValues[y][x] = line.charAt(x);// set map
			}
		}

		int startX = 0, startY = 0;
		boolean foundStart = false;
		int enemyX = 0, enemyY = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (cellValues[y][x] == '.') {
					startX = x;
					startY = y;
					foundStart = true;
					break;
				}
			}
			if (foundStart)
				break;
		}

		// Write an action using System.out.println()
		// To debug: System.err.println("Debug messages...");

		System.out.println(startX + " " + startY);

		Ship myShip = new Ship(new int[] { startX, startY }, ' ', myId);

		// chargeValue of Torpedo, Sonar, Silence
		int[] chargeToSoSi = new int[] { -1, -1, -1 };
		// game loop
		String enemyDir = "";
		while (true) {
			int x = in.nextInt();
			int y = in.nextInt();
			int myLife = in.nextInt();
			int oppLife = in.nextInt();
			int torpedoCooldown = in.nextInt();
			int sonarCooldown = in.nextInt();
			int silenceCooldown = in.nextInt();
			int mineCooldown = in.nextInt();
			String sonarResult = in.next();
			if (in.hasNextLine()) {
				in.nextLine();
			}
			String opponentOrders = in.nextLine();
			String[] opponentOrdersArray = opponentOrders.split(" ");
			if (opponentOrdersArray.length > 1)
				// if silence, then the prev dir will applied
				enemyDir = opponentOrdersArray[1];
			switch (enemyDir) {
			case "E":
				enemyX += 1;
				break;
			case "S":
				enemyY += 1;
				break;
			case "W":
				enemyX -= 1;
				break;
			case "N":
				enemyY -= 1;
				break;
			default:
				break;
			}

			System.err.println("Enemy poz: " + enemyX + " " + enemyY);

			System.err.println(x);
			System.err.println(y);
			System.err.println(sonarResult);
			System.err.println(opponentOrders);
			String chargeString = "";

			if ((distanceShips(myShip.getPosition(), new int[] { enemyX, enemyY }) <= 4) && (chargeToSoSi[0] == 3)) {
				System.out.println("TORPEDO " + enemyX + " " + enemyY);
				chargeToSoSi[0] = -1;
			} else {

				boolean canMove = myShip.move(cellValues);
				if (canMove) {
					if (chargeToSoSi[0] < 3) {
						chargeString = "TORPEDO";
						chargeToSoSi[0]++;
					} else if (chargeToSoSi[1] < 4) {
						chargeString = "SONAR";
						chargeToSoSi[1]++;
					} else if (chargeToSoSi[2] < 6) {
						chargeString = "SILENCE";
						chargeToSoSi[2]++;

					}

					System.err.println("Charges: " + chargeToSoSi[0] + " " + chargeToSoSi[1] + " " + chargeToSoSi[2]);
					// if enemy is near, fire

					/*
					 * if ((distanceShips(myShip.getPosition(), new int[] { enemyX, enemyY }) <= 4)
					 * && (chargeToSoSi[0] == 3)) { System.out.println( "TORPEDO " + enemyX + " " +
					 * enemyY + "|" + "MOVE " + myShip.getDir() + " " + chargeString);
					 * chargeToSoSi[0] = -1; } else
					 */

					// if (chargeToSoSi[2] == 6) {
					// System.out.println("SILENCE " + myShip.getDir() + " 1");
					// chargeToSoSi[2] = -1;
					// } else
					System.out.println("MOVE " + myShip.getDir() + " " + chargeString);
				} else {
					// surface mechanism
					for (int yy = 0; yy < height; yy++) {
						for (int xx = 0; xx < width; xx++) {
							if ((cellValues[yy][xx] == 'B')
									&& !(xx == myShip.getPosition()[0] && yy == myShip.getPosition()[1]))
								cellValues[yy][xx] = '.';
						}
					}
					System.out.println("SURFACE");
				}
			}

			for (int yy = 0; yy < height; yy++) {
				for (int xx = 0; xx < width; xx++) {
					System.err.print(cellValues[yy][xx]);
					if (xx == 14)
						System.err.print("\n");

				}
			}

			System.err.println(myShip);
		}
	}
}