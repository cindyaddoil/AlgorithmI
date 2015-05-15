public class Percolation {

	private int top = 0;
	private int bottom;
	private boolean OPEN = true;
	private boolean BLOCK = false;
	private int number;
	private boolean grid[][];
	private WeightedQuickUnionUF QU;

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
	} // create N-by-N grid, with all sites blocked

	private int getGridIndex(int i, int j) {
		return (number * i + j + 1);
	} // Get index of N-by-N grid

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
	} // open site (row i, column j) if it is not open already

	public boolean isOpen(int i, int j) {
		if (i > number || i < 1 || j > number || j < 1) {
			throw new IndexOutOfBoundsException("out of range");
		}
		return (grid[i - 1][j - 1] == OPEN);
	} // is site (row i, column j) open?

	public boolean isFull(int i, int j) {
		if (i >= number || i < 0 || j >= number || j < 0) {
			throw new IndexOutOfBoundsException("out of range");
		}
		return isOpen(i, j) && QU.connected(top, getGridIndex(i, j));
	} // is site (row i, column j) full?

	public boolean percolates() {
		return QU.connected(top, bottom);
	} // does the system percolate?

	public int getNumber() {
		return number;
	}

	public static void main(String[] args) {
		Percolation P = new Percolation(5);
		int N = 5;
		// for (int n = 0; n < P.getNumber(); n++){
		P.open(1, 5);
		// }
		for (int m = 1; m <= N; m++) {
			for (int n = 1; n <= N; n++) {
				System.out.print(P.isOpen(m, n) + " ");
			}
			System.out.println();
		}
		System.out.println(P.percolates());
	} // test client (optional)
}
