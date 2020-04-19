import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/

class Cell {
	private int x;
	private int y;
	private char value;
	private boolean walkable;

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

class Heuristic {
	private int value;
	private char dir;

	public Heuristic(int value, char dir) {
		this.value = value;
		this.dir = dir;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public char getDir() {
		return dir;
	}

	public void setDir(char dir) {
		this.dir = dir;
	}

}

class Ship {
	private int[] position = new int[2];
	private char dir;
	private int id;

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

		// if cannot move then surface needed
		if (eastH == 0 && westH == 0 && southH == 0 && northH == 0)
			return false;

		// search for the best move
		char chosenDir = 'E';
		int max = eastH;

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

		// set the values of the map
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

	// co-map
	public Cell[][] cells = new Cell[15][15];

	// actual map load to cells array
	public void setCells(char[][] cellValues) {
		for (int i = 0; i < cellValues.length; i++)
			for (int j = 0; j < cellValues.length; j++) {
				cells[i][j] = new Cell(j, i, cellValues[i][j]);
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

		// set map
		for (int y = 0; y < height; y++) {
			String line = in.nextLine();
			for (int x = 0; x < width; x++) {
				cellValues[y][x] = line.charAt(x);
			}
		}

		// find the start position
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

		// first we have to put the ship
		System.out.println(startX + " " + startY);

		Ship myShip = new Ship(new int[] { startX, startY }, ' ', myId);
		cellValues[startY][startX] = 'B';

		// chargeValue of Torpedo, Sonar, Silence, Mine
		int[] chargeToSoSiMi = new int[] { 0, 0, 0, 0 };
		String enemyDir = "";
		int turn = 0;
		boolean canMove = false;
		int[] myPosition = new int[] { 0, 0 };
		char myDir = ' ';
		String chargeString = "";
		// game loop
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
			canMove = myShip.move(cellValues);
			if (canMove) {
				myDir = myShip.getDir();
				myPosition = myShip.getPosition();

				// lets move silenced
				if (chargeToSoSiMi[2] == 6) {
					System.out.println("SILENCE " + myDir + " " + 1);
					chargeToSoSiMi[2] = 0;
				} else {
					// if none of above, then move and charge
					if ((chargeToSoSiMi[2] < 6)) {
						chargeString = "SILENCE";
						chargeToSoSiMi[2]++;
					}
					System.out.println("MOVE " + myDir + " " + chargeString);
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

		}
	}
}