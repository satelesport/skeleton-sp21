package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        int n=1000;
        int len=8;
        AList<Integer> Ns=new AList<>();
        AList<Double> times=new AList<>();
        AList<Integer> opCounts=new AList<>();
        for(int i=1;i<=8;i++){
            AList<Integer> test=new AList<>();
            Stopwatch sw = new Stopwatch();
            for(int t=1;t<=n;t++){
                test.addLast(1);
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.addLast(n);
            times.addLast(timeInSeconds);
            opCounts.addLast(n);
            n*=2;
        }
        printTimingTable(Ns,times,opCounts);
    }
}
