package tesla.cposcanner.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * Scans given Tesla api endpoint and returns the JSON string
 * @author zhang165
 *
 */
@Singleton
@Slf4j
public class WebScanner {
	public String scan(String apiString) throws IOException {
		final URL url = new URL(apiString);
		final URLConnection urlConnection = url.openConnection();
		log.info("URL connection opened");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		urlConnection.connect();
		
		final StringBuilder sb = new StringBuilder();
		String line;
		try(final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));){
			while((line = in.readLine()) != null){
				sb.append(line);
			}
		}	
		log.info("Web scanner complete, result: {}",sb.toString());
		return sb.toString();
	}
}
