package procedures;

import java.awt.image.BufferedImage;

public class MedianLowFilter {

    public static BufferedImage process(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        convolution(image, resultImage);
        return resultImage;
    }

    public static void convolution(BufferedImage image, BufferedImage resultImage) {
        int channelCount = image.getRaster().getNumBands();
        int width = image.getWidth();
        int height = image.getHeight();

        for (int c = 0; c < channelCount; c++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int[][] neighbours = new int[3][3];
                    for (int lx = 0; lx < 3; lx++) {
                        for (int ly = 0; ly < 3; ly++) {
                            int ix = limitX(image, x + lx - 1);
                            int iy = limitY(image, y + ly - 1);
                            neighbours[lx][ly] = image.getRaster().getSample(ix, iy, c);
                        }
                    }
                    int newValue = computeCenter(neighbours);
                    resultImage.getRaster().setSample(x, y, c, newValue);
                }
            }
        }
    }

    public static int computeCenter(int[][] neighbours) {
        int[] values = new int[9];
        int index = 0;
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                int value = neighbours[x][y];
                insert(values, value, index);
                index++;
            }
        }
        return values[values.length / 2];
    }

    public static void insert(int[] values, int newValue, int index) {
        for (int i = values.length - 1; i > index; i--) {
            values[i] = values[i - 1];
        }
        values[index] = newValue;
    }

    private static int limitX(BufferedImage image, int x) {
        return Math.max(0, Math.min(x, image.getWidth() - 1));
    }

    private static int limitY(BufferedImage image, int y) {
        return Math.max(0, Math.min(y, image.getHeight() - 1));
    }
}

