package com.eulerhermes.dri.fileaid.DataParsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eulerhermes.dri.fileaid.Panels.StructuredDataPanel;
import com.eulerhermes.dri.fileaid.model.DataInfo;
import com.eulerhermes.dri.fileaid.model.Redefine;
import com.eulerhermes.dri.fileaid.model.TypeEnum;

public class DataParsing {

	private StructuredDataPanel dataPanel = null;
	private File copyFile = null;
	private File dataFile = null;
	
	/**
	 * Indique le panneau de données pour afficher le résultat
	 * @param dataPanel panneau de données qui va afficher le résultat
	 */
	public void setDataPanel(StructuredDataPanel dataPanel) {
		this.dataPanel = dataPanel;
	}
	
	/**
	 * Met à jour le fichier de la copy
	 * @param copyFile fichier de la copy
	 */
	public void setCopy(File copyFile) {
		this.copyFile = copyFile;
		
		// si on a les deux fichiers alors on les parse
		if(this.dataFile != null) parseData();
	}
	

	/**
	 * Met à jour le chemin du fichier de données
	 * @param dataFile chemin du fichier de données
	 */
	public void setData(File dataFile) {
		this.dataFile = dataFile;
		
		// si on a les deux fichiers alors on les parse
		if(this.copyFile != null) parseData();
	}
	
