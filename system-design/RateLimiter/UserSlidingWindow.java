import java.util.HashMap;
import java.util.Map;

public class UserSlidingWindow{
    Map<Integer, SlidingWindow> bucket;

    public UserSlidingWindow(int id){
        bucket = new HashMap<>();
        bucket.put(id, new SlidingWindow(1,10));
    }

    void accessApplication(int id){
        if(bucket.get(id).grantAccess()){
            System.out.println("Able to access");
        }
        else{
            System.out.println("Too many access");
        }
    }
}