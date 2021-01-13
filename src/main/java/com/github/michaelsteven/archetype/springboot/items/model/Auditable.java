package com.github.michaelsteven.archetype.springboot.items.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * Instantiates a new auditable.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	/** The created by. */
	@CreatedBy
	private String createdBy;
	
	/** The created timestamp. */
	@CreatedDate
	@Column(name = "createdTs")
	private Instant createdTimestamp;
	
	/** The updated by. */
	@LastModifiedBy
	private String updatedBy;
	
	/** The updated timestamp. */
	@LastModifiedDate
	@Column(name="updatedTs")
	private Instant updatedTimestamp;
	
}
