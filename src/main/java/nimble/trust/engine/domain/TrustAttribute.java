package nimble.trust.engine.domain;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TrustAttributes")
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrustAttribute extends BaseEntity{

	
	@Id
	@Getter
	@Setter
	@GeneratedValue
	private Long id;

	private double importance = 1;
	
	private String value;
	
	private String minValue;
	
	private String maxValue;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private TrustAttributeType trustAttributeType;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
//	@JoinColumn(name="trustProfile", referencedColumnName="id" )
    private TrustProfile trustProfile;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
//	@JoinColumn(name="trustPolicy", referencedColumnName="id" )
    private TrustPolicy trustPolicy;

	
		

}
