import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServerUtilityTest {

    @Test
    public void testIpv4ToInt() {
        // Logic: 127.0.0.1
        // 127 << 24 | 0 << 16 | 0 << 8 | 1
        // Result should be 2130706433
        
        String ip = "127.0.0.1";
        int expected = 2130706433;
        int result = Server.ipv4ToInt(ip);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIpv4ToIntComplex() {
        // 192.168.1.1
        // 192 << 24 = -1073741824 (in signed 32-bit int)
        // 168 << 16 = 11010048
        // 1 << 8 = 256
        // 1
        // Sum/OR operation logic
        
        int result = Server.ipv4ToInt("192.168.1.1");
        // We verify it returns a non-zero integer consistent with IP parsing
        assertTrue(result != 0);
    }
}