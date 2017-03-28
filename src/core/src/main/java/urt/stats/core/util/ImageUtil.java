package urt.stats.core.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

/**
 * Image utilities
 * 
 * @author ghost
 *
 */
public class ImageUtil {

    /**
     * Default image type
     */
    public static final String DEFAULT_IMG_TYPE = "PNG";
    
    /**
     * Resizes image according to the specified frame.
     * Creates a new file for resized image and returns this file.
     * Source image file will be deleted if deleteSrcFile is <code>true</code>.
     * 
     * @param imageFile - image file
     * @param frameWidth - prefered frame width
     * @param frameHeight - prefered frame height
     * @param doVerticalAlign - if <code>true</code> then resized image will be vertical aligned(if needed)
     * @param doHorizontalAlign - if <code>true</code> then resized image will be horizontal aligned(if needed)
     * @param deleteSrcFile - if <code>true</code> then source image file will be deleted
     * @return resized Image path as <code>String</code>
     * @throws Exception shows when any exception appears
     */
    public static File getResizedImage(File imageFile, int frameWidth, int frameHeight, boolean doVerticalAlign, boolean doHorizontalAlign, boolean deleteSrcFile) throws Exception{

        // creating resized image file path
        String filePath = imageFile.getParent() + File.separator;
        String resizedFilePath = imageFile.getName();
        int pos = resizedFilePath.lastIndexOf(".");
        if(pos == -1){
            resizedFilePath = resizedFilePath + "_" + frameWidth + "_" + frameHeight;
        }else{
            resizedFilePath = resizedFilePath.substring(0, pos)
               + "_" + frameWidth + "_" + frameHeight + ".png";
        }
        resizedFilePath = filePath + resizedFilePath;

        BufferedImage srcImage = null;

        try{
            srcImage = ImageIO.read(imageFile);
        }catch(Throwable e){
            throw new RuntimeException(e);
        }

        if(srcImage == null){
            StringBuffer message = new StringBuffer();
            message.append("ImageIO: Failed to read image from file, unrecognized mime type! ");
            message.append("\nSupported by system mime types to read are: ");
            String[] supportedMimeTypes = ImageIO.getReaderMIMETypes();
            for(int i=0; i< supportedMimeTypes.length; i++){
                String mimeType = supportedMimeTypes[i];
                message.append(mimeType);
                message.append("; ");
            }
            throw new RuntimeException(message.toString());
        }

        // computing resize rates and sizes
        int srcImgWidth = srcImage.getWidth();
        int srcImgHeight = srcImage.getHeight();

        float widthRate = (float) frameWidth / srcImgWidth;
        float heightRate = (float) frameHeight / srcImgHeight;

        float resizedWidth;
        float resizedHeight;

        if(widthRate >= 1 && heightRate >= 1){
            resizedWidth = srcImgWidth;
            resizedHeight = srcImgHeight;

            widthRate = 1;
            heightRate = 1;
        }else{
            if(widthRate < heightRate){
                resizedWidth = frameWidth;
                resizedHeight = widthRate * srcImgHeight;
                heightRate = widthRate;
            }else{
                resizedWidth = heightRate * srcImgWidth;
                resizedHeight = frameHeight;
                widthRate = heightRate;
            }

        }

        // performing vertical and horizontal align (if needed)
        int drawX = 0;
        int drawY = 0;
        if(doHorizontalAlign && resizedWidth < frameWidth){
            drawX = Math.round((frameWidth - resizedWidth)/2);
        }
        if(doVerticalAlign && resizedHeight < frameHeight){
            drawY = Math.round((frameHeight - resizedHeight)/2);
        }
        BufferedImage destImage =
                Scalr.resize(srcImage
                           , Scalr.Method.QUALITY
                           , Scalr.Mode.AUTOMATIC
                           , (int)resizedWidth
                           , (int)resizedHeight
                           , Scalr.OP_ANTIALIAS);
        BufferedImage dest = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D destGraphisc = dest.createGraphics();
        destGraphisc.drawImage(destImage, null, drawX, drawY);

        File targetFile = new File(resizedFilePath);
        ImageIO.write( dest, DEFAULT_IMG_TYPE, targetFile); 

        if(deleteSrcFile){
            // deleting source image file
            if (!imageFile.delete()) {
                throw new IOException("Can't delete temporary image file");
            }
        }

        return targetFile;
    }
}
