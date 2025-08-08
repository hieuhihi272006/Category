package com.javaweb.model.entity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="import_receipt")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ImportReceiptEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "note")
	private String note;
	
	@CreatedDate
	@Column(name = "created_at")
	private LocalDate createdAt;
	
	@OneToMany(mappedBy = "importReceipt" , fetch = FetchType.LAZY , cascade =  {CascadeType.PERSIST , CascadeType.MERGE } , orphanRemoval = true )
	private List<ImportDetailEntity> importDetails = new ArrayList<>();
}
