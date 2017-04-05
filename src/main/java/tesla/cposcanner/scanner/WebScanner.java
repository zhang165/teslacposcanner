package tesla.cposcanner.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebScanner {
	private static final String URL_STRING = "https://www.tesla.com/preowned?model=ms";
	private static final String API_STRING = "https://www.tesla.com/cpo_tool/ajax?exteriors=all&model=MODEL_S&priceRange=0%2C200000&city=null&state=null&country=US";
	
	public WebScanner(){
		
	}
	
	public void scan() throws IOException {
		final URL url = new URL(API_STRING);
		final URLConnection urlConnection = url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		urlConnection.connect();
		
		String line;
		try(final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));){
			while((line = in.readLine()) != null){
				System.out.println(line);
			}
		}	
	}
}
