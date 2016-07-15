package com;

import com.util.ColorUtils;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.pixel.statistics.HistogramModel;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Pavel Neizhmak
 */
public class Main {

    public static void main(String[] args) throws IOException {

        MBFImage pavelImage = ImageUtilities.readMBF(new URL("http://cs622021.vk.me/v622021595/17c62/MkfkdONZSJg.jpg"));
        MBFImage maxImage = ImageUtilities.readMBF(new URL("http://cs622617.vk.me/v622617316/1ff2f/TP3_0VWPrWo.jpg"));

        final Color pavelDColor = testDominantColor(pavelImage);
        final Color maxDColor = testDominantColor(maxImage);

        System.out.println("pavel dominant color(rgb):" + pavelDColor);
        System.out.println("max dominant color(rgb):" + maxDColor);

        ColorUtils colorUtils = new ColorUtils();
        final String pavelColorName = colorUtils.getColorNameFromColor(pavelDColor);
        final String maxColorName = colorUtils.getColorNameFromColor(maxDColor);

        System.out.println("\npavel dominant color: " + pavelColorName);
        System.out.println("max dominant color: " + maxColorName);
    }

    public static Color testDominantColor(MBFImage image) {

        int res = 64;
        HistogramModel model = new HistogramModel(res, res, res);
        model.estimateModel(image);

        double max = 0.0;
        int max_i = 0;

        double[] vec = model.getFeatureVector().asDoubleVector();
        for (int i = 0; i < vec.length; i++) {
            if (vec[i] > max) {
                max = vec[i];
                max_i = i;
            }
        }
        Color dc = new Color((int) (255 * model.colourAverage(max_i)[0]),
                (int) (255 * model.colourAverage(max_i)[1]),
                (int) (255 * model.colourAverage(max_i)[2]));

//        System.out.println("Got Color: " + dc);

//        int pixelStep = 8;
//        Map<Color, Integer> color2counter = new HashMap<Color, Integer>();
//        for (int x = 0; x < image.getWidth(); x += pixelStep) {
//            for (int y = 0; y < image.getHeight(); y += pixelStep) {
//                Float[] fc = image.getPixel(x, y);
//                Color color = new Color(fc[0], fc[1], fc[2]);
//                color = testMaxBrightness(color);
//                Integer occurrences = color2counter.get(color);
//                if (occurrences == null) occurrences = 0;
//                color2counter.put(color, occurrences + 1);
//            }
//        }
//        int fcmax = 0;
//        for (Color c : color2counter.keySet()) {
//            if (color2counter.get(c) > fcmax) {
//                fcmax = color2counter.get(c);
//            }
//        }
        return dc;
    }

    private static Color testMaxBrightness(Color c) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
        return new Color(Color.HSBtoRGB(hsv[0], hsv[1], 1.0f));
    }
}
