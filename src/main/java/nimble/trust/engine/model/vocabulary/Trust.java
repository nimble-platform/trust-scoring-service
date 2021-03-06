
package nimble.trust.engine.model.vocabulary; 
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
 

public class Trust {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.nimble-project.org/ns/trust#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /** <p>The ontology's owl:versionInfo as a string</p> */
    public static final String VERSION_INFO = "1.1.0";
    
    public static final Property forContext = m_model.createProperty( "http://www.nimble-project.org/ns/trust#forContext" );
    
    public static final Property hasAttribute = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasAttribute" );
    
    public static final Property hasCertificateDetail = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasCertificateDetail" );
    
    public static final Property hasCriteria = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasCriteria" );
    
    /** <p>Importance or weigth.</p> */
    public static final Property hasImportance = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasImportance" );
    
    public static final Property hasMaxValue = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasMaxValue" );
    
    public static final Property hasMetric = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasMetric" );
    
    public static final Property hasMetricValue = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasMetricValue" );
    
    public static final Property hasMinValue = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasMinValue" );
    
    /** <p>attaches to any instance</p> */
    public static final Property hasName = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasName" );
    
    public static final Property hasProfile = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasProfile" );
    
    public static final Property hasScore = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasScore" );
    
    public static final Property hasTimestamp = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasTimestamp" );
    
    public static final Property hasValue = m_model.createProperty( "http://www.nimble-project.org/ns/trust#hasValue" );
    
    public static final Property isTrustProfileOf = m_model.createProperty( "http://www.nimble-project.org/ns/trust#isTrustProfileOf" );
    
    public static final Property next = m_model.createProperty( "http://www.nimble-project.org/ns/trust#next" );
    
    public static final Property rank = m_model.createProperty( "http://www.nimble-project.org/ns/trust#rank" );
    
    public static final Property recommendedCriteria = m_model.createProperty( "http://www.nimble-project.org/ns/trust#recommendedCriteria" );
    
    public static final Property suggestedMatcher = m_model.createProperty( "http://www.nimble-project.org/ns/trust#suggestedMatcher" );
    
    public static final Property trustedParticipant = m_model.createProperty( "http://www.nimble-project.org/ns/trust#trustedParticipant" );
    
    public static final Property trustingParticipant = m_model.createProperty( "http://www.nimble-project.org/ns/trust#trustingParticipant" );
    
    public static final Resource Accuracy = m_model.createResource( "http://www.nimble-project.org/ns/trust#Accuracy" );
    
    /** <p>An agent is a entity who participates in a trust relationship. An agent can 
     *  be any resource such as software agent, human, software service, etc. An agent 
     *  engages in relationships with other agents to meet certian goals withing some 
     *  context or domain, under belief (trust) that other agent will provide what 
     *  claimed to provide. Therefore, an agent may have trust requerments as a precondition 
     *  for engaging into a relationship with other agent. Each agent has a trust 
     *  profile, which is a set of its characteristics (aspects, attributes) to meet 
     *  trust requrments.</p>
     */
    public static final Resource Agent = m_model.createResource( "http://www.nimble-project.org/ns/trust#Agent" );
    
    public static final Resource Availability = m_model.createResource( "http://www.nimble-project.org/ns/trust#Availability" );
    
    public static final Resource CertificateAuthorityAttribute = m_model.createResource( "http://www.nimble-project.org/ns/trust#CertificateAuthorityAttribute" );
    
    /** <p>A context within a trust relationship is being or to be established. E.g. 
     *  public transport, tourism, retail. For each context, there can be a recommended 
     *  req trust profile(s) provided, in general, or agent can have req trust profile 
     *  defined for the context. The term context defines the nature of the service 
     *  or service functions, and each Context has a name, a type and a functional 
     *  specification, such as rent a car or buy a book Context Type can be matched 
     *  to a Service Type.</p>
     */
    public static final Resource Context = m_model.createResource( "http://www.nimble-project.org/ns/trust#Context" );
    
    /** <p>Contract Compliance can be a measure of compliance of actual behaviour of 
     *  a service with respect to the service contract. This informatin may be comming 
     *  out from COMPOSE Activity Monitors</p>
     */
    public static final Resource ContractCompliance = m_model.createResource( "http://www.nimble-project.org/ns/trust#ContractCompliance" );
    
    public static final Resource ContractComplianceCount = m_model.createResource( "http://www.nimble-project.org/ns/trust#ContractComplianceCount" );
    
    public static final Resource Frequency = m_model.createResource( "http://www.nimble-project.org/ns/trust#Frequency" );
    
    /** <p>A matcher represents an matching technique or algorithm or strategy to compute 
     *  the level of trust between two agents. The agents expose their trust profiles 
     *  (capabilities) and impose their trust criteria (requirments), which are then 
     *  used by a matcher to assess whether trustworthy relationship can be established 
     *  or not. I.e., whether trust capabilities meet trust requirments. E.g., concrete 
     *  matchers are Additive Weighting or TOPSIS.</p>
     */
    public static final Resource Matcher = m_model.createResource( "http://www.nimble-project.org/ns/trust#Matcher" );
    
