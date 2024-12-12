package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void addHexagon(TETile[][] tiles, int size, int x, int y){
        for(int i = 0; i < size; i++){
//            for(int j = 0; j < size - i + 1; j++){
//                tiles[x + j][y + i] = Tileset.NOTHING;
//            }
            for(int k = 1; k <= size + i * 2; k++){
                tiles[x + size - 1 - i + k][y + i] = Tileset.FLOWER;
            }
        }

        for(int i = size - 1; i >= 0; i--){
//            for(int j = 0; j < size - i + 1; j++){
//                tiles[x + 2 * size - 1 - i][y + j] = Tileset.NOTHING;
//            }
            for(int k = 1; k <= size + i * 2 ; k++){
                tiles[x + size - 1 - i + k][y + 2 * size - 1 - i] = Tileset.FLOWER;
            }
        }
    }

    public static void fillWithNoting(TETile[][] tiles){
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        fillWithNoting(randomTiles);
        addHexagon(randomTiles, 3, 10, 10);
        ter.renderFrame(randomTiles);
    }
}
