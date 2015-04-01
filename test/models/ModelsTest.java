package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }

    @Test
    public void tryAuthenticateUser() {
        new User("Bob", "secret").save();

        assertNotNull(User.authenticate("Bob", "secret"));
        assertNull(User.authenticate("Bob1", "badpassword"));
        assertNull(User.authenticate("Bob2", "secret"));
    }

    @Test
    public void tryAddNewPost() {
        new Post("Title", "abcdef", "02/01/2015").save();

    }

}