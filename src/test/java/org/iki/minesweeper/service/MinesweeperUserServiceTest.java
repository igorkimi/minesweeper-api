package org.iki.minesweeper.service;

import org.iki.minesweeper.config.MinesweeperApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MinesweeperUserServiceTest {

    @Autowired
    private MinesweeperUserService minesweeperUserService;

    @Test
    @DisplayName("When accessing the system, user must be able to create and account")
    public void minesweeperUserServiceCreate(){

        Assertions.assertDoesNotThrow(
            () -> minesweeperUserService.createUser("usernameone", "password")
        );
    }

    @Test
    @DisplayName("When creating an account, username cannot be duplicated")
    public void minesweeperUserServiceCreateExistingUser(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> {
                minesweeperUserService.createUser("username", "password");
                minesweeperUserService.createUser("username", "password");
            }
        );
        Assertions.assertEquals("User already exists", ex.getMessage());
    }

    @Test
    @DisplayName("When creating an account, user should provide username and password")
    public void minesweeperUserServiceCreateEmptyParams(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> {
                minesweeperUserService.createUser(null, null);
            }
        );
        Assertions.assertEquals("Username and Password must be set", ex.getMessage());
    }

    @Test
    @DisplayName("When creating an account, user should provide username and password with at least 3 characters")
    public void minesweeperUserServiceCreateInvalidParams(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> {
                minesweeperUserService.createUser("12", "12");
            }
        );
        Assertions.assertEquals("Username and Password must be at least 3 characters long", ex.getMessage());
    }

    @Test
    @DisplayName("When creating an account, user should provide username with no number or special characters")
    public void minesweeperUserServiceCreateInvalidUser(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> {
                minesweeperUserService.createUser("username132%$@1", "password");
            }
        );
        Assertions.assertEquals("Username should not contain numbers or special characters", ex.getMessage());
    }


    @Test
    @DisplayName("When authenticating, user should provide username and password")
    public void minesweeperUserServiceAuthenticateEmptyParams(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> {
                minesweeperUserService.authenticate(null, null);
            }
        );
        Assertions.assertEquals("Username and Password must be set", ex.getMessage());
    }

    @Test
    @DisplayName("When authenticating, user should provide correct username and password")
    public void minesweeperUserServiceAuthenticateWrongPassword(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> {
                minesweeperUserService.createUser("usernametwo", "password");
                minesweeperUserService.authenticate("usernametwo", "wrongpassword");
            }
        );
        Assertions.assertEquals("Authentication Failed", ex.getMessage());
    }

    @Test
    @DisplayName("When authenticating, user should receive no erros when using correct credentials")
    public void minesweeperUserServiceAuthenticateCorrenctPassword(){

        Assertions.assertDoesNotThrow(
            () -> {
                minesweeperUserService.createUser("usernamethree", "password");
                minesweeperUserService.authenticate("usernamethree", "password");
            }
        );
    }
}
