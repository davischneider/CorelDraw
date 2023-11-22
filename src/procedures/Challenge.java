package procedures;

import utils.Constants;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    public static String identify(List<Integer> histogram) {
        String value = "Não reconhecido";
        for (Map.Entry<Integer, int[]> entry : Constants.values.entrySet()) {
            int key = entry.getKey();
            int[] arrayToCompare = entry.getValue();

            // Comparar o array com a lista
            if (compareArrayWithList(arrayToCompare, histogram)) {
                value = String.valueOf(key);
            }
        }
        return value;
    }

    private static boolean compareArrayWithList(int[] array, List<Integer> lista) {
        if (array.length != lista.size()) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] != lista.get(i)) {
                return false;
            }
        }

        return true;
    }

    public static List<Integer> removeZerosFromExtremes(List<Integer> histogram) {
        int start = 0;
        int end = histogram.size() - 1;

        while (start < histogram.size() && histogram.get(start) == 0) {
            start++;
        }

        while (end >= 0 && histogram.get(end) == 0) {
            end--;
        }

        if (start > end) {
            return new ArrayList<>();
        }

        return new ArrayList<>(histogram.subList(start, end + 1));
    }

}
