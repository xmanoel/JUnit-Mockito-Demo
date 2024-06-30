package net.k.estore.data;

import net.k.estore.model.User;

import java.util.HashMap;
import java.util.Map;

public class UsersRepositoryImpl implements UsersRepository{
    Map<String,User> users = new HashMap<>();
    @Override
    public boolean save(User user) {
        if( users.containsKey(user.getId()) )
            return false;
        users.put(user.getId(),user);
        return true;
    }

    @Override
    public User load(String id) {
        return null;
    }
}
