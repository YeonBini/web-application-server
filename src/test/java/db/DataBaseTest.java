package db;

import model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataBaseTest {



    @Test
    public void addUser() {
        // Given
        User user = new User("YeonBin", "1234", "YeonBin", "test@test.com");

        // When
        DataBase.addUser(user);

        // Then

    }

    @Test
    public void findUserById() {
        // Given
        User user = new User("YeonBin", "1234", "YeonBin", "test@test.com");

        // When
        DataBase.addUser(user);

        // Then
        assertEquals(DataBase.findUserById("YeonBin"), user);

    }

    @Test
    public void findAll() {
        // Given
        User user = new User("YeonBin", "1234", "YeonBin", "test@test.com");
        User user2 = new User("YeonBin2", "1234", "YeonBin2", "test@test.com");

        // When
        DataBase.addUser(user);
        DataBase.addUser(user2);

        // Then
        assertEquals(DataBase.findAll().size(), 2);

    }
}