import pictureencoder.PictureDecodeResult;
import pictureencoder.PictureEncoder;
import yapi.encryption.encryption.EncryptionOutputStreamProcessor;
import yapi.encryption.fastencryption.FastEncryptionSymmetricAsyncStream;
import yapi.encryption.fastencryption.FastEncrytptionSymmetric;
import yapi.file.FileUtils;
import yapi.runtime.ThreadUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {

    public static void main(String[] args) throws Exception {
        File f = new File("D:\\Downloads\\Flachen-Stationen.pdf");
        //File f = new File("save.png");
        BufferedImage img = PictureEncoder.encode(f);
        ImageIO.write(img, "png", new File("save.png"));
        FastEncryptionSymmetricAsyncStream s = new FastEncryptionSymmetricAsyncStream(new FileInputStream(new File("save.png")));
        s.setEncryptionStreamProcessor(new EncryptionOutputStreamProcessor(new FileOutputStream(new File("save.png.encrypted"))));
        s.encrypt(FastEncrytptionSymmetric.createKey("123"));
        while(s.isNotDone()){
            System.out.println(s.nPerSecond() + " " + s.getDoneBytes());
            ThreadUtils.sleep(100);
        }
        ImageIO.write(PictureEncoder.encode(new File("save.png.encrypted")), "png", new File("save.png.encrypted.png"));

        //BufferedImage img = ImageIO.read(f);
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
