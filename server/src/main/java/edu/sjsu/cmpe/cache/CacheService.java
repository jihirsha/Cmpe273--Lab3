package edu.sjsu.cmpe.cache;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import edu.sjsu.cmpe.cache.api.resources.CacheResource;
import edu.sjsu.cmpe.cache.config.CacheServiceConfiguration;
import edu.sjsu.cmpe.cache.repository.CacheInterface;
import edu.sjsu.cmpe.cache.repository.ChronicleMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CacheService extends Service<CacheServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    static String serverURL;
    public static void main(String[] args) throws Exception {
        serverURL = args[1];
        new CacheService().run(args);

    }

    @Override
    public void initialize(Bootstrap<CacheServiceConfiguration> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfiguration configuration,
            Environment environment) throws Exception {
        /** Cache APIs */
        CacheInterface cache = new ChronicleMapCache(getServerName());
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");

    }
    public String getServerName()
    {
        String[] strServerConfig = serverURL.split("/");
        String[] strServer = strServerConfig[1].split("\\.");
        log.info(strServer[0]);
        String strServerName =strServer[0].substring(0,strServer[0].length()-7);
        log.info(strServerName);
        return strServerName;
    }
}
