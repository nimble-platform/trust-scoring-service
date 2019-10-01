package nimble.trust.engine.restclient;

import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Ayeshmantha Perera
 */
@Component
public class IndexingClientFallback implements IndexingClient {
    @Override
    public Boolean partyTrustUpdate(String partyId,PartyType partyType,String bearerToken) {
        return false;
    }
}
