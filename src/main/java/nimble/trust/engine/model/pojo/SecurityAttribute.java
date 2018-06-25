package nimble.trust.engine.model.pojo;



import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;

public class SecurityAttribute extends TrustAttribute {
	
	private List<SecurityMechanism> implementedBy =  Lists.newArrayList();

	private List<SecurityGoal> securityGoals = Lists.newArrayList();

	public SecurityAttribute(URI uri) {
		super(uri);
	}

	public void addImplementedBy(SecurityMechanism securityMechanism) {
		this.implementedBy.add(securityMechanism);
	}

	public void addSecurityGoal(SecurityGoal securityGoal) {
		this.securityGoals.add(securityGoal);
	}

	public List<SecurityMechanism> getImplementedBy() {
		return implementedBy;
	}

	public List<SecurityGoal> getSecurityGoals() {
		return securityGoals;
	}

	public void removeImplementedBy(SecurityMechanism securityMechanism) {
		this.implementedBy.remove(securityMechanism);
	}

	public void removeSecurityGoal(SecurityGoal securityGoal) {
		this.securityGoals.remove(securityGoal);
	}
	
	public List<SecurityTechnology> getRealizedByTechnology() {
		return realizedByTechnology;
	}

	public void addRealizedByTechnology(SecurityTechnology realizedByTechnology) {
		this.realizedByTechnology.add(realizedByTechnology);
	}
	
	public void removeRealizedByTechnology(SecurityTechnology realizedByTechnology) {
		this.realizedByTechnology.remove(realizedByTechnology);
	}

	private List<SecurityTechnology> realizedByTechnology =  Lists.newArrayList();

	@Override
	public String toString() {
		String s = "";
		List<SecurityGoal> l = getSecurityGoals();
		for (SecurityGoal g : l) {
			s = s + " goal: " + g.getUri();
		}
		List<SecurityMechanism> ll = getImplementedBy();
		for (SecurityMechanism m : ll) {
			s = s + " mechanism " + m.getUri();
		}
		List<SecurityTechnology> lll = getRealizedByTechnology();
		for (SecurityTechnology t : lll) {
			s = s + " technology " + t.getUri();
		}
		return getUri().toASCIIString() + " " + s;
	}

}
