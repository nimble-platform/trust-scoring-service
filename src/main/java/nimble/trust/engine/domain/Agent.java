package nimble.trust.engine.domain;

import java.math.BigDecimal;

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
@Table(name = "Agents")
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Agent extends BaseEntity{	
	
	@Id
	@Getter
	@Setter
	@GeneratedValue
	private Long id;
	
	@Column(name = "name")
	@Getter
	@Setter
	private String name;
	
	
	@Getter
	@Setter
	@Column(name = "altId")
	private String altId;
	
	@Getter
	@Setter
	@Column(name = "trustScore", precision=12, scale=6)
	private BigDecimal trustScore;
	
	
	@Override
	public String toString() {
		return "Agent [id=" + getId() + ", name=" + name + "]";
	}
	

}
