package nimble.trust.engine.model.types;



import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.RDFDatatype;

public class USDLSecExpression extends BaseDatatype{

	public USDLSecExpression(String uri) {
		super(uri);
	}
	
	public static final String theTypeURI = "http://www.nimble-project.org/ns/security/profiles#USDLSecType";
    public static final RDFDatatype TYPE = new USDLSecExpression(theTypeURI);
    
    @Override
    public String getURI() {
    	return super.getURI();
    }


}
