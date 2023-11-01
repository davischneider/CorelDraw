package procedures;

import java.awt.image.BufferedImage;

public class Threshold {
    public static BufferedImage process(BufferedImage image) {
        BufferedImage grayscaleImage = grayscale(image);
        BufferedImage resultImage = new BufferedImage(grayscaleImage.getWidth(), grayscaleImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        int threshold = 100;

        for (int x = 0; x < grayscaleImage.getWidth(); x++) {
            for (int y = 0; y < grayscaleImage.getHeight(); y++) {
                int grayValue = grayscaleImage.getRGB(x, y) & 0xFF;
                int newPixel = grayValue >= threshold ? 0xFFFFFF : 0x000000;
                resultImage.setRGB(x, y, newPixel);
            }
        }

        return resultImage;
    }

    public static BufferedImage grayscale(BufferedImage image) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int sum = 0;
                for (int c = 0; c < image.getColorModel().getNumColorComponents(); c++) {
                    sum += image.getRaster().getSample(x, y, c);
                }
                int newPixel = sum / image.getColorModel().getNumColorComponents();
                resultImage.setRGB(x, y, newPixel << 16 | newPixel << 8 | newPixel);
            }
        }

        return resultImage;
    }
}


