package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment implements Serializable {
	private static final long serialVersionUID = 4670393251358554050L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long threadId;
	private Long parentId;
	private String author;
	private String body;

	@Column(name = "addedDate", updatable = false, insertable = false)
	private LocalDateTime addedDate;
	@Version
	private LocalDateTime amendedDate;
	private LocalDateTime inactiveDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "placementId")
	private PlacementDetails placement;
	@Enumerated(value = EnumType.STRING)
	@Column(name = "source")
	private CommentSource source;
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Comment comment = (Comment) o;
		return Objects.equals(id, comment.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public LocalDateTime getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(LocalDateTime addedDate) {
		this.addedDate = addedDate;
	}

	public LocalDateTime getAmendedDate() {
		return amendedDate;
	}

	public void setAmendedDate(LocalDateTime amendedDate) {
		this.amendedDate = amendedDate;
	}

	public LocalDateTime getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(LocalDateTime inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public PlacementDetails getPlacement() {
		return placement;
	}

	public void setPlacement(PlacementDetails placement) {
		this.placement = placement;
	}

	public CommentSource getSource() { return source; }

	public void setSource(CommentSource source) { this.source = source; }
}