    public static final Resource MeasurableTrustAttribute = m_model.createResource( "http://www.nimble-project.org/ns/trust#MeasurableTrustAttribute" );
    
    /** <p>A metric as a measurement for specific trust attribute</p> */
    public static final Resource Metric = m_model.createResource( "http://www.nimble-project.org/ns/trust#Metric" );
    
    /** <p>A list of values of metric, if applicable.</p> */
    public static final Resource MetricValue = m_model.createResource( "http://www.nimble-project.org/ns/trust#MetricValue" );
    
    public static final Resource NumberOfCompositions = m_model.createResource( "http://www.nimble-project.org/ns/trust#NumberOfCompositions" );
    
    public static final Resource NumberOfDevelopers = m_model.createResource( "http://www.nimble-project.org/ns/trust#NumberOfDevelopers" );
    
    public static final Resource NumberOfRequests = m_model.createResource( "http://www.nimble-project.org/ns/trust#NumberOfRequests" );
    
    /** <p>Popularity of an agent. It may be provided as simple as enumeration of two 
     *  values - popular and no-popular - or as a numerical value e.g. any number 
     *  between 0 and 1. (In COMPOSE, provevance information can be used to calculate 
     *  popularity)</p>
     */
    public static final Resource Popularity = m_model.createResource( "http://www.nimble-project.org/ns/trust#Popularity" );
    
    public static final Resource PopularityCount = m_model.createResource( "http://www.nimble-project.org/ns/trust#PopularityCount" );
    
    public static final Resource ProviderCategoryBy3rdParty = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProviderCategoryBy3rdParty" );
    
    public static final Resource ProviderLocation = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProviderLocation" );
    
    public static final Resource ProviderWebReputationBy3rdParty = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProviderWebReputationBy3rdParty" );
    
    /** <p>Any attribute about quality of service.</p> */
    public static final Resource QoSAttribute = m_model.createResource( "http://www.nimble-project.org/ns/trust#QoSAttribute" );
    
    /** <p>Reputation of an agent. It may be provided as simple as enumeration of predefined 
     *  reputation values (unknown, bad, good, very good, excellent) or as a numerical 
     *  value e.g. any number between 0 and 1.</p>
     */
    public static final Resource Reputation = m_model.createResource( "http://www.nimble-project.org/ns/trust#Reputation" );
    
    public static final Resource ResponseTime = m_model.createResource( "http://www.nimble-project.org/ns/trust#ResponseTime" );
    
    /** <p>Any attribute about security aspects. E.g. authentication, authorization, 
     *  identification, etc.</p>
     */
    public static final Resource SecurityAttribute = m_model.createResource( "http://www.nimble-project.org/ns/trust#SecurityAttribute" );
    
    public static final Resource SecurityGuarantee = m_model.createResource( "http://www.nimble-project.org/ns/trust#SecurityGuarantee" );
    
    public static final Resource SecurityRequirment = m_model.createResource( "http://www.nimble-project.org/ns/trust#SecurityRequirment" );
    
    /** <p>Any parameter (feature, property, characteristics) that is relevant to a perception 
     *  of trust. E..g. security-related features (authorization mechanism, authentication 
     *  mechanism, data encryption), quality of service apects (reponse, data quality), 
     *  reputation, etc.</p>
     */
    public static final Resource TrustAttribute = m_model.createResource( "http://www.nimble-project.org/ns/trust#TrustAttribute" );
    
    /** <p>Trust criteria is a set of requested trust attributes, with their values and 
     *  preference. In other words, it is a triple &lt;trust attribute/indicator, 
     *  value, relevance&gt;. For example, &lt;'reputation', 'good', '1'&gt;, &lt;'popularity', 
     *  '0.5', '1'&gt;}</p>
     */
    public static final Resource TrustCriteria = m_model.createResource( "http://www.nimble-project.org/ns/trust#TrustCriteria" );
    
    /** <p>Trust prifile is a set of trust-related attributes of an agent, e.g. security 
     *  attributes, reputation, QoS attributes, etc.</p>
     */
    public static final Resource TrustProfile = m_model.createResource( "http://www.nimble-project.org/ns/trust#TrustProfile" );
    
    /** <p>TrustRelationship represents trust relationship as a 6-tuple &lt;Trusted Agent, 
     *  Trusting Agent, Context, TrustCriteria, Score, Timestamp&gt;. Each trust relationship 
     *  is established between two agents who wants to engange in some form of communication 
     *  or cooperation within a certian context (e.g. buying or seeling goods, geting 
     *  some information, etc). An trusting agent trusts that trusted agent has certian 
     *  characteristics (or features), which are defined as a trust criteria by the 
     *  trusting agent. TrustRelationship has a level of trust, which can be numerical 
     *  value within a range e.g. [0..1].</p>
     */
    public static final Resource TrustRelationship = m_model.createResource( "http://www.nimble-project.org/ns/trust#TrustRelationship" );
    
