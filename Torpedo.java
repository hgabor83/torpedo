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

	public boolean isDeadlock(char[][] cellValues, int wantedX, int wantedY) {
		if (((wantedX + 1) < cellValues.length) && (cellValues[wantedY][wantedX + 1] == '.')) {
			System.err.println("No deadlock east");
			return false;
		} else if (((wantedY + 1) < cellValues.length) && (cellValues[wantedY + 1][wantedX] == '.')) {
			System.err.println("No deadlock south");
			return false;
		} else if (((wantedX - 1) > -1) && (cellValues[wantedY][wantedX - 1] == '.')) {
			System.err.println("No deadlock west");
			return false;
		} else if (((wantedY - 1) > -1) && (cellValues[wantedY - 1][wantedX] == '.')) {
			System.err.println("No deadlock north");
			return false;
		} else {
			System.err.println("DEADLOCK");
			return true;
		}
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

	public int silentMove(char[][] cellValues) {
		int[] startPosition = this.getPosition();
		System.err.println("Actual poz before silent move: " + startPosition[0] + " " + startPosition[1]);
		int x = startPosition[0];
		int y = startPosition[1];
		List<Character> possibleDirs = new ArrayList<Character>();
		boolean eastOK, southOK, westOK, northOK;
		int moveCount = 5;
		do {
			// start with maximum 4
			moveCount--;
			eastOK = true;
			southOK = true;
			westOK = true;
			northOK = true;
			System.err.println("Investigating " + moveCount + " silent move possibility");
			// possible direction check if they are ok, no island no Backwards
			for (int i = 1; i <= moveCount; i++) {
				System.err.println("IN FOR, i: " + i + " movecount: " + moveCount);
				if (((x + i) >= cellValues.length) || isDeadlock(cellValues, x + i, y) || (cellValues[y][x + i] != '.')
						|| !eastOK) {
					System.err.println("EAST NOK");
					eastOK = false;
				}

				if (((y + i) >= cellValues.length) || isDeadlock(cellValues, x, y + i) || (cellValues[y + i][x] != '.')
						|| !southOK) {
					System.err.println("SOUTH NOK");
					southOK = false;
				}

				if (((x - i) <= -1) || isDeadlock(cellValues, x - i, y) || (cellValues[y][x - i] != '.') || !westOK) {
					System.err.println("WEST NOK");
					westOK = false;
				}

				if (((y - i) <= -1) || isDeadlock(cellValues, x, y - i) || (cellValues[y - i][x] != '.') || !northOK) {
					System.err.println("NORTH NOK");
					northOK = false;
				}
			}
			if (eastOK)
				possibleDirs.add('E');
			if (southOK)
				possibleDirs.add('S');
			if (westOK)
				possibleDirs.add('W');
			if (northOK)
				possibleDirs.add('N');
			System.err.println("At moveCount " + moveCount + " the possible dirs are " + possibleDirs.toString());
		} while (possibleDirs.size() == 0 && moveCount > 1);

		System.err.print("Possible dirs: ");
		for (Character character : possibleDirs) {
			System.err.print(character);
		}
		System.err.println("");

		if (possibleDirs.size() == 0)
			return 0;

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

		for (int i = 1; i <= moveCount; i++) {
			switch (chosenDir) {
			case 'E':
				this.setPosition(new int[] { x + i, y });
				cellValues[y][x + i] = 'B';
				break;

			case 'S':
				this.setPosition(new int[] { x, y + i });
				cellValues[y + i][x] = 'B';
				break;

			case 'W':
				this.setPosition(new int[] { x - i, y });
				cellValues[y][x - i] = 'B';
				break;

			case 'N':
				this.setPosition(new int[] { x, y - i });
				cellValues[y - i][x] = 'B';
				break;

			default:
				break;
			}
		}
		return moveCount;

	}

}

class Player {

	private static int distanceShips(int[] myPosition, int[] enemyPosition) {
		return Math.max(Math.abs(myPosition[0] - enemyPosition[0]), Math.abs(myPosition[1] - enemyPosition[1]));
	}

