package net.k.estore;

import net.k.estore.data.UsersRepository;
import net.k.estore.model.User;
import net.k.estore.service.EmailVerificationService;
import net.k.estore.service.EmailVerificationServiceImpl;
import net.k.estore.service.UserServiceException;
import net.k.estore.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    // need to point to implementation to be able to run the real method
    private EmailVerificationServiceImpl emailVerificationService;

    @BeforeEach
    void prepareUser() {
            firstName ="Pepe";
            lastName = "Gotera";
            email = "pepe@chapuzasadomicilio.com";
            password = "xxxxxx";
            repeatPassword = "xxxxxx";
    }

    @Test
    @DisplayName("Correct User creation")
    void testCreateUser_returnObject() {
        //Arrange
        // method with return value
        Mockito.doReturn(true)
                .when(usersRepository)
                .save(Mockito.any(User.class));
        // void method
        Mockito.doNothing()
                .when(emailVerificationService)
                .scheduleEmailConfirmation(Mockito.any(User.class));

        // Act
        User user = userService.createUser(firstName,lastName,email,password,repeatPassword);

        // Assert
        assertNotNull(user,"null object!");
        assertEquals(firstName,user.getFirstName(), "firstName not stored");
        assertEquals(lastName,user.getLastName(), "lastName not stored");
        assertEquals(email,user.getEmail(), "email not stored");
        assertNotNull(user.getId());
        Mockito.verify(usersRepository,Mockito.atMost(1))
                .save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Correct User creation")
    void testCreateUser_failedSave() {
        //Arrange
        // method with return value
        Mockito.doReturn(false)
                .when(usersRepository)
                .save(Mockito.any(User.class));
        // Act
        Throwable exception = assertThrows(UserServiceException.class,
                () -> {
                    userService.createUser(firstName,lastName, email, password, repeatPassword);
                });
        // validate
        assertEquals("failed to create user",exception.getMessage());
    }

    @Test
    @DisplayName("UserRepository Exception")
    void testCreateUser_saveException(){
        // Arrange
        Mockito.when(usersRepository.save(Mockito.any(User.class)))
                .thenThrow(UserServiceException.class);
        // Act
        Throwable exception = assertThrows(UserServiceException.class,
                () -> {
                    userService.createUser(firstName,lastName, email, password, repeatPassword);
                });
        // Assert
    }

    @Test
    @DisplayName("EmailService Exception")
    void testCreateUser_emailException(){
        // Arrange
        Mockito.doReturn(true)
                .when(usersRepository)
                .save(Mockito.any(User.class));
        Mockito.doThrow(RuntimeException.class)
               .when(emailVerificationService)
                .scheduleEmailConfirmation(Mockito.any(User.class));
        // Act
        Throwable exception = assertThrows(UserServiceException.class,
                () -> {
                    userService.createUser(firstName,lastName, email, password, repeatPassword);
                });
        // Assert
        Mockito.verify(emailVerificationService,Mockito.times(1))
                .scheduleEmailConfirmation(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Parameter-EmptyFirstName")
    void testCreateUser_exceptionFirstname() {
        // Arrange
        String emptyFristName = "";

        // Acc & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.createUser(emptyFristName, lastName, email, password, repeatPassword);
                });

        // Asert
        assertEquals("First Name required",exception.getMessage(), "Unexpected Exception");
    }

    @Test
    @DisplayName("Parameter-EmptyLastName")
    void testCreateUser_exceptionLastName() {
        // Arrange
        String emptyLastName = "";

        // Acc & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.createUser(firstName,emptyLastName, email, password, repeatPassword);
                });

        // Asert
        assertEquals("Last Name required",exception.getMessage(), "Unexpected Exception");
    }

    @Test
    @DisplayName("Parameter-IncorrectPassword")
    void testCreateUser_exceptionPassword() {
        // Arrange
        String differentPassword = "yyyyyyyy";

        // Acc & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.createUser(firstName,lastName, email, password, differentPassword);
                });

        // Asert
        assertEquals("Passwords do not match",exception.getMessage(), "Unexpected Exception");
    }

    @Test
    void testCreateUser_scheduleEmailConfirmation(){
        // Arrange
        Mockito.doReturn(true)
                .when(usersRepository)
                .save(Mockito.any(User.class));
        Mockito.doCallRealMethod()
                .when(emailVerificationService)
                .scheduleEmailConfirmation(Mockito.any(User.class));
        // Act
        User user = userService.createUser(firstName,lastName,email,password,repeatPassword);
        // Assert
        Mockito.verify(emailVerificationService,Mockito.times(1))
                .scheduleEmailConfirmation(Mockito.any(User.class));
    }
}
