package boxes;

import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class GameUtils {

    public static <T> T loadObjectFromXmlFile(T object, String filename) {
        XStream xstream = new XStream();

        // do NOT serialize these attributes
        xstream.omitField(FrameSequence.class, "singleFrameFraction");

        try (FileInputStream fs = new FileInputStream(filename)) {
            return (T) xstream.fromXML(fs);
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error while loading '" + filename + "'. " + ex.toString());
            return null;
        }
        catch (IOException ex) {
            System.out.println("Error while loading '" + filename + "'. " + ex.toString());
            return null;
        }
    }

    public static <T> void writeObjectToXmlFile(T object, String filename) {
        XStream xstream = new XStream();

        // do NOT serialize these attributes
        xstream.omitField(FrameSequence.class, "singleFrameFraction");

        try (FileOutputStream fs = new FileOutputStream(filename)) {
            xstream.toXML(object, fs);
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error while writing '" + filename + "'. " + ex.toString());
        }
        catch (IOException ex) {
            System.out.println("Error while writing '" + filename + "'. " + ex.toString());
        }
    }

    public static Texture loadTexturePng(String filename) {
        try (FileInputStream fs = new FileInputStream(filename)) {
            return TextureLoader.getTexture("PNG", fs, GL_LINEAR);
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error while loading texture '" + filename + "'. " + ex.toString());
            return null;
        }
        catch (IOException ex) {
            System.out.println("Error while loading texture '" + filename + "'. " + ex.toString());
            return null;
        }
    }
}
