package procedures;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Kirsch {
    public static BufferedImage process(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        convolution(image, resultImage);
        return resultImage;
    }

    private static final int[][][] masks = {
            {
                    { 5, 5, 5 },
                    { -3, 0, -3 },
                    { -3, -3, -3 }
            },
            {
                    { -3, 5, 5 },
                    { -3, 0, 5 },
                    { -3, -3, -3 }
            },
            {
                    { -3, -3, 5 },
                    { -3, 0, 5 },
                    { -3, -3, 5 }
            },
            {
                    { -3, -3, -3 },
                    { -3, 0, 5 },
                    { -3, 5, 5 }
            },
            {
                    { -3, -3, -3 },
                    { -3, 0, -3 },
                    { 5, 5, 5 }
            },
            {
                    { -3, -3, -3 },
                    { 5, 0, -3 },
                    { 5, 5, -3 }
            },
            {
                    { 5, -3, -3 },
                    { 5, 0, -3 },
                    { 5, -3, -3 }
            },
            {
                    { 5, 5, -3 },
                    { 5, 0, -3 },
                    { -3, -3, -3 }
            }
    };

    private static int computePixel(BufferedImage image, int channel, int x, int y) {
        int[] gradients = new int[masks.length];
        for (int lx = 0; lx < 3; lx++) {
            for (int ly = 0; ly < 3; ly++) {
                int value = new Color(image.getRGB(x + lx - 1, y + ly - 1)).getRed();
                for (int i = 0; i < gradients.length; i++) {
                    if (gradients[i] == 0) {
                        gradients[i] = 0;
                    }
                    gradients[i] += value * masks[i][lx][ly];
                }
            }
        }
        return processGradients(gradients) * 256 / 3840;
    }

    private static int[] getMaskWeights() {
        return new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
    }

    private static int processGradients(int[] gradients) {
        int[] weights = getMaskWeights();
        int gradient = 0;
        for (int i = 0; i < gradients.length; i++) {
            gradient = Math.max(gradients[i] * weights[i], gradient);
        }
        return (int) Math.floor(gradient);
    }

    private static void convolution(BufferedImage image, BufferedImage resultImage) {
        for (int channel = 0; channel < image.getRaster().getNumBands(); channel++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {
                for (int y = 1; y < image.getHeight() - 1; y++) {
                    int newValue = computePixel(image, channel, x, y);
                    resultImage.setRGB(x, y, new Color(newValue, newValue, newValue).getRGB());
                }
            }
        }
    }
}
