
package procedures;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Dilation {
    public static BufferedImage process(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        dilation(image, resultImage);
        return resultImage;
    }

    private static int computeCenter(int[][] neighbours) {
        int largest = 0;
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                if (neighbours[x][y] > largest) {
                    largest = neighbours[x][y];
                }
            }
        }
        return largest;
    }

    private static void dilation(BufferedImage image, BufferedImage resultImage) {
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                int[][] redNeighbors = new int[3][3];
                int[][] greenNeighbors = new int[3][3];
                int[][] blueNeighbors = new int[3][3];
                for (int lx = 0; lx < 3; lx++) {
                    for (int ly = 0; ly < 3; ly++) {
                        int ix = limitX(image, x + lx - 1);
                        int iy = limitY(image, y + ly - 1);
                        Color neighborColor = new Color(image.getRGB(ix, iy));
                        redNeighbors[lx][ly] = neighborColor.getRed();
                        greenNeighbors[lx][ly] = neighborColor.getGreen();
                        blueNeighbors[lx][ly] = neighborColor.getBlue();
                    }
                }
                int newRedValue = computeCenter(redNeighbors);
                int newGreenValue = computeCenter(greenNeighbors);
                int newBlueValue = computeCenter(blueNeighbors);
                Color newColor = new Color(newRedValue, newGreenValue, newBlueValue);
                resultImage.setRGB(x, y, newColor.getRGB());
            }
        }
    }

    private static int limitX(BufferedImage image, int x) {
        return Math.min(image.getWidth() - 1, Math.max(0, x));
    }

    private static int limitY(BufferedImage image, int y) {
        return Math.min(image.getHeight() - 1, Math.max(0, y));
    }
}
