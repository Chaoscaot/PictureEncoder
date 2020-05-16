import pictureencoder.PictureEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Test {

    public static void main(String[] args) throws Exception {
        File f = new File("D:\\Downloads\\AB Meinungskurve, AB Lesetagebuch, AB Autorenrecherche, AB Einmal ein guter Lehrer sein.pdf");
        BufferedImage img = PictureEncoder.encode(f);
        ImageIO.write(img, "png", new File("save.png"));
    }
}
