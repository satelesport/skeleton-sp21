package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns=new AList<>();
        AList<Double> times=new AList<>();
        AList<Integer> opCounts=new AList<>();
        int n=1000;
        for(int i=1;i<=8;i++){
            SLList<Integer> test=new SLList<>();
            for(int t=1;t<=n;t++){
                test.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for(int t=1;t<=10000;t++){
                test.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.addLast(n);
            times.addLast(timeInSeconds);
            opCounts.addLast(10000);
            n*=2;
        }
        printTimingTable(Ns,times,opCounts);
    }

}
