import pictureencoder.PictureDecodeResult;
import pictureencoder.PictureEncoder;
import yapi.file.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Test {

    public static void main(String[] args) throws Exception {
        File f = new File("D:\\Downloads\\a");
        BufferedImage img = PictureEncoder.encode(f);
        ImageIO.write(img, "png", new File("save.png"));

        PictureDecodeResult result = PictureEncoder.decode(img);
        System.out.println(result.toString());
        boolean b = false;
        f = new File(result.getName());
        if(!f.exists()) f.createNewFile();
        while (result.isNotEmpty()) {
            FileUtils.dump(f, result.getBytes(1024), b);
            b = true;
        }

    }


}
