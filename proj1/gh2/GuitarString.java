package gh2;

import deque.*;

public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    // TODO: uncomment the following line once you're ready to start this portion
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int newSize = (int) Math.round(SR / frequency);
        buffer = new LinkedListDeque<Double>();
        for(int i = 0; i < newSize; i++) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int size = buffer.size();
        for(int i = 0; i < size; i++) {
            buffer.removeLast();
        }
        for(int i = 0; i < size; i++) {
            double r = Math.random() - 0.5;
            buffer.addLast(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double f1 = buffer.removeFirst();
        double f2 = buffer.get(0);
        buffer.addLast((f1 + f2) / 2 * DECAY);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        int size = buffer.size();
        return buffer.get(size - 1);
    }
}
