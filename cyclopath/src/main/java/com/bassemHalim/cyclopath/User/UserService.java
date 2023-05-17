package com.bassemHalim.cyclopath.User;

import com.bassemHalim.cyclopath.Repositoy.SingleTableDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class UserService {
    @Autowired
    private SingleTableDB userRepo;

    public void testSave() {
        User gosling = new User(
                "biso@email.com",
                "pwd1",
                Role.USER
        );
        userRepo.saveUser(gosling);

    }

    public User testgetByEmail() {
        return userRepo.getUserByEmail("ragui@email.com");

    }

}
