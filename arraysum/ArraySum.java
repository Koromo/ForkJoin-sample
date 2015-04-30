import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArraySum {
    //  We need extends RecursivTask<T> if we need return value
    public static class Task extends RecursiveTask<Integer> {
        private int[] array;
        private int from, to;

        Task(int from, int to, int[] array) {
            this.array = array;
            this.from = from;
            this.to = to;
        }

        // Computing method
        protected Integer compute() {
            if (to - from < 10) {
                Integer sum = 0;
                for (int i = from; i < to; ++i)
                    sum += this.array[i];
                return sum;
            }

            int half = (this.from + this.to) / 2;

            Task task1 = new Task(this.from, half, this.array);
            task1.fork();

            Task task2 = new Task(half, this.to, array);
            task2.fork();

            return task1.join() + task2.join();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You need input 1 interger.");
            return;
        }

        int length = Integer.parseInt(args[0]);
        int[] array = new int[length];
        for (int i = 0; i < length; ++i)
            array[i] = i + 1;

        // Create ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Do computing
        int sum = pool.invoke(new Task(0, length, array));

        System.out.print("Array : ");
        for (int i: array) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();

        System.out.print("Sum : ");
        System.out.println(sum);
    }
}
