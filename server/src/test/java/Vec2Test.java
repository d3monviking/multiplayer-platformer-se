import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vec2Test {
    
    @Test
    public void testConstructorAndGetters() {
        Vec2 v = new Vec2(12.5f, -5.0f);
        assertEquals(12.5f, v.getX());
        assertEquals(-5.0f, v.getY());
    }

    @Test
    public void testPublicFieldAccess() {
        // Since fields are public, ensure setting them works
        Vec2 v = new Vec2(0, 0);
        v.x = 100f;
        v.y = 200f;
        
        assertEquals(100f, v.getX());
        assertEquals(200f, v.getY());
    }
}