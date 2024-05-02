import java.awt.Image;
import java.io.File;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class test {
    public static void main(String[] args) {
        int resultat;
        JFileChooser selectorArxius = new JFileChooser();
        FileNameExtensionFilter filtre = new FileNameExtensionFilter("Imatges PNG i JPG", "png", "jpg");
        selectorArxius.setFileFilter(filtre);
        selectorArxius.setMultiSelectionEnabled(true);
        
        // Hacemos que el directorio por defecto de selector sea el directorio en el que se esta ejecutando el programa
        String currentDirectory = System.getProperty("user.dir");
        selectorArxius.setCurrentDirectory(new File(currentDirectory));
        
        resultat = selectorArxius.showOpenDialog(null);

        if (resultat == JFileChooser.APPROVE_OPTION) {
            for (File f: selectorArxius.getSelectedFiles()) {
                ImageIcon imatge = new ImageIcon(f.getAbsolutePath());
                imatge.setImage(imatge.getImage().getScaledInstance(
                    600, -1, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                    null, new JLabel(imatge), f.getName() + " - " + f.length()  + "bytes" + " - " + f.length() / (1024.0 * 1024) + " MBytes", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}