package tesla.cposcanner.scanner;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebScanner {
	private static final String URL_STRING = "https://www.tesla.com/preowned?model=ms";

	public void scan() throws IOException {
		Document doc = Jsoup.connect(URL_STRING).get();
		Elements links = doc.select("a[href]");
		System.out.println(links.toString());
	}

}
