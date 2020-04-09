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

	// my sector
	public int getSector() {
		int sector = 0;
		int sectorX = 0;
		int sectorY = 0;

		int[] position = this.getPosition();
		sectorX = position[0] / 5;
		sectorY = position[1] / 5;

		if ((sectorX == 0) && (sectorY == 0))
			sector = 1;
		if ((sectorX == 1) && (sectorY == 0))
			sector = 2;
		if ((sectorX == 2) && (sectorY == 0))
			sector = 3;
		if ((sectorX == 0) && (sectorY == 1))
			sector = 4;
		if ((sectorX == 1) && (sectorY == 1))
			sector = 5;
		if ((sectorX == 2) && (sectorY == 1))
			sector = 6;
		if ((sectorX == 0) && (sectorY == 2))
			sector = 7;
		if ((sectorX == 1) && (sectorY == 2))
			sector = 8;
		if ((sectorX == 2) && (sectorY == 2))
			sector = 9;

		return sector;
	}

	public boolean isDeadlock(char[][] cellValues, int wantedX, int wantedY) {
		if (((wantedX + 1) < cellValues.length) && (cellValues[wantedY][wantedX + 1] == '.')) {
			return false;
		} else if (((wantedY + 1) < cellValues.length) && (cellValues[wantedY + 1][wantedX] == '.')) {
			return false;
		} else if (((wantedX - 1) > -1) && (cellValues[wantedY][wantedX - 1] == '.')) {
			return false;
		} else if (((wantedY - 1) > -1) && (cellValues[wantedY - 1][wantedX] == '.')) {
			return false;
		} else
			return true;
	}

	public boolean move(char[][] cellValues) {
		int[] startPosition = this.getPosition();
		System.err.println("Actual poz before move: " + startPosition[0] + " " + startPosition[1]);
		int x = startPosition[0];
		int y = startPosition[1];
		List<Character> possibleDirs = new ArrayList<Character>();

		// possible direction
		if (((x + 1) < cellValues.length) && !isDeadlock(cellValues, x + 1, y) && (cellValues[y][x + 1] == '.'))
			possibleDirs.add('E');

		if (((y + 1) < cellValues.length) && !isDeadlock(cellValues, x, y + 1) && (cellValues[y + 1][x] == '.'))
			possibleDirs.add('S');

		if (((x - 1) > -1) && !isDeadlock(cellValues, x - 1, y) && (cellValues[y][x - 1] == '.'))
			possibleDirs.add('W');

		if (((y - 1) > -1) && !isDeadlock(cellValues, x, y - 1) && (cellValues[y - 1][x] == '.'))
			possibleDirs.add('N');

		System.err.print("Possible dirs: ");
		for (Character character : possibleDirs) {
			System.err.print(character);
		}
		System.err.println("");

		if (possibleDirs.size() == 0)
			return false;

		// measure heuristic value of possible directions
		int eastH = 0, southH = 0, westH = 0, northH = 0;

		// east
		if (possibleDirs.indexOf('E') != -1) {
			for (int yy = 0; yy < cellValues.length; yy++)
				for (int xx = x; xx < cellValues.length; xx++)
					if (cellValues[yy][xx] == '.')
						eastH++;
		}

		// south
		if (possibleDirs.indexOf('S') != -1) {
			for (int yy = y; yy < cellValues.length; yy++)
				for (int xx = 0; xx < cellValues.length; xx++)
					if (cellValues[yy][xx] == '.')
						southH++;
		}

		// west
		if (possibleDirs.indexOf('W') != -1) {
			for (int yy = 0; yy < cellValues.length; yy++)
				for (int xx = 0; xx < x; xx++)
					if (cellValues[yy][xx] == '.')
						westH++;
		}

		// north
		if (possibleDirs.indexOf('N') != -1) {
			for (int yy = 0; yy < y; yy++)
				for (int xx = 0; xx < cellValues.length; xx++)
					if (cellValues[yy][xx] == '.')
						northH++;
		}

		// Random r = new Random();
		// char chosenDir = possibleDirs.get(r.nextInt(possibleDirs.size()));

		int max = eastH;
		char chosenDir = 'E';
		if (southH > max) {
			max = southH;
			chosenDir = 'S';
		}
		if (westH > max) {
			max = westH;
			chosenDir = 'W';
		}
		if (northH > max) {
			max = northH;
			chosenDir = 'N';
		}

		System.err.println("Heuristic values: E " + eastH + " S " + southH + " W " + westH + " N " + northH);
		System.err.println("Chosen dir: " + chosenDir);

		this.setDir(chosenDir);
		switch (chosenDir) {
		case 'E':
			this.setPosition(new int[] { x + 1, y });
			cellValues[y][x + 1] = 'B';
			break;

		case 'S':
			this.setPosition(new int[] { x, y + 1 });
			cellValues[y + 1][x] = 'B';
			break;

		case 'W':
			this.setPosition(new int[] { x - 1, y });
			cellValues[y][x - 1] = 'B';
			break;

		case 'N':
			this.setPosition(new int[] { x, y - 1 });
			cellValues[y - 1][x] = 'B';
			break;

		default:
			break;
		}

		return true;

	}
}

