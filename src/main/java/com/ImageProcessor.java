package com;

import com.analysis.ImageAnalyzerImpl;
import org.openimaj.image.MBFImage;

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

    public MBFImage detectFaces(final URL url) throws IOException {
        return analyzer.detectFaces(url);
    }

    public int getFacesCount() {
        return analyzer.getFacesCount();
    }
}
