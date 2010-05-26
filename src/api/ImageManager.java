/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.mortennobel.imagescaling.ResampleOp;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author Lada Riha
 */
public class ImageManager {

    private static int height;
    private static int width;
    private static String sep = System.getProperty("file.separator");

    public static String saveImage(String path, int idPol) throws FileNotFoundException, Exception {
        // copy file
        File image = new File(path);
        FileChannel inChannel = new FileInputStream(image).getChannel();
        File dir = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep);
        boolean created = false;
        if (!dir.exists()) {
            created = dir.mkdirs();
        }

        if (dir.exists() || created) {
            boolean resize = isResizeNeccessery(image);
            File newImage;
            if (resize) {
                resizeAndSaveImage(image, idPol);

            } else {
                newImage = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg");

                
                if (newImage.exists()) {
                boolean dl = newImage.delete();
                newImage = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg");
                boolean cr = newImage.createNewFile();
                }
                
                FileChannel outChannel = new FileOutputStream(newImage).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }

            }

            return System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg";
        } else {
            throw new Exception("Unable to create directory " + System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep);
        }
    }

    private static boolean isResizeNeccessery(File newImage) throws IOException, Exception {

        ImageInputStream imageStream = ImageIO.createImageInputStream(newImage);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
        ImageReader reader = null;
        if (!readers.hasNext()) {
            throw new Exception("Cannot read image file");
        } else {
            reader = readers.next();
        }
        reader.setInput(imageStream, true, true);

        width = reader.getWidth(0);
        height = reader.getHeight(0);
        reader.dispose();
        imageStream.close();

        if (width > 55 || height > 55) {
            return true;
        }
        return false;
    }

    private static void resizeAndSaveImage(File image, int idPol) throws IOException {
        int newHeight;
        int newWidth;
        newWidth = 55;
        float change = change = (float) ((float) width / (float) newWidth);
        newHeight = (int) (height / change);


        if (height >= width) {
            newHeight = 55;
            change = (float) ((float) height / (float) newHeight);
            newWidth = (int) (width / change);
        }

        BufferedImage originalImage = ImageIO.read(image);
        ResampleOp resampleOp = new ResampleOp(newWidth, newHeight);
        BufferedImage resizedImage = resampleOp.filter(originalImage, null);

        File newImage = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg");
        
        if (newImage.exists()) {
            newImage.delete();
        }
        newImage = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg");
        ImageIO.write(resizedImage, "jpg", newImage);

    }

    public static void removeImage(int idPolozky) {
        String path = ItemsManager.getImagePath(idPolozky);
        File f = new File(path);
        f.delete();
    }
}
