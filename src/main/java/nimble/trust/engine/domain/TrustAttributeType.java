package nimble.trust.engine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	
		
	@Override
	public String toString() {
		return "TrustAttributeType [id=" + getId() + ", name=" + name + "]";
	}
	

}
