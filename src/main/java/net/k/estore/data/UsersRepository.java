package net.k.estore.data;

import net.k.estore.model.User;

public interface UsersRepository {

    public boolean save(User user);

    public User load(String id);
}