	/**
	 * parcours la copy pour en déduire la position de chaque donnée
	 * !!! ATTENTION !!!! Pour l'instant on on ne traite pas les redefine
	 * @return liste de DataInfo où chaque element correspond à une donnée de la copy, renvoie null en cas d'erreur de lecture
	 */
	private List<DataInfo> parseCopy() {
		
		// Ouverture de la copy
		BufferedReader copyReader;
		try {
			copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(copyFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		// initialisation du tableau de retour
		List<DataInfo> copyInfo = new ArrayList<>();
		
		try {

			// position initiale
			int position = 1;

			// Liste des Redefines
			ArrayList<Redefine> redefines = new ArrayList<>();

			// Boucle sur chaque instruction
			String instruction = getNextInstruction(copyReader);
			while (instruction != null){
				// passage de l'instruction entière en majuscules
				instruction = instruction.toUpperCase();

				// Découpe de la ligne en mots séparés par des espaces
				ArrayList<String> words = new ArrayList<>(List.of(instruction.split(" ")));

				// vérifie si on est dans une variable groupée ou pas
				boolean estVariableGroupe = !words.contains("PIC");

				// vérifie si l'instruction est un niveau 88
				boolean estNiveau88 = words.get(0).equals("88");

				// Gestion des redefine

				// fin des redefine
				while(!redefines.isEmpty() && Integer.parseInt(words.get(0)) <= redefines.get(redefines.size() - 1).getNiveau() ){
					Redefine r = redefines.get(redefines.size() - 1);
					position = Math.max(position, r.getPosition() + r.getTaille());
					redefines.remove(r);
					System.out.println("Fin de redefine, nouvelle position : " + position);
				}

				// ajout d'un nouveau redefine
				if(words.size() >= 3 && words.get(2).equalsIgnoreCase("REDEFINES")) {

					// on recherche la donnée redéfinie par ce redefine
					DataInfo donneeRedefinie = null;
					for (DataInfo d : copyInfo){
						if(d.getName().equals(words.get(3))) donneeRedefinie = d;
					}

					// contrôle qu'on a bien trouvé la donnée à redéfinir
					if (donneeRedefinie == null) {
						System.out.println("ERREUR : On veut redéfinir une donnée qu'on n'a pas dans la liste : " + words.get(3));
						return null;
					}

					// alimentation du redefine
					Redefine redefine = new Redefine();
					redefine.setNiveau(Integer.parseInt(words.get(0)));
					redefine.setPosition(donneeRedefinie.getPosition());
					redefine.setTaille(position - donneeRedefinie.getPosition());

					// ajout du redefine à la liste des redefine
					redefines.add(redefine);

					// repositionnement de la lecture au niveau du redefine
					position = redefine.getPosition();
					System.out.println("Nouveau redefine, nouvelle position : " + position);
				}

				// si on n'est pas dans une variable groupée et qu'on n'est pas dans un niveau 88
				// alors on ajoute son nom, sa taille et sa position
				if(!estVariableGroupe && !estNiveau88) {

					// alimentation du nouveau DataInfo
					DataInfo lineInfo = new DataInfo();
					lineInfo.setName(words.get(1)); // ATENTION : pour l'instant on considère que le nom est toujours la deuxième valeur
					lineInfo.setType(typeOf(words.get(3)));// ATENTION : pour l'instant on considère que la taille est toujours la quatrième valeur
					lineInfo.setSize(computeSize(words.get(3), lineInfo.getType()));
					lineInfo.setPosition(position);
					copyInfo.add(lineInfo);

					// incrémentation de la position
					position += lineInfo.getSize();
				}

				// lecture de la prochaine instruction
				instruction = getNextInstruction(copyReader);
			}

			// ferme le fichier
			copyReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// renvoi le résultat
		return copyInfo;
	}

	/**
	 * Récupère la prochaine instruction cobol d'un fichier cobol
	 * @param reader lecteur du fichier cobol
	 * @return la prochaine instruction cobol
	 * @throws IOException en cas d'erreur de lecture
	 */
	private String getNextInstruction(BufferedReader reader) throws IOException {
		// initialisation de l'instruction
		StringBuilder instruction = new StringBuilder();

		// Lecture de la prochaine ligne cobol
		String cobolLine = getNextCobolLine(reader);

		// booléen
		boolean estDansChaineDeCaractere = false;

		// boucle sur les lignes cobol tant que l'instruction n'est pas terminée et qu'on a encore des lignes à lire
		while (!cobolLine.equals("")) {

			//boucle sur chaque caractère de la ligne cobol pour chercher le point de fin d'instruction
			for (char c : cobolLine.toCharArray()){

				// tout d'abord on vérifie si on est dans une chaine de caractères
				if(c == '\'') {
					estDansChaineDeCaractere = !estDansChaineDeCaractere;
				}

				// si le caractère lu est un point alors on termine l'instruction ici
				if(!estDansChaineDeCaractere && c == '.') {
					return instruction.toString();
				} else {
					// sinon, ajoute le caractère lu à l'instruction
					instruction.append(c);
				}
			}

			// si on est ici c'est qu'on a terminé de lire la ligne cobol et que l'instruction n'est pas terminée
			// Alors on lis la prochaine instruction
			cobolLine = getNextCobolLine(reader);
			// et on ajoute un espace pour représenter l'aller à la ligne
			instruction.append(' ');
		}

		// si on arrive ici c'est qu'on a terminé le fichier et qu'on n'a pas terminé l'instruction.

		// La copy est mal foutue si l'instruction est en cours
		if(instruction.length() > 0){
			// TODO : renvoyer une erreur
			System.out.println("ERREUR : on a une instruction mal foutue : " + instruction);
			return null;
		}

		// si l'instruction est vide tout va bien, la dernière instruction envoyée était la dernière de la copy
		return null;
	}

	/**
	 * Lis un fichier cobol en entrée
	 * @param reader lecteur du fichier cobol en entrée
	 * @return prochaine ligne d'instructions utiles
	 * @throws IOException en cas de lecture
	 */
	private String getNextCobolLine(BufferedReader reader) throws IOException {

		// initialisation de la ligne
		String ligneCobol = "";

		// Lecture de la prochaine ligne
		String copyLine = reader.readLine();

		// condition d'arrêt : Si la ligne lue est vide, alors on renvoie l'instruction en cours
		if (copyLine == null) return ligneCobol;

		// on ignore les lignes de commentaire
		while (copyLine.charAt(6) == '*') {
			copyLine = reader.readLine();

			// condition d'arrêt : Si la ligne lue est vide, alors on renvoie l'instruction en cours
			if (copyLine == null) return ligneCobol;
		}

		// on conserve uniquement les informations utiles et on retire les espaces en trop
		ligneCobol = copyLine.substring(7,Math.min(copyLine.length(),72)).trim();

		return ligneCobol;
	}

	/**
	 * Determine le type d'une donnée à partir de sa déclaration
	 * @param sizeText déclaration du type de donnée en cobol
	 * @return le type de donnée dans un TypeEnum
	 */
	private TypeEnum typeOf(String sizeText) {
		// detection du type alphanumerique
		if(sizeText.contains("X")) return TypeEnum.ALPHANUM;
		
		// detection du type numerique décimal
		if(sizeText.contains("V")) return TypeEnum.FLOAT;
		
		// par élimination le reste c'est du numérique entier
		return TypeEnum.INTEGER;
	}
	
	/**
	 * calcule la taille d'un champ cobol
	 * @param sizeText taille du champ en cobol
	 * @return taille du champ en nombre de caractères
	 */
	private int computeSize(String sizeText, TypeEnum type) {
		
		switch(type) {
		case ALPHANUM: // cas des variables alphanumériques
			// si on a une version simplifiée 
			if(sizeText.startsWith("X("))
				return Integer.parseInt(sizeText.substring(2, sizeText.indexOf(')')));
			
			// version détaillée
			return 1; // TODO
			
		case INTEGER:
			// si on a une version simplifiée 
			if(sizeText.startsWith("9("))
				return Integer.parseInt(sizeText.substring(2, sizeText.indexOf(')')));
			
			// version détaillée
			return 1; // TODO
			
		case FLOAT:
			return 1;// TODO
		}
		
		return 0;
	}
	
	/**
	 * Lis la copy et le fichier de données pour déterminer les valeurs du fichier de données
	 */
	public void parseData(){
		
		// interpretation de la copy
		List<DataInfo> copyInfo = parseCopy();

		// contrôme des données
		if(copyInfo == null){
			// TODO envoyer un message d'erreur
			return;
		}
		
		// initialisation de la première ligne
		List<List<String>> donneesInterpretees = new ArrayList<>(); // création du tableau
		donneesInterpretees.add(new ArrayList<>()); // création de la ligne d'entête
		for(DataInfo ci : copyInfo) {
			donneesInterpretees.get(0).add(ci.getName());
		}
		
		try {
			// Ouverture du fichier de données
			BufferedReader dataReader;
			dataReader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));

			// Boucle sur chaque ligne
			String dataLine;
			int numLigne = 0;
			dataLine = dataReader.readLine();
			while (dataLine != null) {
				// incrémentation du nombre de lignes
				numLigne++;

				// ajout d'une ligne dans les données interpretées
				donneesInterpretees.add(new ArrayList<>());
				
				// on parcours les données de la copy et on les interprete
				for(DataInfo ci : copyInfo) {
					int debDonnee = ci.getPosition() - 1;
					int finDonnee = ci.getSize() + ci.getPosition() - 1;

					// si la copy décrit plus de colonnes que ce que le fichier contien
					if(debDonnee > dataLine.length()){
						donneesInterpretees.get(numLigne).add(""); // on n'ajoute rien
					} else { // sinon, on ajoute ce qu'on a
						finDonnee = Math.min(finDonnee, dataLine.length());
						donneesInterpretees.get(numLigne).add(dataLine.substring(debDonnee, finDonnee));
					}
				}
				
				// lecture de la prochaine ligne
				dataLine = dataReader.readLine();
			}
			
			// fermeture du fichier
			dataReader.close();
			
		} catch (FileNotFoundException e) {
			// TODO envoyer un message d'erreur
			e.printStackTrace();
		} catch (IOException e) {
			// TODO envoyer un message d'erreur
			e.printStackTrace();
		}
		
		// envoi des données au panneau de données pour affichage
		dataPanel.setStructuredData(donneesInterpretees);
	}
}
