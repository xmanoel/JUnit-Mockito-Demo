package net.k.estore.service;

import net.k.estore.model.User;

public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Override
    public void scheduleEmailConfirmation(User user) {
        // put information in email queue
        System.out.println("Real method executed");
    }
}
