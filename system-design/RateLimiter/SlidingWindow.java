import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindow implements RateLimiter{
     Queue<Integer> slidingWindow;
     int bucketCap;
     int time;

     public SlidingWindow(int time, int cap){
        this.time = time;
        this.bucketCap = cap;
        slidingWindow = new ConcurrentLinkedQueue<>();

     }

     @Override
     public boolean grantAccess(){
        // update the queue...only the valid requests are there
        long currentTime = System.currentTimeMillis();
        updateQueue(currentTime);
        // after updarting the queue, we need to check if the number of request exceeds the bucketCap or not
        if(slidingWindow.size() < bucketCap){
            slidingWindow.offer((int) currentTime);
            return true;
        }
        return false;
     }

     private void updateQueue(long currentTime){
        if(slidingWindow.isEmpty()) return;
        lomg time = (currentTime - slidingWindow.peek()) / 1000;

        while(time >=this.time){
            slidingWindow.poll();
            if(slidingWindow.isEmpty()) break;
            time = (currentTime - slidingWindow.peek()) / 1000;
        }
     }
}