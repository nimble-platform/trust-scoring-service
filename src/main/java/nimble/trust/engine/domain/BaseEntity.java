package nimble.trust.engine.domain;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@MappedSuperclass
public abstract class BaseEntity{
	

//	@Version
	@Getter
	@Setter
	@JsonIgnore
	private Long version;

	
	@Getter
	@Setter
	@CreatedDate
	@JsonIgnore
	private Date createdDatetime;

	
	@Getter
	@Setter
	@LastModifiedDate
	@JsonIgnore
	private Date updatedDatetime;
	
	@Getter
	@Setter
	@CreatedBy
	@JsonIgnore
	private String createdBy;
	
	@Getter
	@Setter
	@LastModifiedBy
	@JsonIgnore
	private String lastUpdatedBy;
	
	@Getter
	@Setter
	private String descr;
	
		
	public boolean isNew() {
		return getId()==null;
	}
	
	public abstract Long getId();
}
	
