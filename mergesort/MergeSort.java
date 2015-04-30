import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class MergeSort {
    //  We need extends RecursivTask<T> if we need return value
    public static class Task extends RecursiveTask<List<Integer>> {
        private List<Integer> list;

        Task(List<Integer> list) {
            this.list = list;
        }

        // Computing method
        protected List<Integer> compute() {
            int length = this.list.size();
            if (length <= 1)
                return this.list;

            int half = length / 2;

            Task ltask = new Task(this.list.subList(0, half));
            ltask.fork();

            Task rtask = new Task(this.list.subList(half, length));
            rtask.fork();

            return merge(ltask.join(), rtask.join());
        }

        private List<Integer> merge(List<Integer> left, List<Integer> right) {
            ListIterator<Integer> leftIt = left.listIterator();
            ListIterator<Integer> rightIt = right.listIterator();

            List<Integer> list = new ArrayList<Integer>();
            while (leftIt.hasNext() && rightIt.hasNext()) {
                Integer l = leftIt.next();
                Integer r = rightIt.next();
                if (l < r) {
                    list.add(l);
                    rightIt.previous();
                } else {
                    list.add(r);
                    leftIt.previous();
                }
            }

            while (leftIt.hasNext())
                list.add(leftIt.next());
            while (rightIt.hasNext())
                list.add(rightIt.next());

            return list;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You need input 1 interger.");
            return;
        }

        int length = Integer.parseInt(args[0]);
        List<Integer> list = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            int r = random.nextInt();
            list.add(new Integer(r % 100));
        }

        // Create ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Do computing
        List<Integer> sorted = pool.invoke(new Task(list));

        System.out.println("List : ");
        for (int i: list) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();

        System.out.println("Result : ");
        for (int i: sorted) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }
}
