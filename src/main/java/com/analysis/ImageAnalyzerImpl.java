package com.analysis;

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
public class ImageAnalyzerImpl implements IImageAnalyzer {

    @Override
    public String getDominantColorByPhoto(URL imageUrl) throws IOException {

        final MBFImage image = ImageUtilities.readMBF(imageUrl);
        final Color dominantColor = getDominantColor(image);

        return ColorUtils.getColorNameFromColor(dominantColor);
    }

    private Color getDominantColor(MBFImage image) {

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

        System.out.println("Got Color: " + dc);

        return dc;
    }
}
