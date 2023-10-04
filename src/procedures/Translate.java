package procedures;

import java.awt.image.BufferedImage;

public class Translate {

    public static BufferedImage process(BufferedImage image, int x, int y) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        transform(image, resultImage, x, y);
        return resultImage;
    }

    public static void transform(BufferedImage image, BufferedImage resultImage, int x, int y) {
        int channelCount = image.getRaster().getNumBands();
        int width = image.getWidth();
        int height = image.getHeight();

        double[][] kernel = {
                {1, 0, -x},
                {0, 1, -y},
                {0, 0, 1}
        };

        for (int c = 0; c < channelCount; c++) {
            for (int xCoord = 0; xCoord < width; xCoord++) {
                for (int yCoord = 0; yCoord < height; yCoord++) {
                    applyKernel(image, resultImage, kernel, c, xCoord, yCoord);
                }
            }
        }
    }

    public static void applyKernel(BufferedImage image, BufferedImage resultImage, double[][] kernel, int channel, int x, int y) {
        int halfX = image.getWidth() / 2;
        int halfY = image.getHeight() / 2;
        int tmpX = x - halfX;
        int tmpY = y - halfY;
        int newX = (int) Math.round(tmpX * kernel[0][0] + tmpY * kernel[0][1] + 1 * kernel[0][2]);
        int newY = (int) Math.round(tmpX * kernel[1][0] + tmpY * kernel[1][1] + 1 * kernel[1][2]);
        newX += halfX;
        newY += halfY;

        if (newX < image.getWidth() && newY < image.getHeight() && newX >= 0 && newY >= 0) {
            int pixelValue = image.getRaster().getSample(newX, newY, channel);
            resultImage.getRaster().setSample(x, y, channel, pixelValue);
        }
    }
}