class Player {

	public static class Sector {
		int id;
		int middlePointX;
		int middlePointY;

		public Sector(int id, int middlePointX, int middlePointY) {
			this.id = id;
			this.middlePointX = middlePointX;
			this.middlePointY = middlePointY;
		}
	}

	public static int[] distanceShips(int[] myPosition, int enemySector) {
		// x, y, distance
		int[] enemyShipData = new int[3];
		Sector[] sectors = new Sector[9];
		sectors[0] = new Sector(1, 2, 2);
		sectors[1] = new Sector(2, 7, 2);
		sectors[2] = new Sector(3, 12, 2);
		sectors[3] = new Sector(4, 2, 7);
		sectors[4] = new Sector(5, 7, 7);
		sectors[5] = new Sector(6, 12, 7);
		sectors[6] = new Sector(7, 2, 12);
		sectors[7] = new Sector(8, 7, 12);
		sectors[8] = new Sector(9, 12, 12);
		for (Sector sector : sectors) {
			if (sector.id == enemySector) {
				enemyShipData[0] = sector.middlePointX;
				enemyShipData[1] = sector.middlePointY;
				enemyShipData[2] = Math.max(Math.abs(myPosition[0] - enemyShipData[0]),
						Math.abs(myPosition[1] - enemyShipData[1]));
				break;
			}
		}
		return enemyShipData;
	}

