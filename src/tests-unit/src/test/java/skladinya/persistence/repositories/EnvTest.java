package skladinya.persistence.repositories;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class EnvTest {

    @Test
    void testEnvVariables() {
        String db = System.getenv("POSTGRES_DB");
        String user = System.getenv("POSTGRES_USER");
        String pass = System.getenv("POSTGRES_PASSWORD");

        assertNotNull(db);
        assertNotNull(user);
        assertNotNull(pass);
    }
}
