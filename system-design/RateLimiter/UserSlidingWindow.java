import java.util.HashMap;
import java.util.Map;

public class UserSlidingWindow{
    Map<Integer, SlidingWindow> bucket;

    public UserSlidingWindow(int id){
        bucket = new HashMap();
        bucket.put(id, new SlidingWindow(time:1, cap:10);)
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