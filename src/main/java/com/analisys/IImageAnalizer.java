package com.analisys;

import java.io.IOException;
import java.net.URL;

/**
 * @author Pavel Neizhmak
 */
interface IImageAnalizer {

    /**
     * You can use {@see File}, {@see InputStream}, {@see URL} as input param
     *
     * @param imageUrl image
     * @return dominant color name
     */
    String getDominantColorByPhoto(URL imageUrl) throws IOException;
}
