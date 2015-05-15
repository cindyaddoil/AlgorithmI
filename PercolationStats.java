/**
 * Monte Carlo simulation
 */
public class PercolationStats {

	private int numberGrid;
	private int numberExp;
	private double[] threshold;
	private double meanThreshold;
	private double stddevThreshold;

	public PercolationStats(int N, int T) {
		numberGrid = N;
		numberExp = T;
		threshold = new double[numberExp];
	} // perform T independent experiments on an N-by-N grid

	private double getThreshold(int N) {
		int count = 0;
		Percolation p = new Percolation(N);
		while (!p.percolates()) {
			int i = StdRandom.uniform(1, N + 1);
			int j = StdRandom.uniform(1, N + 1);
			if (!p.isOpen(i, j))
				p.open(i, j);
			else
				continue;
			count++;
		}
		double d = (double) count / (N * N);
//		System.out.println(d);
		return d;
	}

	public double mean() {
		double sum = 0;
		for (int i = 0; i < numberExp; i++) {
			double d = getThreshold(numberGrid);
			threshold[i] = d;
			sum = sum + threshold[i];
		}
		meanThreshold = sum / numberExp;
		return meanThreshold;
	} // sample mean of percolation threshold

	public double stddev() {
		double temp = 0;
		for (int i = 0; i < numberExp; i++) {
			temp = temp + (threshold[i] - meanThreshold)
					* (threshold[i] - meanThreshold);
		}
		stddevThreshold = Math.sqrt(temp / (numberExp - 1));
		return stddevThreshold;
	} // sample standard deviation of percolation threshold

	public double confidenceLo() {
		return (meanThreshold - (1.96 * stddevThreshold) / Math.sqrt(numberExp));
	} // low endpoint of 95% confidence interval

	public double confidenceHi() {
		return (meanThreshold + (1.96 * stddevThreshold) / Math.sqrt(numberExp));
	} // high endpoint of 95% confidence interval

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
	} // test client (described below)
}
