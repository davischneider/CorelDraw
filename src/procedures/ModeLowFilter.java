package procedures;

import java.awt.image.BufferedImage;

public class ModeLowFilter {

    public static BufferedImage process(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        convolution(image, resultImage);
        return resultImage;
    }

    public static int computeCenter(int[][] neighbours) {
        int[] occurances = new int[256];
        for (int x = 0; x < neighbours.length; x++) {
            for (int y = 0; y < neighbours[x].length; y++) {
                int value = neighbours[x][y];
                occurances[value]++;
            }
        }
        int largest = -1;
        for (int entry = 0; entry < occurances.length; entry++) {
            if (largest == -1 || occurances[entry] > occurances[largest]) {
                largest = entry;
            }
        }
        if (occurances[largest] == 1) {
            return neighbours[1][1];
        }
        return largest;
    }

    public static void convolution(BufferedImage image, BufferedImage resultImage) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[][] redNeighbours = new int[3][3];
                int[][] greenNeighbours = new int[3][3];
                int[][] blueNeighbours = new int[3][3];

                for (int lx = 0; lx < 3; lx++) {
                    for (int ly = 0; ly < 3; ly++) {
                        int ix = Math.max(0, Math.min(image.getWidth() - 1, x + lx - 1));
                        int iy = Math.max(0, Math.min(image.getHeight() - 1, y + ly - 1));
                        int rgb = image.getRGB(ix, iy);
                        redNeighbours[lx][ly] = (rgb >> 16) & 0xFF;
                        greenNeighbours[lx][ly] = (rgb >> 8) & 0xFF;
                        blueNeighbours[lx][ly] = rgb & 0xFF;
                    }
                }

                int newRed = computeCenter(redNeighbours);
                int newGreen = computeCenter(greenNeighbours);
                int newBlue = computeCenter(blueNeighbours);

                int newRGB = (newRed << 16) | (newGreen << 8) | newBlue;
                resultImage.setRGB(x, y, newRGB);
            }
        }
    }

}
