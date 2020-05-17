package pictureencoder;

import yapi.datastructures.circular.CircularBuffer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class PictureEncoder {
    private PictureEncoder() {
        throw new IllegalStateException("Utility class");
    }

    public static BufferedImage encode(File f) throws IOException {
        long size = 3 + f.getName().length()*3 + 3 + f.length();
        int img_dim = (int) Math.ceil(Math.sqrt(size/3.0));
        long trailing = (long) img_dim*img_dim*3 - size;

        BufferedImage img = new BufferedImage(img_dim, img_dim, BufferedImage.TYPE_INT_RGB);
        long offset = 0;

        setColor(img, getX(offset, img_dim),getY(offset, img_dim) , toRGB(toBytes(f.getName().length())));
        offset++;

        byte[] name = f.getName().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < name.length; i++) {
            setColor(img, getX(offset, img_dim),getY(offset, img_dim) , toRGB(name[i]));
            offset++;
        }

        trailing = trailing % (255*255*255);
        System.out.println(trailing);
        System.out.println(size);
        System.out.println(img_dim);
        setColor(img, getX(offset, img_dim),getY(offset, img_dim) , toRGB(toBytes(((int) trailing))));
        offset++;

        FileInputStream fis = new FileInputStream(f);
        int[] buffer = new int[3];
        byte buffer_offset = -1;

        while (true) {
            int i = fis.read();
            if(i == -1) break;
            buffer_offset++;
            buffer[buffer_offset] = i;
            if(buffer_offset == 2) {
                setColor(img, getX(offset, img_dim),getY(offset, img_dim) , toRGB(buffer));
                offset++;
                buffer_offset = -1;
            }
        }
        if(buffer_offset != -1){
            while(buffer_offset < 2){
                buffer_offset++;
                buffer[buffer_offset] = 0;
            }
            setColor(img, getX(offset, img_dim),getY(offset, img_dim) , toRGB(buffer));
        }

        fis.close();

        return img;
    }

    public static PictureDecodeResult decode(BufferedImage img) {
        long offset = 0;
        int img_dim = img.getWidth();
        long size = (long) img.getWidth() * img.getHeight() * 3;
        PictureDecodeResult result = new PictureDecodeResult();

        int name_lenght = fromBytes(fromRGB(getColor(img, getX(offset, img_dim) ,getY(offset, img_dim))));
        offset++;
        size-=3;

        StringBuilder st = new StringBuilder();
        while(name_lenght > 0) {
            st.append((char)fromRGB(getColor(img, getX(offset, img_dim) ,getY(offset, img_dim)))[0]);

            name_lenght--;
            offset++;
            size-=3;
        }
        result.name = st.toString();

        int trailing = fromBytes(fromRGB(getColor(img, getX(offset, img_dim) ,getY(offset, img_dim))));
        offset++;
        size-=trailing+3;

        while(size > 0) {
            int[] color = fromRGB(getColor(img, getX(offset, img_dim) ,getY(offset, img_dim)));
            result.add((byte)color[0]);
            size--;
            if(size > 0) {
                result.add((byte)color[1]);
                size--;
            }
            if(size > 0) {
                result.add((byte)color[2]);
                size--;
            }
            offset++;
        }

        return result;
    }

    private static int[] toBytes(int lenght) {
        int x1 = lenght%255;
        int x2 = (lenght-x1)/255%255;
        int x3 = (lenght-x1-x2*255)/255/255%255;
        return new int[] {x1, x2 ,x3};
    }

    private static int fromBytes(int[] bytes) {
        return bytes[2]*255*255+bytes[1]*255+bytes[0];
    }

    private static int toRGB(int[] rgb) {
        return new Color(rgb[0], rgb[1], rgb[2]).getRGB();
    }

    private static int toRGB(int g) {
        return new Color(g, g, g).getRGB();
    }

    private static int[] fromRGB(int rgb) {
        Color c = new Color(rgb);
        return new int[] {c.getRed(), c.getGreen(), c.getBlue()};
    }

    private static void setColor(BufferedImage img, int x, int y, int rgb) {
        try {
            img.setRGB(x, y, rgb);
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println(x + " " + y + " > " + rgb);
            throw e;
        }
    }

    private static int getColor(BufferedImage img, int x, int y){
        return img.getRGB(x, y);
    }

    private static int getX(long offset, int img_dim) {
        return (int) (offset%img_dim);
    }

    private static int getY(long offset, int img_dim) {
        return (int) ((offset-(offset%img_dim))/img_dim);
    }
}