	private static int getSectorToSonar(int myS, char myD) {
		int mySector = myS;
		char myDir = myD;
		int sectorToSonar = 1;

		if (mySector == 1 && myDir == 'E')
			sectorToSonar = 2;
		if (mySector == 1 && myDir == 'S')
			sectorToSonar = 4;
		if (mySector == 2 && myDir == 'E')
			sectorToSonar = 3;
		if (mySector == 2 && myDir == 'S')
			sectorToSonar = 5;
		if (mySector == 2 && myDir == 'W')
			sectorToSonar = 1;
		if (mySector == 3 && myDir == 'W')
			sectorToSonar = 2;
		if (mySector == 3 && myDir == 'S')
			sectorToSonar = 6;
		if (mySector == 4 && myDir == 'E')
			sectorToSonar = 5;
		if (mySector == 4 && myDir == 'S')
			sectorToSonar = 7;
		if (mySector == 4 && myDir == 'N')
			sectorToSonar = 1;
		if (mySector == 5 && myDir == 'E')
			sectorToSonar = 6;
		if (mySector == 5 && myDir == 'S')
			sectorToSonar = 8;
		if (mySector == 5 && myDir == 'W')
			sectorToSonar = 4;
		if (mySector == 5 && myDir == 'N')
			sectorToSonar = 2;
		if (mySector == 6 && myDir == 'S')
			sectorToSonar = 9;
		if (mySector == 6 && myDir == 'W')
			sectorToSonar = 5;
		if (mySector == 6 && myDir == 'N')
			sectorToSonar = 3;
		if (mySector == 7 && myDir == 'E')
			sectorToSonar = 8;
		if (mySector == 7 && myDir == 'N')
			sectorToSonar = 4;
		if (mySector == 8 && myDir == 'E')
			sectorToSonar = 9;
		if (mySector == 8 && myDir == 'W')
			sectorToSonar = 7;
		if (mySector == 8 && myDir == 'N')
			sectorToSonar = 5;
		if (mySector == 9 && myDir == 'W')
			sectorToSonar = 8;
		if (mySector == 9 && myDir == 'N')
			sectorToSonar = 6;
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

			// in case of turning, we should fire the right direction
			char myDir = myShip.getDir();
			// calculate the torpedo goal cells
			int torpedoX = 0, torpedoY = 0;
			int[] myPos = myShip.getPosition();
			switch (myDir) {
			case 'E':
				torpedoX = myPos[0] + 4;
				torpedoY = myPos[1];
				break;
			case 'S':
				torpedoX = myPos[0];
				torpedoY = myPos[1] + 4;
				break;
			case 'W':
				torpedoX = myPos[0] - 4;
				torpedoY = myPos[1];
				break;
			case 'N':
				torpedoX = myPos[0];
				torpedoY = myPos[1] - 4;
				break;

			default:
				break;
			}

			// if ((chargeToSoSi[0] == 3) && sonarResult.equals("Y") && torpedoX > -1 &&
			// torpedoX < 15 && torpedoY > -1
			// && torpedoY < 15) {
			// System.out.println("TORPEDO " + torpedoX + " " + torpedoY);
			// } else
			if (chargeToSoSi[2] == 6) {
				int moveCount = myShip.silentMove(cellValues);
				if (moveCount != 0) {
					myDir = myShip.getDir();
					System.out.println("SILENCE " + myDir + " " + moveCount);
					chargeToSoSi[2] = -1;
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
			} else if (myShip.move(cellValues)) {

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
				// myActualDir for moving
				myDir = myShip.getDir();

				// if (chargeToSoSi[1] == 4) {
				// let's sonar our directions next sector
				// int mySector = myShip.getSector();
				// int sectorToSonar = getSectorToSonar(mySector, myDir);
				// System.out.println("SONAR " + sectorToSonar + " | MOVE " + myDir + " " +
				// chargeString);
				// chargeToSoSi[1] = -1;
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