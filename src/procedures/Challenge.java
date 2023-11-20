package procedures;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class Challenge {

    public static List<Integer> process(BufferedImage image) {
        List<Integer> counts = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();

        for (int i = 0; i < width; i++) {
            int count = 0;
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                if (isBlack(rgb)) {
                    count++;
                }
            }
            counts.add(count);
        }

        return counts;
    }

    private static boolean isBlack(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return red < 126 && green < 126 && blue < 126;
    }

}
