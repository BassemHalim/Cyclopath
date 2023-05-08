package com.bassemHalim.cyclopath.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public void sampleTestCase() {
        User gosling = new User(
                "ragui@email.com",
                "pwd1",
                Role.USER
        );
        userRepo.save(gosling);

    }

}
