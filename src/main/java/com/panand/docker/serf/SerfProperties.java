package com.panand.docker.serf;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Docker Serf properties.
 */
public class SerfProperties {
	/**
	 *
	 * @return {@link Properties}
	 * @throws IOException
	 */
   public static Properties getSerfProperties() throws IOException {
	   
	   Properties properties = new Properties();
	   FileReader rd = new FileReader("serf.properties");
       properties.load(rd);
       
       return properties;
   }

}
