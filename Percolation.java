/**
 * 
 * @author cindy li
 * @written May 15, 2015
 * 
 *          Compilation javac Percolation.java
 * 
 *          Execution java Percolation
 * 
 *          We model a percolation system using an N-by-N grid of sites. Each
 *          site is either open or blocked. The system percolates if top row
 *          connects to bottom row
 *
 */
public class Percolation {

	private int top = 0;
	private int bottom;
	private boolean OPEN = true;
	private boolean BLOCK = false;
	private int number;
	private boolean grid[][];
	private WeightedQuickUnionUF QU;

	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N <= 0) {
			throw new IllegalArgumentException("Wrong Parameter");
		}
		number = N;
		grid = new boolean[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = BLOCK;
			}
		}
		bottom = N * N + 1;
		QU = new WeightedQuickUnionUF(N * N + 2);
	}

	// Get index of N-by-N grid
	private int getGridIndex(int i, int j) {
		return (number * i + j + 1);
	}

	// open site (row i, column j) if it is not open already
	public void open(int i, int j) {
		if (!isOpen(i, j)) {
			if (i > number || i < 1 || j > number || j < 1) {
				throw new IndexOutOfBoundsException("out of range");
			}
			grid[i - 1][j - 1] = OPEN;
			if (i == 1) {
				QU.union(top, getGridIndex(i - 1, j - 1));
			}
			if (i == number)
				QU.union(getGridIndex(i - 1, j - 1), bottom);
			if (i != 1 && grid[i - 2][j - 1] == OPEN)
				QU.union(getGridIndex(i - 2, j - 1), getGridIndex(i - 1, j - 1));
			if (i != number && grid[i][j - 1] == OPEN)
				QU.union(getGridIndex(i, j - 1), getGridIndex(i - 1, j - 1));
			if (j != 1 && grid[i - 1][j - 2] == OPEN)
				QU.union(getGridIndex(i - 1, j - 2), getGridIndex(i - 1, j - 1));
			if (j != number && grid[i - 1][j] == OPEN)
				QU.union(getGridIndex(i - 1, j), getGridIndex(i - 1, j - 1));
		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		if (i > number || i < 1 || j > number || j < 1) {
			throw new IndexOutOfBoundsException("out of range");
		}
		return (grid[i - 1][j - 1] == OPEN);
	}

	// is site (row i, column j) full?
	// A full site is an open site that can be connected to an open site
	// in the top row via a chain of neighboring open sites
	public boolean isFull(int i, int j) {
		if (i >= number || i < 0 || j >= number || j < 0) {
			throw new IndexOutOfBoundsException("out of range");
		}
		return isOpen(i, j) && QU.connected(top, getGridIndex(i, j));
	}

	// is site (row i, column j) full?
	public boolean percolates() {
		return QU.connected(top, bottom);
	}

	// test client
	public static void main(String[] args) {
		Percolation Perco = new Percolation(5);
		int N = 5;
		// for (int n = 0; n < P.getNumber(); n++){
		Perco.open(1, 5);
		// }
		for (int m = 1; m <= N; m++) {
			for (int n = 1; n <= N; n++) {
				System.out.print(Perco.isOpen(m, n) + " ");
			}
			System.out.println();
		}
		System.out.println(Perco.percolates());
	}
}
