import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MovingPlatformTest {

    @Test
    public void testPlatformMovementLogic() {
        Vec2 startPos = new Vec2(500, 500);
        Vec2 size = new Vec2(50, 10);
        MovingPlatform platform = new MovingPlatform(startPos, size);

        platform.movePlatform(); 
        assertTrue(platform.coordinates.x > 500, "Should move right initially");

        platform.coordinates.x = 600; 
        platform.movePlatform(); 
        
        platform.movePlatform(); 
        assertTrue(platform.coordinates.x < 600, "Should move left after hitting boundary");
    }

    @Test
    public void testUpdatePlatformsList() {
        List<MovingPlatform> platforms = new ArrayList<>();
        MovingPlatform p1 = new MovingPlatform(new Vec2(100, 100), new Vec2(50, 10));
        MovingPlatform p2 = new MovingPlatform(new Vec2(200, 200), new Vec2(50, 10));
        platforms.add(p1);
        platforms.add(p2);

        assertEquals(100, p1.coordinates.x);
        assertEquals(200, p2.coordinates.x);

        MovingPlatform.updatePlatforms(platforms);

        assertEquals(101, p1.coordinates.x, 0.01f);
        assertEquals(201, p2.coordinates.x, 0.01f);
    }
    
    @Test
    public void testStaticFlagConsistency() {
        
        MovingPlatform p = new MovingPlatform(new Vec2(0,0), new Vec2(10,10));
        
        MovingPlatform.moveLeft = true; 
        p.movePlatform(); 
        
        assertFalse(MovingPlatform.moveLeft, "Instance movement should update the static flag");
    }
}