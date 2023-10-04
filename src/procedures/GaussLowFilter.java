package procedures;

import java.awt.image.BufferedImage;

public class GaussLowFilter {

    private static final double[][] kernel = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };

    public static BufferedImage process(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        convolution(image, resultImage);
        return resultImage;
    }

    public static void convolution(BufferedImage image, BufferedImage resultImage) {
        int channelCount = image.getRaster().getNumBands();
        int width = image.getWidth();
        int height = image.getHeight();

        for (int channel = 0; channel < channelCount; channel++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    processKernel(image, resultImage, channel, x, y);
                }
            }
        }
    }

    public static void processKernel(BufferedImage image, BufferedImage resultImage, int channel, int x, int y) {
        double sumValue = 0;
        double valueKernel = 0;
        int ksize = kernel.length;

        for (int i = 0; i < ksize; i++) {
            for (int j = 0; j < ksize; j++) {
                int xPos = limitX(image, x + (i - 1));
                int yPos = limitY(image, y + (j - 1));
                double pixelValue = image.getRaster().getSample(xPos, yPos, channel);
                sumValue += pixelValue * kernel[i][j];
                valueKernel += kernel[i][j];
            }
        }

        if (valueKernel > 0) {
            sumValue /= valueKernel;
        }

        int value = (int) Math.floor(Math.round(sumValue));
        value = limitPixelValue(image, value);
        resultImage.getRaster().setSample(x, y, channel, value);
    }

    // Métodos limitX, limitY e limitPixelValue devem ser definidos corretamente
    private static int limitX(BufferedImage image, int x) {
        return Math.max(0, Math.min(x, image.getWidth() - 1));
    }

    private static int limitY(BufferedImage image, int y) {
        return Math.max(0, Math.min(y, image.getHeight() - 1));
    }

    private static int limitPixelValue(BufferedImage image, int value) {
        return Math.max(0, Math.min(value, image.getRaster().getSample(0, 0, 0)));
    }
}

