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
import java.net.URL;
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

    public static String saveImage(String path, int idPol, String usedPath) throws FileNotFoundException, Exception {
        // copy file
        File image = new File(path);
        String p = "";
        String toDelete = "";
        if (usedPath.length() > 0) {
            if (usedPath.endsWith("_.jpg")) {
                p = System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg";
                toDelete = System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + "_.jpg";

            } else {
                toDelete = System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg";
                p = System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + "_.jpg";
            }


        } else {
            p = System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg";

        }

        FileChannel inChannel = new FileInputStream(image).getChannel();
        File dir = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep);
        boolean created = false;
        if (!dir.exists()) {
            created = dir.mkdirs();
        }

        if (dir.exists() || created) {
            if (toDelete.length() > 0) {
                File toDeleteFile = new File(toDelete);
                toDeleteFile.delete();
            }
            boolean resize = isResizeNeccessery(image);
            File newImage;
            newImage = new File(p);
            if (resize) {
                resizeAndSaveImage(image, idPol, newImage);

            } else {



                /**
                if (newImage.exists()) {
                boolean dl = newImage.delete();
                newImage = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg");
                boolean cr = newImage.createNewFile();
                }
                 **/
                FileChannel outChannel = new FileOutputStream(newImage).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }

            }

            return p;
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

    private static void resizeAndSaveImage(File image, int idPol, File newImage) throws IOException {
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


        //newImage = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPol + ".jpg");
        System.out.println("IMAGE p " + image.getAbsolutePath());
        if (resizedImage == null) {
            System.out.println("RES NULL");
        }
        if (newImage == null) {
            System.out.println("NEW NULL");
        }
        ImageIO.write(resizedImage, "jpg", newImage);

    }

    public static void removeImage(int idPolozky) {
        String path = ItemsManager.getImagePath(idPolozky);
        File f = new File(path);
        f.delete();
    }

    public static String getImageFromURL(URL url, int idPolozky, String usedPath) throws IOException, FileNotFoundException, Exception {

        File dir = new File(System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep);
        boolean created = false;
        if (!dir.exists()) {
            created = dir.mkdirs();
        }

        BufferedImage img = ImageIO.read(url);
        String p = System.getProperty("user.home") + sep + "organizer" + sep + "images" + sep + idPolozky + "__.jpg";
        File image = new File(p);
        image.deleteOnExit();
        if (!image.exists()) {
            image.createNewFile();
        }

        ResampleOp resampleOp = new ResampleOp(img.getWidth(), img.getHeight());
        BufferedImage resizedImage = resampleOp.filter(img, null);

        ImageIO.write(resizedImage, "jpg", image);

        return saveImage(p, idPolozky, usedPath);

    }
}
