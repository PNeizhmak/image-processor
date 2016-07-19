package com;

import com.analysis.ImageAnalyzerImpl;

import java.io.IOException;
import java.net.URL;

/**
 * @author Pavel Neizhmak
 */
public class ImageProcessor {

    private ImageAnalyzerImpl analyzer = new ImageAnalyzerImpl();

    public String analyzeImage(final URL url) throws IOException {
        return analyzer.getDominantColorByPhoto(url);
    }
}
