package net.k.estore.service;

import net.k.estore.model.User;

public interface EmailVerificationService {
    void scheduleEmailConfirmation(User user);
}