    public static final Resource UnmeasurableTrustAttribute = m_model.createResource( "http://www.nimble-project.org/ns/trust#UnmeasurableTrustAttribute" );
    
    /** <p>Feedback from agents about other agents. E.g. user feedback about some particular 
     *  service. Feedback can be in a form of rating with predefined rating scale 
     *  (e.g. poor, fair, good, very good, excellent)</p>
     */
    public static final Resource UserRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#UserRating" );
    
    public static final Resource UserRatingCount = m_model.createResource( "http://www.nimble-project.org/ns/trust#UserRatingCount" );
    
    public static final Resource RatingScale = m_model.createResource( "http://www.nimble-project.org/ns/trust#RatingScale" );
    
    public static final Resource RelativeToMaxScale = m_model.createResource( "http://www.nimble-project.org/ns/trust#RelativeToMaxScale" );
    
    public static final Resource ReputationScale = m_model.createResource( "http://www.nimble-project.org/ns/trust#ReputationScale" );
    
    public static final Resource fiveStar = m_model.createResource( "http://www.nimble-project.org/ns/trust#fiveStar" );
    
    public static final Resource fourStar = m_model.createResource( "http://www.nimble-project.org/ns/trust#fourStar" );
    
    public static final Resource high = m_model.createResource( "http://www.nimble-project.org/ns/trust#high" );
    
    public static final Resource low = m_model.createResource( "http://www.nimble-project.org/ns/trust#low" );
    
    public static final Resource medium = m_model.createResource( "http://www.nimble-project.org/ns/trust#medium" );
    
    public static final Resource oneStar = m_model.createResource( "http://www.nimble-project.org/ns/trust#oneStar" );
    
    public static final Resource threeStar = m_model.createResource( "http://www.nimble-project.org/ns/trust#threeStar" );
    
    public static final Resource twoStar = m_model.createResource( "http://www.nimble-project.org/ns/trust#twoStar" );
    
    
    //NIMBLE metrics
    
    
    public static final Resource TrustScore = m_model.createResource( "http://www.nimble-project.org/ns/trust#TrustScore" );
    
    public static final Resource OverallCompanyRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#OverallCompanyRating" );
    
    public static final Resource OverallCommunicationRating  = m_model.createResource( "http://www.nimble-project.org/ns/trust#OverallCommunicationRating" );
    public static final Resource QualityOfNegotiationProcessRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#QualityOfNegotiationProcessRating");
    public static final Resource QualityOfOrderingProcessRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#QualityOfOrderingProcessRating");
    public static final Resource ResponseTimeRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#ResponseTimeRating");
    
    public static final Resource OverallFullfilmentOfTermsRating  = m_model.createResource( "http://www.nimble-project.org/ns/trust#OverallFullfilmentOfTermsRating" );
    public static final Resource ListingAccuracyRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#ListingAccuracyRating");
    public static final Resource ConformanceToContractualTerms = m_model.createResource( "http://www.nimble-project.org/ns/trust#ConformanceToContractualTerms");
    

    public static final Resource OverallDeliveryAndPackagingRating = m_model.createResource( "http://www.nimble-project.org/ns/trust#OverallDeliveryAndPackagingRating");
    
    public static final Resource NumberOfCompletedTransactions = m_model.createResource( "http://www.nimble-project.org/ns/trust#NumberOfCompletedTransactions" );
    public static final Resource NumberOfUncompletedTransactions = m_model.createResource( "http://www.nimble-project.org/ns/trust#NumberOfUncompletedTransactions" );
    
    public static final Resource TradingVolume = m_model.createResource( "http://www.nimble-project.org/ns/trust#TradingVolume" );
    
    public static final Resource OverallProfileCompletness = m_model.createResource( "http://www.nimble-project.org/ns/trust#OverallProfileCompletness" );
    public static final Resource ProfileCompletnessDetails = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProfileCompletnessDetails" );
    public static final Resource ProfileCompletnessDescription = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProfileCompletnessDescription" );
    public static final Resource ProfileCompletnessTrade = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProfileCompletnessTrade" );
    public static final Resource ProfileCompletnessCertificates = m_model.createResource( "http://www.nimble-project.org/ns/trust#ProfileCompletnessCertificates" );
    
    public static final Resource AverageTimeToRespond = m_model.createResource( "http://www.nimble-project.org/ns/trust#AverageTimeToRespond" );
    
    public static final Resource AverageNegotiationTime = m_model.createResource( "http://www.nimble-project.org/ns/trust#AverageNegotiationTime" );
   
    
}

