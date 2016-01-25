package Database;

import Entities.User;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;

/**
 * Created by rbech on 1/25/2016.
 */
public class DB {



    private HashMap<String, User> registeredUsers = new HashMap<>();



    private static DB instance = null;
    protected DB() {}
    public static DB getInstance(){
        if(instance==null)
            instance = new DB();
        return instance;
    }

    public boolean contains(String userEmail){
     return registeredUsers.containsKey(userEmail);
    }


    public HashMap<String, User> getRegisteredUsers() {
        return registeredUsers;
    }


    public void addUser(String k, User v){
        registeredUsers.put(k,v);
    }
}
