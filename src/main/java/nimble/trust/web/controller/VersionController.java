package nimble.trust.web.controller;

import nimble.trust.swagger.api.VersionApi;
import nimble.trust.swagger.model.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


@Controller
public class VersionController implements VersionApi {

    private String versionString;

    private String serviceId;
    
    private static Logger log = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    public VersionController(@Value("${build.version}") String versionString,
                             @Value("${spring.application.name}") String serviceId) {
        this.versionString = versionString;
        this.serviceId = serviceId;
    }

    public ResponseEntity<Version> versionGet() {
    	log.info(versionString);
        return new ResponseEntity<>(VersionFactory.create(serviceId, versionString), HttpStatus.OK);
    }

    private static class VersionFactory {
        static Version create(String serviceId, String version) {
            Version v = new Version();
            v.setVersion(version);
            v.setServiceId(serviceId);
            return v;
        }
    }
}
