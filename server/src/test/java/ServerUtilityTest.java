import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ServerUtilityTest {

    @Test
    public void testIpv4ToInt() {
        
        String ip = "127.0.0.1";
        int expected = 2130706433;
        int result = Server.ipv4ToInt(ip);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIpv4ToIntComplex() {

        int result = Server.ipv4ToInt("192.168.1.1");
        assertTrue(result != 0);
    }
}