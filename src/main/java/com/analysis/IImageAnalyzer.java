package com.analysis;

import org.openimaj.image.MBFImage;

import java.io.IOException;
import java.net.URL;

/**
 * @author Pavel Neizhmak
 */
interface IImageAnalyzer {

    /**
     * You can use {@see File}, {@see InputStream}, {@see URL} as input param
     *
     * @param imageUrl image
     * @return dominant color name
     * @throws IOException
     */
    String getDominantColorByPhoto(URL imageUrl) throws IOException;

    /**
     * Detect faces
     *
     * @param imageUrl image
     * @return {@link MBFImage}
     * @throws IOException
     */
    MBFImage detectFaces(URL imageUrl) throws IOException;

    /**
     *
     * @return count of faces
     */
    int getFacesCount();
}
