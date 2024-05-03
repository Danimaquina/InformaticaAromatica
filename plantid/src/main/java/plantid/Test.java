package plantid;

import java.io.File;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Test {
	private static final String IMAGE1 = "C:/Users/Dani/Desktop/a.jpg";
	private static final String IMAGE2 = "C:/Users/Dani/Desktop/b.png";

	private static final String PROJECT = "all";
	private static final String URL = "https://my-api.plantnet.org/v2/identify/" + PROJECT + "?api-key=2b10oHEhibsbL9uFqTzhqVFDGe";

	public static void main(String[] args) 
	{
        int resultat;
        JFileChooser selectorArxius = new JFileChooser();
        FileNameExtensionFilter filtre = new FileNameExtensionFilter("Imatges PNG i JPG", "png", "jpg");
        selectorArxius.setFileFilter(filtre);
        selectorArxius.setMultiSelectionEnabled(true);
        NumberFormat formatador = NumberFormat.getInstance();
        
        // Hacemos que el directorio por defecto de selector sea el directorio en el que se esta ejecutando el programa
        String currentDirectory = System.getProperty("user.dir");
        selectorArxius.setCurrentDirectory(new File(currentDirectory));
        
        resultat = selectorArxius.showOpenDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
        	// Creem l'objecte entity builder que ens permet gestionar la
        	// creació del contingut de la petició d'identificació.
        	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        	// Bucle que es repeteix un cop per cada arxiu triat per l'usuari.
        	for (File f: selectorArxius.getSelectedFiles()) 
        	{
        		ImageIcon imatge = new ImageIcon(f.getAbsolutePath());
        		imatge.setImage(imatge.getImage()
        				.getScaledInstance(600, -1, Image.SCALE_SMOOTH));
        		// Mostrem la imatge a l'usuari i li demanem que identifiqui
        		// l'òrgan de la planta més representatiu.
        		String resposta = (String) JOptionPane.showInputDialog(
        				null, new JLabel(imatge),
        				String.format("%s - %s Bytes - %.1f MBytes",
        						f.getName(), formatador.format(f.length()),
        						f.length() / (1024.0 * 1024.0)),
        				JOptionPane.PLAIN_MESSAGE, null,
        				new String[] {"Leaf", "Flower", "Fruit", "Bark"},
        				"Leaf");
        		if (resposta != null) {
        			// Si l'usuari ha confirmat la resposta (ha premut el botó
        			// OK), passem el valor a minúscules (l'API ho requereix).
        			resposta = resposta.toLowerCase();
        		}
        		else 
        		{
        			// Si l'usuari finalitza prement Cancel·lar, passarem el
        			// valor "auto" perquè el servidor ho intenti detectar.
        			resposta = "auto";
        		}
        		// Afegim la imatge al contingut de la petició, adjuntant
        		// l'òrgan de la planta que ens ha indicat l'usuari.
        		builder.addPart("images", new FileBody(f))
        		.addTextBody("organs", resposta);
        	}
        	// Donem l'ordre de construir el contingut (entity) de la petició.
        	HttpEntity entity = builder.build();
        	// Continuem amb la petició com a l'exemple de Pl@ntNet.
        	HttpPost request = new HttpPost(URL);
        	request.setEntity(entity);
        	HttpClient client = HttpClientBuilder.create().build();
        	HttpResponse response;
        	
        	try
        	{
        		response = client.execute(request);
        		String jsonString = EntityUtils.toString(response.getEntity());
        		System.out.println(jsonString);
        	}
        	catch (IOException e)
        	{
        		System.out.println("Error al mostrar la informacion recibida por la API.");
        	}
        }
	}
}