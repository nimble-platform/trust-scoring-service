package nimble.trust.engine.op.propagation;



import nimble.trust.engine.model.pojo.Agent;
public class TrustAssessmentManagerImpl implements TrustAssessmentManager{

	@Override
	public void assessIt(Agent agent) {
		
		if ( isComposed(agent) == false) return ;
		
		// decompose & analize
		
	}

	private boolean isComposed(Agent agent) {
		//TODO isComposed of TrustAssessmentManagerImpl implement me
		/*
		 * there should be info if agent is composed
		 */
		return true;
	}

}
