import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/

class Cell {
	int x;
	int y;
	char value;
	boolean walkable;

	public Cell(int x, int y, char value) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.walkable = true;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

}

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

	public boolean move(char[][] cellValues) {
		int[] startPosition = this.getPosition();
		System.err.println("Actual poz before move: " + startPosition[0] + " " + startPosition[1]);
		int x = startPosition[0];
		int y = startPosition[1];

		// measure heuristic value of possible directions
		int eastH = 0, southH = 0, westH = 0, northH = 0;

		setCells(cellValues);
		eastH = cellCount(x + 1, y);

		setCells(cellValues);
		southH = cellCount(x, y + 1);

		setCells(cellValues);
		westH = cellCount(x - 1, y);

		setCells(cellValues);
		northH = cellCount(x, y - 1);

		if (eastH == 0 && westH == 0 && southH == 0 && northH == 0)
			return false;

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
		switch (chosenDir)

		{
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

	public Cell[][] cells = new Cell[15][15];

	// actual map load to cells array
	public void setCells(char[][] cellValues) {
		for (int i = 0; i < cellValues.length; i++)
			for (int j = 0; j < cellValues.length; j++) {
				cells[j][i] = new Cell(j, i, cellValues[j][i]);
			}
	}

	// count the free cells from a certain cell
	public int cellCount(int x, int y) {
		if (x < 15 && x > -1 && y < 15 && y > -1 && cells[y][x].getValue() == '.' && cells[y][x].isWalkable()) {
			cells[y][x].setWalkable(false);
			return (1 + cellCount(x + 1, y) + cellCount(x - 1, y) + cellCount(x, y + 1) + cellCount(x, y - 1));
		} else
			return 0;
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

	public static int[] distanceToSectorMP(int[] myPosition, int enemySector) {
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

	// get sector direction
	public static char directionToSector(int mySector, int enemySector) {
		char direction = ' ';

		switch (mySector) {
		case 1:
			switch (enemySector) {
			case 2:
				direction = 'E';
				break;
			case 4:
				direction = 'S';
				break;
			default:
				break;
			}
			break;
		case 2:
			switch (enemySector) {
			case 1:
				direction = 'W';
				break;
			case 3:
				direction = 'E';
				break;
			case 5:
				direction = 'S';
				break;
			default:
				break;
			}
			break;
		case 3:
			switch (enemySector) {
			case 2:
				direction = 'W';
				break;
			case 6:
				direction = 'S';
				break;
			default:
				break;
			}
			break;
		case 4:
			switch (enemySector) {
			case 1:
				direction = 'N';
				break;
			case 5:
				direction = 'E';
				break;
			case 7:
				direction = 'S';
				break;
			default:
				break;
			}
			break;
		case 5:
			switch (enemySector) {
			case 2:
				direction = 'N';
				break;
			case 4:
				direction = 'W';
				break;
			case 6:
				direction = 'E';
				break;
			case 8:
				direction = 'S';
				break;
			default:
				break;
			}
			break;
		case 6:
			switch (enemySector) {
			case 3:
				direction = 'N';
				break;
			case 5:
				direction = 'W';
				break;
			case 9:
				direction = 'E';
				break;
			default:
				break;
			}
			break;
		case 7:
			switch (enemySector) {
			case 4:
				direction = 'N';
				break;
			case 8:
				direction = 'E';
				break;
			default:
				break;
			}
			break;
		case 8:
			switch (enemySector) {
			case 7:
				direction = 'W';
				break;
			case 5:
				direction = 'N';
				break;
			case 9:
				direction = 'E';
				break;
			default:
				break;
			}
			break;
		case 9:
			switch (enemySector) {
			case 8:
				direction = 'W';
				break;
			case 6:
				direction = 'N';
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return direction;
	}

	public static int getSectorToSonar(int mySector, int[] myPosition) {
		int sectorToSonar = 0;
		/*
		 * Random r = new Random(); List<Integer> possibleSectors = new ArrayList<>();
		 * 
		 * switch (mySector) { case 1: possibleSectors = Arrays.asList(2, 4); break;
		 * case 2: possibleSectors = Arrays.asList(1, 3, 5); break; case 3:
		 * possibleSectors = Arrays.asList(2, 6); break; case 4: possibleSectors =
		 * Arrays.asList(1, 5, 7); break; case 5: possibleSectors = Arrays.asList(2, 4,
		 * 6, 8); break; case 6: possibleSectors = Arrays.asList(3, 5, 9); break; case
		 * 7: possibleSectors = Arrays.asList(4, 8); break; case 8: possibleSectors =
		 * Arrays.asList(7, 5, 9); break; case 9: possibleSectors = Arrays.asList(8, 6);
		 * break; default: break; } sectorToSonar =
		 * possibleSectors.get(r.nextInt(possibleSectors.size()));
		 */

		// better to find nearest sector to sonar
		int minDistance = 100;
		for (int i = 1; i <= 9; i++) {
			if (distanceToSectorMP(myPosition, i)[2] < minDistance && i != mySector) {
				sectorToSonar = i;
				minDistance = distanceToSectorMP(myPosition, i)[2];
			}
		}
		return sectorToSonar;
	}

	private static int[] fire(int[] myPosition, char[][] cellValues, int[] possibleEnemyTargets) {
		int[] enemyPosition = new int[2];
		int myShipX = myPosition[0];
		int myShipY = myPosition[1];
		boolean eastOK = true, southOK = true, westOK = true, northOK = true;

		if (possibleEnemyTargets != null) {
			// now there is no check
			System.err.println("-----------------SECTORshoot--------------");
			enemyPosition[0] = possibleEnemyTargets[0];
			enemyPosition[1] = possibleEnemyTargets[1];
		} else {
			System.err.println("-----------------RANDOMshoot--------------");

			// no info about enemy, so shoot randomly

			// possible direction check if they are ok, no island crossed
			for (int i = 1; i <= 4; i++) {
				if ((myShipX + i) < cellValues.length && cellValues[myShipY][myShipX + i] != 'x' && eastOK) {
					enemyPosition[0] = myShipX + 4;
					enemyPosition[1] = myShipY;
				} else {
					eastOK = false;
				}
			}

			if (!eastOK)
				for (int i = 1; i <= 4; i++) {
					if (westOK && (myShipX - i) > -1 && cellValues[myShipY][myShipX - i] != 'x') {
						enemyPosition[0] = myShipX - 4;
						enemyPosition[1] = myShipY;
					} else {
						westOK = false;
					}
				}

			if (!eastOK && !westOK)
				for (int i = 1; i <= 4; i++) {
					if (southOK && (myShipY + i) < cellValues.length && cellValues[myShipY + i][myShipX] != 'x') {
						enemyPosition[0] = myShipX;
						enemyPosition[1] = myShipY + 4;
					} else {
						southOK = false;
					}
				}

			if (!eastOK && !westOK && !southOK)
				for (int i = 1; i <= 4; i++) {
					if (northOK && (myShipY - i) > -1 && cellValues[myShipY - i][myShipX] != 'x') {
						enemyPosition[0] = myShipX;
						enemyPosition[1] = myShipY - 4;
					} else {
						northOK = false;
					}
				}

			if (!eastOK && !westOK && !southOK && !northOK) {
				enemyPosition[0] = 0;
				enemyPosition[1] = 0;
				System.err.println("/////////////////////////////////NOWHERETOSHOOT//////////////");
			}

		}

		return enemyPosition;

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
		int enemyX, enemyY;
		int s = 0;
		while (true) {
			turn++;
			enemyX = 0;
			enemyY = 0;

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
			int distanceToSectorMP = 100;
			int[] enemyShipData = new int[3];
			int enemySectorMiddlePointX = 0;
			int enemySectorMiddlePointY = 0;
			int mySector = 0;
			boolean sonarSuccess = false;
			boolean canMove = myShip.move(cellValues);
			if (canMove) {
				System.err.println("Charges: " + chargeToSoSi[0] + " " + chargeToSoSi[1] + " " + chargeToSoSi[2]);
				if (sonarResult.equals("Y"))
					sonarSuccess = true;
				// we have got enemy sector with sonar
				if (sonarSuccess) {
					enemySector = sectorToSonar;
					enemyShipData = distanceToSectorMP(myShip.getPosition(), enemySector);
					System.err.println("xxxxxxxxxxxxxx Sonar info from enemy in sector: " + enemySector);
					// reset SectorToSonar
					sectorToSonar = 0;
				} else
					// if we got enemy sector from surface command
					for (int i = 0; i < opponentOrdersArray.length - 1; i++) {
						if (opponentOrdersArray[i].contains("SURFACE")) {
							enemySector = Integer.valueOf(opponentOrdersArray[i + 1].substring(0, 1));
							System.err.println("xxxxxxxxxxxxxx Surface info from enemy in sector: " + enemySector);
							break;
						}
					}

				// got information about enemy with sonar command or surface command
				if (enemySector != 0) {
					enemyShipData = distanceToSectorMP(myShip.getPosition(), enemySector);
					enemySectorMiddlePointX = enemyShipData[0];
					enemySectorMiddlePointY = enemyShipData[1];
					distanceToSectorMP = enemyShipData[2];
					System.err.println("xxxxxxxxxxxxxx Enemy is in sector: " + enemySector + " , middle point is: "
							+ enemySectorMiddlePointX + " " + enemySectorMiddlePointY + " , distance is: "
							+ distanceToSectorMP);

					// shoot to the direction of the sector
					char directionToSector = directionToSector(mySector, enemySector);
					int[] myPosition = myShip.getPosition();
					int[] enemyPosition = new int[] { 0, 0 };
					int[] possibleEnemyTargets = new int[] { 0, 0 };
					switch (directionToSector) {
					case 'E':
						possibleEnemyTargets = new int[] { myPosition[0] + 4, myPosition[1] };
						break;
					case 'W':
						possibleEnemyTargets = new int[] { myPosition[0] - 4, myPosition[1] };
						break;
					case 'S':
						possibleEnemyTargets = new int[] { myPosition[0], myPosition[1] + 4 };
						break;
					case 'N':
						possibleEnemyTargets = new int[] { myPosition[0], myPosition[1] - 4 };
						break;
					default:
						break;
					}
					enemyPosition = fire(myPosition, cellValues, possibleEnemyTargets);
					enemyX = enemyPosition[0];
					enemyY = enemyPosition[1];

					/*
					 * // distance estimation with surface command if (distanceToSectorMP <= 4) {
					 * int[] possibleEnemyTargets = new int[] { enemySectorMiddlePointX,
					 * enemySectorMiddlePointY }; int[] enemyPosition = fire(myShip.getPosition(),
					 * cellValues, possibleEnemyTargets); enemyX = enemyPosition[0]; enemyY =
					 * enemyPosition[1]; }
					 */
				}
				// if no enemy position info, then fire randomly
				else {
					int[] enemyPosition = fire(myShip.getPosition(), cellValues, null);
					enemyX = enemyPosition[0];
					enemyY = enemyPosition[1];
				}

				System.err.println("Enemy poz: " + enemyX + " " + enemyY);
				System.err.println("Cellvalue at enemy poz: " + cellValues[enemyY][enemyX]);

				if (chargeToSoSi[2] == 6) {
					System.out.println("SILENCE " + myShip.getDir() + " 1");
					chargeToSoSi[2] = 0;
					// if none of above, then move and charge
				} else {
					if ((chargeToSoSi[2] < 6)) {
						chargeString = "SILENCE";
						chargeToSoSi[2]++;
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