	public static int getSectorToSonar(int mySector) {
		int sectorToSonar = 0;
		Random r = new Random();
		List<Integer> possibleSectors = new ArrayList<>();

		switch (mySector) {
		case 1:
			possibleSectors = Arrays.asList(2, 4);
			break;
		case 2:
			possibleSectors = Arrays.asList(1, 3, 5);
			break;
		case 3:
			possibleSectors = Arrays.asList(2, 6);
			break;
		case 4:
			possibleSectors = Arrays.asList(1, 5, 7);
			break;
		case 5:
			possibleSectors = Arrays.asList(2, 4, 6, 8);
			break;
		case 6:
			possibleSectors = Arrays.asList(3, 5, 9);
			break;
		case 7:
			possibleSectors = Arrays.asList(4, 8);
			break;
		case 8:
			possibleSectors = Arrays.asList(7, 5, 9);
			break;
		case 9:
			possibleSectors = Arrays.asList(8, 6);
			break;
		default:
			break;
		}
		sectorToSonar = possibleSectors.get(r.nextInt(possibleSectors.size()));
		return sectorToSonar;
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

		for (int y = 7; y < height; y++) {
			for (int x = 7; x < width; x++) {
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
		cellValues[startY][startX] = 'B';

		// chargeValue of Torpedo, Sonar, Silence
		int[] chargeToSoSi = new int[] { 0, 0, 0 };
		// game loop
		String enemyDir = "";
		int turn = 0;
		int sectorToSonar = 0;
		while (true) {
			turn++;
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
			String opponentOrder = "";
			int enemySector = 0;
			System.err.println(x);
			System.err.println(y);
			System.err.println("Sonar result: " + sonarResult);
			System.err.println(opponentOrders);
			String chargeString = "";
			int distanceShips = 100;
			int[] enemyShipData = new int[3];
			int enemySectorMiddlePointX = 0;
			int enemySectorMiddlePointY = 0;
			int mySector = 0;
			boolean sonarSuccess = false;
			boolean canMove = myShip.move(cellValues);
			if (canMove) {
				System.err.println("Charges: " + chargeToSoSi[0] + " " + chargeToSoSi[1] + " " + chargeToSoSi[2]);
				sonarSuccess = Boolean.valueOf(sonarResult);
				// we have got enemy sector with sonar
				if (sonarSuccess) {
					enemySector = sectorToSonar;
					enemyShipData = distanceShips(myShip.getPosition(), enemySector);
					System.err.println("xxxxxxxxxxxxxx Sonar info from enemy in sector: " + enemySector);
					// reset SectorToSonar
					sectorToSonar = 0;
				} else
					// if we got enemy sector from surface command
					for (int i = 0; i < opponentOrdersArray.length - 1; i++) {
						if (opponentOrdersArray[i].contains("SURFACE")) {
							enemySector = Integer.valueOf(opponentOrdersArray[i + 1]);
							System.err.println("xxxxxxxxxxxxxx Surface info from enemy in sector: " + enemySector);
							break;
						}
					}

				// got information about enemy with sonar command or surface command
				if (enemySector != 0) {
					enemyShipData = distanceShips(myShip.getPosition(), enemySector);
					enemySectorMiddlePointX = enemyShipData[0];
					enemySectorMiddlePointY = enemyShipData[1];
					distanceShips = enemyShipData[2];
					System.err.println("xxxxxxxxxxxxxx Enemy is in sector: " + enemySector + " , middle point is: "
							+ enemySectorMiddlePointX + " " + enemySectorMiddlePointY + " , distance is: "
							+ distanceShips);
				}

				// distance estimation with surface command
				if (distanceShips <= 4) {
					enemyX = enemySectorMiddlePointX;
					enemyY = enemySectorMiddlePointY;
					// if no enemy position info, then fire randomly
				} else if ((myShip.getPosition()[0] + 4) < cellValues.length
						&& cellValues[myShip.getPosition()[1]][myShip.getPosition()[0] + 4] != 'x') {
					enemyX = myShip.getPosition()[0] + 4;
					enemyY = myShip.getPosition()[1];
				} else if ((myShip.getPosition()[0] - 4) > -1
						&& cellValues[myShip.getPosition()[1]][myShip.getPosition()[0] - 4] != 'x') {
					enemyX = myShip.getPosition()[0] - 4;
					enemyY = myShip.getPosition()[1];
				} else if ((myShip.getPosition()[1] + 4) < cellValues.length
						&& cellValues[myShip.getPosition()[1] + 4][myShip.getPosition()[0]] != 'x') {
					enemyX = myShip.getPosition()[0];
					enemyY = myShip.getPosition()[1] + 4;
				} else if ((myShip.getPosition()[1] - 4) > -1
						&& cellValues[myShip.getPosition()[1] - 4][myShip.getPosition()[0]] != 'x') {
					enemyX = myShip.getPosition()[0];
					enemyY = myShip.getPosition()[1] - 4;
				} else {
					enemyX = 0;
					enemyY = 0;
				}
				// }
				System.err.println("Enemy poz: " + enemyX + " " + enemyY);
				System.err.println("Cellvalue at enemy poz: " + cellValues[enemyY][enemyX]);
				// if sonar and torpedo is ready
				if (chargeToSoSi[1] == 4 && chargeToSoSi[0] == 3) {
					mySector = myShip.getSector();
					sectorToSonar = getSectorToSonar(mySector);
					System.out
							.println("MOVE " + myShip.getDir() + " " + chargeString + " | " + "SONAR " + sectorToSonar);
					chargeToSoSi[1] = 0;
					// torpedo is charged, there is target, sonar is not charged
				} else if ((chargeToSoSi[0] == 3) && (enemyX != 0) && chargeToSoSi[1] < 4) {
					chargeToSoSi[0] = 0;
					chargeString = "SILENCE";
					chargeToSoSi[2]++;
					System.out.println("MOVE " + myShip.getDir() + " " + chargeString + " | " + "TORPEDO " + enemyX
							+ " " + enemyY);
					// if ready to go silence
				} else if (chargeToSoSi[2] == 6) {
					System.out.println("SILENCE " + myShip.getDir() + " 1");
					chargeToSoSi[2] = 0;
					// if none of above, then move and charge
				} else {
					if ((chargeToSoSi[2] < 6) && (turn % 4 == 0)) {
						chargeString = "SILENCE";
						chargeToSoSi[2]++;
					} else if ((chargeToSoSi[1] < 4) && (turn % 1 == 0)) {
						chargeString = "SONAR";
						chargeToSoSi[1]++;
					} else if ((chargeToSoSi[0] < 3)) {
						chargeString = "TORPEDO";
						chargeToSoSi[0]++;
					}
					System.out.println("MOVE " + myShip.getDir() + " " + chargeString);
				}
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

			/*
			 * for (int yy = 0; yy < height; yy++) { for (int xx = 0; xx < width; xx++) {
			 * System.err.print(cellValues[yy][xx]); if (xx == 14) System.err.print("\n");
			 * 
			 * } }
			 */

			System.err.println(myShip);
		}
	}
}