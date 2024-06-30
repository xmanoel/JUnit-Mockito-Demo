package net.k.estore.service;

import net.k.estore.data.UsersRepository;
import net.k.estore.model.User;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    EmailVerificationService emailVerificationService;
    UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository, EmailVerificationService emailVerificationService) {
        this.usersRepository = usersRepository;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public User createUser(String firstName, String lastName, String email, String password, String repeatPassword) {

        
            if(firstName == null || firstName.trim().isEmpty())
                throw new IllegalArgumentException("First Name required");
            if(lastName == null || lastName.trim().isEmpty())
                throw new IllegalArgumentException("Last Name required");
            if(email == null || email.trim().isEmpty())
                throw new IllegalArgumentException("Email required");
            if(password == null || password.trim().isEmpty())
                throw new IllegalArgumentException("Password required");
            if(!password.equals(repeatPassword))
                throw new IllegalArgumentException("Passwords do not match");
        String id = UUID.randomUUID().toString();

        User user = new User(id, firstName, lastName, email, password, repeatPassword);


        boolean isUserCreation;
        try {
            isUserCreation = this.usersRepository.save(user);
        } catch (RuntimeException re){
            throw new UserServiceException(re.getMessage());
        }

        if (!isUserCreation)
            throw new UserServiceException("failed to create user");

        try {
            emailVerificationService.scheduleEmailConfirmation(user);
        } catch (RuntimeException re){
            throw new UserServiceException(re.getMessage());
        }


        return user;
    }

    @Override
    public User deleteUser(User user) {
        //unimplemented
        return null;
    }
}
