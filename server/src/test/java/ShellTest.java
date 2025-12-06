import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class ShellTest {
    
    @Test
    public void testShellInitialization() {
        Vec2 pos = new Vec2(10, 10);
        Vec2 size = new Vec2(16, 16);
        Shell shell = new Shell(pos, size);
        
        assertEquals('S', shell.getType(), "Shell type should be 'S'");
        assertFalse(shell.isHeld(), "Shell should not be held initially");
        assertFalse(shell.isKicked(), "Shell should not be kicked initially");
    }

    @Test
    public void testInheritance() {
        // Verify it retains Tile properties
        Vec2 pos = new Vec2(50, 50);
        Vec2 size = new Vec2(32, 32);
        Shell shell = new Shell(pos, size);

        assertEquals(50, shell.getCoordinates().x);
        assertEquals(32, shell.getSize().y);
    }
}