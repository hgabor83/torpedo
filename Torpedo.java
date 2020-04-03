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
		if (((startPosition[0] + 1) < cellValues.length)
				&& (cellValues[startPosition[0] + 1][startPosition[1]] == '.')) {
			this.setPosition(new int[] { startPosition[0] + 1, startPosition[1] });
			this.setDir('E');
			cellValues[startPosition[0] + 1][startPosition[1]] = 'B';
		} else if (((startPosition[1] + 1) < cellValues.length)
				&& (cellValues[startPosition[0]][startPosition[1] + 1] == '.')) {
			this.setPosition(new int[] { startPosition[0], startPosition[1] + 1 });
			this.setDir('S');
			cellValues[startPosition[0]][startPosition[1] + 1] = 'B';
		} else if (((startPosition[0] - 1) > -1) && (cellValues[startPosition[0] - 1][startPosition[1]] == '.')) {
			this.setPosition(new int[] { startPosition[0] - 1, startPosition[1] });
			this.setDir('W');
			cellValues[startPosition[0] - 1][startPosition[1]] = 'B';
		} else if (((startPosition[1] - 1) > -1) && (cellValues[startPosition[0]][startPosition[1] - 1] == '.')) {
			this.setPosition(new int[] { startPosition[0], startPosition[1] - 1 });
			this.setDir('N');
			cellValues[startPosition[0]][startPosition[1] - 1] = 'B';
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
		// Map map = new Map(width, height);
		char[][] cellValues = new char[width][height];

		for (int i = 0; i < height; i++) {
			String line = in.nextLine();
			for (int j = 0; j < width; j++) {
				cellValues[j][i] = line.charAt(j);// set map
			}
		}

		int startX = 0, startY = 0;
		boolean foundStart = false;
		int enemyX = 0, enemyY = 13;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (cellValues[i][j] == '.') {
					startX = j;
					startY = i;
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
		int[] chargeToSoSi = new int[] { 0, 0, 0 };
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
				 * enemyY + "|" + "MOVE " + myShip.getDir() + " " + chargeString); // 4 charge
				 * needed for torpedo sometimes..fault I think chargeToSoSi[0] = 0; } else
				 */
				System.out.println("MOVE " + myShip.getDir() + " " + chargeString);
			} else {
				// surface mechanism
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						if ((cellValues[j][i] == 'B')
								&& !(j == myShip.getPosition()[0] && i == myShip.getPosition()[1]))
							cellValues[j][i] = '.';
					}
				}
				System.out.println("SURFACE");
			}

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					System.err.print(cellValues[j][i]);
					if (j == 14)
						System.err.print("\n");

				}
			}

			System.err.println(myShip);
		}
	}
}