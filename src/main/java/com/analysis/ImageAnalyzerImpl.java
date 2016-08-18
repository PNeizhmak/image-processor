package com.analysis;

import com.util.ColorUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FacialKeypoint;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.math.geometry.shape.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Pavel Neizhmak
 */
public class ImageAnalyzerImpl implements IImageAnalyzer {

    private Metadata metadata = new Metadata();

    private static final String IMAGE_HEIGHT = "IMAGE_HEIGHT";
    private static final String IMAGE_WIDTH = "IMAGE_WIDTH";
    private static final String IMAGE_SIZE = "IMAGE_SIZE";
    private static final String DETECTED_FACES = "DETECTED_FACES";

    @Override
    public String getDominantColorByPhoto(URL imageUrl) throws IOException {

        final MBFImage image = ImageUtilities.readMBF(imageUrl);
        final Color dominantColor = getDominantColorByFeatureVector(image);

        return ColorUtils.getColorNameFromColor(dominantColor);
    }

    private Color getDominantColorByFeatureVector(MBFImage image) {
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

    private int getFacesCount() {
        return this.metadata.getValues(DETECTED_FACES).length;
    }

    private MBFImage detectFaces(URL imageUrl) throws IOException {
        MBFImage image = ImageUtilities.readMBF(imageUrl);

        final int imageHeight = image.getHeight();
        final int imageWidth = image.getWidth();

        this.metadata.set(Property.externalInteger(IMAGE_HEIGHT), imageHeight);
        this.metadata.set(Property.externalInteger(IMAGE_WIDTH), imageWidth);
        this.metadata.set(Property.externalInteger(IMAGE_SIZE), imageWidth * imageHeight);

        FaceDetector<KEDetectedFace, FImage> fd = new FKEFaceDetector(50);
        FImage fim = Transforms.calculateIntensity(image);
        List<KEDetectedFace> faces = fd.detectFaces(fim);
        for (KEDetectedFace face : faces) {
            for (FacialKeypoint kp : face.getKeypoints()) {
                kp.position.translate(face.getBounds().getTopLeft());
                image.drawPoint(kp.position, RGBColour.GREEN, 3);
            }

            final Rectangle b = face.getBounds();
            String xywh = "xywh=" + (int) b.x + "," + (int) b.y + "," + (int) b.width + "," + (int) b.height;
            this.metadata.add(DETECTED_FACES, xywh);

            image.drawShape(b, RGBColour.RED);
        }

        for (String face : this.metadata.getValues(DETECTED_FACES)) {
            System.out.println("#" + face);
        }

        return image;
    }

    public static void main(String[] args) throws IOException {

        ImageAnalyzerImpl analyzer = new ImageAnalyzerImpl();

        String imageUrlGod = "https://pp.vk.me/c425218/v425218316/37d6/Um3AYeXk1QQ.jpg";
//        String imageUrlBad = "https://pp.vk.me/c617122/v617122316/93d4/qCQCh7OrYZY.jpg";

        MBFImage image = analyzer.detectFaces(new URL(imageUrlGod));

        DisplayUtilities.display(image);

        final int facesCount = analyzer.getFacesCount();
        System.out.println("Faces count is: " + facesCount);
    }
}
