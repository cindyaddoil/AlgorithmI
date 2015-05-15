/**
 * 
 * @author cindy li
 * @written May 15, 2015
 * 
 *          Compilation javac PercolationStats.java
 * 
 *          Perform T independent experiments on an N-by-N grid 
 *          Execution java PercolationStats N T
 * 
 *          Percolation phase transition: When N is large, theory guarantees a
 *          sharp threshold p* (site vacancy probability). 
 *          p > p*: almost certainly percolates 
 *          p < p*: almost certainly does not percolates 
 *          Mathematical model has no solution for p* 
 *          Monte Carlo simulation estimates p* by running experiments millions
 *          of times in computer
 */
public class PercolationStats {

	private int numberGrid;
	private int numberExp;
	private double[] threshold;
	private double meanThreshold;
	private double stddevThreshold;

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		numberGrid = N;
		numberExp = T;
		threshold = new double[numberExp];
	}

	// Calculate vacancy percentage p when N-by-N grid system percolates
	private double getThreshold(int N) {
		int count = 0;
		Percolation Perco = new Percolation(N);
		while (!Perco.percolates()) {
			int i = StdRandom.uniform(1, N + 1);
			int j = StdRandom.uniform(1, N + 1);
			if (!Perco.isOpen(i, j))
				Perco.open(i, j);
			else
				continue;
			count++;
		}
		double p = (double) count / (N * N);
		return p;
	}

	// sample mean of percolation threshold performing by
	// T independent experiments on an N-by-N grid
	public double mean() {
		double sum = 0;
		for (int i = 0; i < numberExp; i++) {
			double d = getThreshold(numberGrid);
			threshold[i] = d;
			sum = sum + threshold[i];
		}
		meanThreshold = sum / numberExp;
		return meanThreshold;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		double temp = 0;
		for (int i = 0; i < numberExp; i++) {
			temp = temp + (threshold[i] - meanThreshold)
					* (threshold[i] - meanThreshold);
		}
		stddevThreshold = Math.sqrt(temp / (numberExp - 1));
		return stddevThreshold;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return (meanThreshold - (1.96 * stddevThreshold) / Math.sqrt(numberExp));
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return (meanThreshold + (1.96 * stddevThreshold) / Math.sqrt(numberExp));
	}

	// test client
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		Stopwatch stopwatch = new Stopwatch();
		System.out.println(N + " " + T);
		PercolationStats p = new PercolationStats(N, T);
		System.out.println("mean \t\t\t\t= " + p.mean());
		System.out.println("stddev \t\t\t\t= " + p.stddev());
		System.out.println("confidence interval \t\t= " + p.confidenceLo()
				+ ", " + p.confidenceHi());
		double time = stopwatch.elapsedTime();
		System.out.println(time);
	}
}
