import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.Random;

public class QuickSort {
    //  We need extends RecursivAction if we don't need return value
    public static class Task extends RecursiveAction {
        private int[] array;
        private int from, to;

        Task(int from, int to, int[] array) {
            this.array = array;
            this.from = from;
            this.to = to;
        }

        // Computing method
        public void compute() {
            if (this.to - this.from <= 1)
                return;

            int p = partition();

            Task ltask = new Task(this.from, p, array);
            ltask.fork();

            Task rtask = new Task(p + 1, this.to, array);
            rtask.compute();

            //			ltask.join();
        }

        private int partition() {
            int i = this.from;
            int pivot = this.array[this.to - 1];

            for (int j = this.from; j < this.to - 1; ++j) {
                if (this.array[j] < pivot) {
                    int tmp = this.array[j];
                    this.array[j] = this.array[i];
                    this.array[i] = tmp;
                    ++i;
                }
            }

            this.array[this.to - 1] = this.array[i];
            this.array[i] = pivot;

            return i;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You need input 1 interger.");
            return;
        }

        int length = Integer.parseInt(args[0]);
        int[] array = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            int r = random.nextInt();
            array[i] = r % 100;
        }

        System.out.println("Array : ");
        for (int i: array) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();

        // Create ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Do computing
        pool.invoke(new Task(0, length, array));

        System.out.println("Result : ");
        for (int i: array) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }
}
