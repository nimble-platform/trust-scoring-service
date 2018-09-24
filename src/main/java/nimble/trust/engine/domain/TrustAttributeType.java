package nimble.trust.engine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TrustAttributeTypes")
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrustAttributeType extends BaseEntity{
	
	@Id
	@Getter
	@Setter
	@GeneratedValue
	private Long id;
	
	
	@Column(name = "name")
	@Getter
	@Setter
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private TrustAttributeType parentType;

	@OneToMany(mappedBy = "parentType", fetch = FetchType.LAZY)
	private List<TrustAttributeType> subTypes;
	
		
	@Override
	public String toString() {
		return "TrustAttributeType [id=" + getId() + ", name=" + name + "]";
	}
	

}
