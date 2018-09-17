package nimble.trust.engine.domain;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdDatetime", "updatedDatetime"},
        allowGetters = true
)
@Data
public abstract class BaseEntity{
	

	@Version
	@Getter
	@Setter
	private Long version;

	
	@Getter
	@Setter
	@CreatedDate
	private Date createdDatetime;

	
	@Getter
	@Setter
	@LastModifiedDate
	private Date updatedDatetime;
	
	@Getter
	@Setter
	@CreatedBy
	private String createdBy;
	
	@Getter
	@Setter
	@LastModifiedBy
	private String lastUpdatedBy;
	
	@Getter
	@Setter
	private String descr;
	
		
	public boolean isNew() {
		return getId()==null;
	}
	
	public abstract Long getId();
}
	
