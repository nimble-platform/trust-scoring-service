package nimble.trust.engine.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TrustProfiles")
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrustProfile extends BaseEntity{
	
		
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Agent owner;
	
	@OneToMany(mappedBy = "trustProfile", 
			fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TrustAttribute> trustAttributes = Lists.newArrayList();
	
	@Override
	public String toString() {
		return "TrustProfile [id=" + getId() + ", owner=" + getOwner() + "]";
	}
	
	@Transient
	public TrustAttribute findAttribute(String attribueTypeName){
		List<TrustAttribute> trustAttributes =  getTrustAttributes();
		if (CollectionUtils.isEmpty(trustAttributes) == false){
			for (TrustAttribute trustAttribute : trustAttributes) {
				if (trustAttribute.getTrustAttributeType().getName().equals(attribueTypeName)){
					return trustAttribute;
				}
			}
		}
		return null;
	}
	

}
