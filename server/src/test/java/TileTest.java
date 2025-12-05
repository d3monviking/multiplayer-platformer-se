import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    @Test
    public void testTileInitialization() {
        Vec2 pos = new Vec2(10, 20);
        Vec2 size = new Vec2(32, 32);
        Tile tile = new Tile(pos, size);

        assertEquals(10, tile.getCoordinates().x);
        assertEquals(20, tile.getCoordinates().y);
        assertEquals(32, tile.getSize().x);
        assertEquals(0, tile.getVel().x, "Velocity should initialize to 0");
    }

    @Test
    public void testCoordinateReference() {
        // Ensure that modifying the returned coordinate vector affects the tile
        Vec2 pos = new Vec2(0, 0);
        Vec2 size = new Vec2(10, 10);
        Tile tile = new Tile(pos, size);

        tile.getCoordinates().x = 50;
        assertEquals(50, tile.coordinates.x);
    }
}