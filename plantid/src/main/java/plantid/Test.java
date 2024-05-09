package plantid;

import java.io.File;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.Map;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class Test {
	private static final String PROJECT = "all";
	private static final String URL = "https://my-api.plantnet.org/v2/identify/" + PROJECT + "?lang=es&include-related-images=true&api-key=2b10oHEhibsbL9uFqTzhqVFDGe";

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
        		
        		// Creem un objecte Gson configurat per generar una representació
        		 // JSON en mode "pretty printing" (bonic, ben tabulat).
        		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
        		 // Mostrem la conversió a String del JSON que ens ha retornat el
        		 // servidor, en mode pretty printing.
        		 System.out.println(gson.toJson(JsonParser.parseString(jsonString)));
        		 
        		// Fem la desserialització de la resposta obtinguda del servidor
        		 // (conversió de JSON a objecte tipus Resposta de Java).
        		 RespostaPlantNet r =
        		 new Gson().fromJson(jsonString, RespostaPlantNet.class);
        		 // Mostrem el valor de l'atribut bestMatch per consola.
        		 
        		 System.out.print("\n\n Language de la resposta: ");
        		 System.out.println(r.getLanguage());
        		 System.out.print(" Best Match de la resposta: ");
        		 System.out.println(r.getBestMatch());
        		 System.out.print(" Predered Referential de la resposta: ");
        		 System.out.println(r.getPreferedReferential());
        		 System.out.print(" Version de la resposta: ");
        		 System.out.println(r.getVersion());
        		 System.out.print(" Remaining Identification de la resposta: ");
        		 System.out.println(r.getRemainingIdentificationRequests());
        		 System.out.println("");
        		 
        		 // Mostrem l'score en tant per cent i el nom científic de tots els
        		 // resultats que conté la resposta que hem rebut.
        		 for (Resultat res: r.getResults()) 
        		 {
        			 System.out.printf("\n %s (%.2f%%)%n",
        					 res.getSpecies().getScientificName(),
        					 res.getScore() * 100);
        			 
        			 if (res.getSpecies().getCommonNames() == null || res.getSpecies().getCommonNames().isEmpty())
        				 continue;
        			 
        			 System.out.println(" Common names: ");
        			 int counter = 1;
        			 
        			 for (String s : res.getSpecies().getCommonNames())
        			 {
        				 System.out.printf(" %d: %s\n", counter, s);
        				 counter++;
        			 }
        			 
        			 for (Images i : res.getImages())
        			 {
        				 System.out.printf(" Organ: %s\n", i.getOrgan());
        				 System.out.printf(" Citation: %s\n", i.getCitation());
        				 System.out.println(" Urls: ");
        				 System.out.printf(" o: %s\n", i.getUrl().get("o"));
        				 System.out.printf(" m: %s\n", i.getUrl().get("m"));
        				 System.out.printf(" s: %s\n\n", i.getUrl().get("s"));
        			 }
        		 }
        	}
        	catch (IOException e)
        	{
        		System.out.println("Error al mostrar la informacion recibida por la API.");
        	}
        }
	}
}