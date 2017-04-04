package tesla.cposcanner.core;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main{
    public static void main( String[] args )    {
    	final List<Module> modules = new ArrayList<Module>();
		modules.add(new TeslaModule());
		
		log.info("Creating injector");
		final Injector injector = Guice.createInjector(modules);
		final TeslaCPOService service = injector.getInstance(TeslaCPOService.class);
		service.run();
    }
}
