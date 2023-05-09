package com.bassemHalim.cyclopath.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public void testSave() {
        User gosling = new User(
                "biso@email.com",
                "pwd1",
                Role.USER
        );
        userRepo.save(gosling);

    }

    public User testgetByEmail() {
        return userRepo.getUserByEmail("ragui@email.com");

    }

}
