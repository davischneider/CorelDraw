package procedures;

import java.awt.image.BufferedImage;

public class Contrast {

    public static BufferedImage process(BufferedImage image, double contrast) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int c = 0; c < image.getRaster().getNumBands(); c++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int pixelValue = image.getRaster().getSample(x, y, c);
                    int newPixel = (int) (pixelValue * contrast);
                    newPixel = Math.max(0, Math.min(255, newPixel)); // Limit the value between 0 and 255
                    resultImage.getRaster().setSample(x, y, c, newPixel);
                }
            }
        }
        return resultImage;
    }
}
