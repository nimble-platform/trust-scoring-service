package nimble.trust.engine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NotificationLogs")
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationLog extends BaseEntity{	
	
	@Id
	@GeneratedValue
	private Long id;

	
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "content")
	private String content;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private Status status;
	
	
	@Override
	public String toString() {
		return "NotificationLog [id=" + getId() + ", type=" + type + "]";
	}
	

}
