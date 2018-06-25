package nimble.trust.engine.model.io.ext;

/**
 * 
 * A parser to parse SecProfileExpression onto Java Objects
 * 
 * @author marko
 * 
 */

public class SecProfileExpressionToModel {

//	private OntModel securityProfileModel;
//
//	public SecProfileExpressionToModel(OntModel securityProfileModel) {
//		this.securityProfileModel = securityProfileModel;
//	}
//	
//	public void parse(RDFNode node, SecurityAttribute attribute) {
//		parse(node.asResource().getURI(), attribute);
//	}
//
//	public void parse(String resourceURI, SecurityAttribute attribute) {
//		Resource resource = securityProfileModel.getResource(resourceURI);
//		Individual individual = resource.as(Individual.class);
//		fillSecurityGoals(individual, attribute);
//		fillSecurityMechanismAndTechnologies(individual, attribute);
//	}
//
//	private void fillSecurityGoals(Individual individual, SecurityAttribute attribute) {
//		NodeIterator goalNodes = individual.listPropertyValues(UsdlSec.hasSecurityGoal);
//		while (goalNodes.hasNext()) {
//			RDFNode rdfNode = (RDFNode) goalNodes.next();
//			SecurityGoal goal = new SecurityGoal(URI.create(rdfNode.asNode().getURI()));
//			fillTypes(goal, rdfNode.asResource());
//			attribute.addSecurityGoal(goal);
//		}
//	}
//
//	private void fillSecurityMechanismAndTechnologies(Individual individual, SecurityAttribute attribute) {
//		NodeIterator implementedByNodes = individual.listPropertyValues(UsdlSec.isImplementedBy);
//		while (implementedByNodes.hasNext()) {
//			RDFNode securityMechanismNode = (RDFNode) implementedByNodes.next();
//			Individual securityMechanismNodeIdvl = securityMechanismNode.as(Individual.class);
//			List<Resource> list = listSectypes(securityMechanismNodeIdvl);
//			for (Resource usdlSecMechanism : list) {
//				SecurityMechanism mechanism = new SecurityMechanism(URI.create(usdlSecMechanism.asNode().getURI()));
//				attribute.addImplementedBy(mechanism);
//				fillTypes(mechanism, securityMechanismNodeIdvl.asResource());
//				fillTechnologies(securityMechanismNodeIdvl, mechanism);
//			}
//		}
//	}
//
//	
//	private void fillTechnologies(Individual securityMechanismNode, SecurityMechanism mechanism) {
//		NodeIterator realizedByNodes = securityMechanismNode.listPropertyValues(UsdlSec.isRealizedByTechnology);
//		while (realizedByNodes.hasNext()) {
//			RDFNode securityTechnologyNode = (RDFNode) realizedByNodes.next();
//			SecurityTechnology technology = new SecurityTechnology(URI.create(securityTechnologyNode.asNode().getURI()));
//			mechanism.addRealizedByTechnology(technology);
//			Individual securityTechnologyIdvl = securityTechnologyNode.as(Individual.class);
//			fillTypes(mechanism, securityTechnologyIdvl.asResource());
//		}
//	}
//
//	private void fillTypes(com.inn.trusthings.model.pojo.TResource tResource, Resource asResource) {
//		Iterator<Resource> types = asResource.as(Individual.class).listRDFTypes(true);
//		while (types.hasNext()) {
//			Resource type = (Resource) types.next();
//			if (tResource != null) {
//				tResource.addType(new com.inn.trusthings.model.pojo.TResource(URI.create(type.getURI())));
//			}
//		}
//	}
//
//	/**
//	 * Lists types
//	 * @param individual
//	 * @return
//	 */
//	private List<Resource> listSectypes(Individual individual) {
//		ExtendedIterator<Resource> types = individual.listRDFTypes(true);
//		Filter<Resource> typeFilter = new Filter<Resource>() {
//			@Override
//			public boolean accept(Resource x) {
//				if (x.getURI().startsWith(OWL.NS) || x.getURI().startsWith(OWL2.NS)) {
//					return false;
//				}
//				return true;
//			}
//		};
//		return types.filterKeep(typeFilter).toList();
//	}

}
