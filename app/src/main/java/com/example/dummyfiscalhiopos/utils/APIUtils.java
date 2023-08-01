package com.example.dummyfiscalhiopos.utils;

import android.os.StrictMode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class APIUtils
{
	/**
	 * Process int value that uses Payment Gateway
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal parseAPIAmount(String value)
	{
		try
		{
			String integerPart  = value.substring(0, value.length()-2);
			String decimalPart  = value.substring(value.length()-2, value.length());
			
			return new BigDecimal(Double.parseDouble(integerPart + "." + decimalPart));
		}
		catch (Exception e)
		{
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * Formats decimal number to communication specified API format.
	 * 
	 * @param bigDecimal
	 * @return
	 */
	public static String serializeAPIAmount(BigDecimal bigDecimal)
	{
		DecimalFormat df    = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
		String result       = df.format(bigDecimal.doubleValue());
		
		try
		{
			return result.replaceAll("\\.", "");
		}
		catch (NumberFormatException nfe)
		{
			return "000";
		}
	}
	
	/**
	 * Parse initialize action input params to key - value structure.
	 * 
	 * @param serializedPropeties
	 * @return
	 */
	public static Properties parseInitializeParameters(String serializedPropeties)
	{
		Properties properties = new Properties();
		
		try
		{
			if (serializedPropeties != null && !serializedPropeties.isEmpty())
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				Document document = dBuilder.parse(new ByteArrayInputStream(serializedPropeties.getBytes()));
				document.normalize();
				
				// Obtaining all params
				NodeList params = document.getElementsByTagName("Param");
				for (int i = 0; i < params.getLength(); i++)
				{
					Element param = (Element)params.item(i);
					String key = param.getAttribute("Key");
					String value = param.getTextContent();
					
					properties.put(key, value);
				}
			}
		}
		catch (Exception e) {}
		
		
		return properties;
	}

	// --------------------------------------------------------------------------------------------------------- //
	/**
	 * Crear XML para el tráfico entre HPC y módulos externos
	 */
	public static String createXMLFileExternalModuleTraffic(String name, byte[] data, String filePath) {
		// Generamos nombre del fichero, con el id del módulo externo y el id del objeto parseado
		String fileName = "Result_" + name;

		try {
			// Generamos el directorio en el que se guardan los archivos de tráfico
			// de módulos externos si no existe
			File directory = createDirectoryIfNecessary(filePath);

			// Creamos el fichero
			File file = createFile(fileName, filePath);

			// Escribimos en el fichero la información
			file = writeDataToFile(file, data);

			// Devolvemos la ruta del fichero para pasarla al módulo externo
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Crear directorio si es necesario
	 */
	public static File createDirectoryIfNecessary(String path) {
		// Si el directorio no existe, lo creamos
		File directory = new File(path);
		if (!directory.exists())
			directory.mkdirs();

		return directory;
	}

	/**
	 * Crear fichero, el nombre debe tener ya el sufijo del archivo
	 */
	public static File createFile(String fileName, String filePath) throws IOException {
		// Si el fichero ya existe, primero lo borramos
		File file = new File(filePath, fileName);
		if (file.exists())
			file.delete();

		// Creamos el nuevo fichero
		file.createNewFile();

		return file;
	}

	/**
	 * Escribir información data en el fichero
	 */
	public static File writeDataToFile(File file, byte[] data) throws IOException {
		// Si el fichero existe
		if (file.exists()) {
			OutputStream out = null;
			try {
				// Escribimos en este la información de data
				out = new FileOutputStream(file);

				out.write(data, 0, data.length);
				out.flush();
				out.close();
			} catch (IOException e) {
				// Si salta alguna excepción, cerramos Stream
				if (out != null) {
					try {
						out.close();
					} catch (IOException ioException) {
						throw ioException;
					}
				}
				throw e;
			}
		}
		return file;
	}

	/**
	 * Eliminar un directorio/fichero, eliminando antes los ficheros de su interior si es un directorio
	 */
	public static boolean deleteFile(File path) {
		// Si hay algun error no queremos que se notifique, si falla el
		// borrado de los ficheros ya se realizará al abrir de nuevo HPC
		try {
			// Si existe y es directorio
			if (path.exists() && path.isDirectory()) {
				// Listamos los ficheros que este contiene
				File[] files = path.listFiles();
				// Recorremos todos los ficheros del directorio
				for (int i = 0; i < files.length; i++) {
					// Si es directorio, recursivamente lo eliminamos
					if (files[i].isDirectory())
						deleteFile(files[i]);
						// Si es fichero, lo eliminamos
					else
						files[i].delete();
				}
			}
			// Cuando se han eliminado los ficheros y subdirectorios
			// del directorio pasado, ya podemos eliminar este
			return (path.delete());
		} catch (Exception ignored) {
		}
		return false;
	}

	/**
	 * Obtener String con la información que se ha leído del fichero pasado por parámetro
	 */
	public static String readFile(String filePath) {
		if (filePath == null)
			return null;

		File file = new File(filePath);

		// Si el fichero existe y es un fichero (no es un directorio)
		if (file.exists() && file.isFile()) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			StringBuilder stringBuilder = new StringBuilder();
			try {
 				BufferedReader br = new BufferedReader(new FileReader(filePath));
				String sCurrentLine;

				// Leemos línea a línea el fichero
				while ((sCurrentLine = br.readLine()) != null)
					stringBuilder.append(sCurrentLine).append("\n");

				return stringBuilder.toString();
			} catch (IOException e) {
				// Si salta cualquier excepción, devolvemos String vacío
				e.printStackTrace();
				return "";
			}
		}

		// Si no existe el fichero o es un directorio, devolvemos String vacío
		else
			return "";
	}

}
