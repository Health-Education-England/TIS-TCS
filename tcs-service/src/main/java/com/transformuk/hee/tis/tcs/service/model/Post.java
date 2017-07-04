package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Post.
 */
@Entity
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nationalPostNumber")
	private String nationalPostNumber;

	@Column(name = "status")
	private String status;

	@Column(name = "postOwner")
	private String postOwner;

	@Column(name = "mainSiteLocated")
	private String mainSiteLocated;

	@Column(name = "leadSite")
	private String leadSite;

	@Column(name = "employingBody")
	private String employingBody;

	@Column(name = "trainingBody")
	private String trainingBody;

	@Column(name = "approvedGrade")
	private String approvedGrade;

	@Column(name = "postSpecialty")
	private String postSpecialty;

	@Column(name = "fullTimeEquivelent")
	private Float fullTimeEquivelent;

	@Column(name = "leadProvider")
	private String leadProvider;

	@Column(name = "oldPostId")
	private String oldPostId;

	@Column(name = "newPostId")
	private String newPostId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNationalPostNumber() {
		return nationalPostNumber;
	}

	public void setNationalPostNumber(String nationalPostNumber) {
		this.nationalPostNumber = nationalPostNumber;
	}

	public Post nationalPostNumber(String nationalPostNumber) {
		this.nationalPostNumber = nationalPostNumber;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Post status(String status) {
		this.status = status;
		return this;
	}

	public String getPostOwner() {
		return postOwner;
	}

	public void setPostOwner(String postOwner) {
		this.postOwner = postOwner;
	}

	public Post postOwner(String postOwner) {
		this.postOwner = postOwner;
		return this;
	}

	public String getMainSiteLocated() {
		return mainSiteLocated;
	}

	public void setMainSiteLocated(String mainSiteLocated) {
		this.mainSiteLocated = mainSiteLocated;
	}

	public Post mainSiteLocated(String mainSiteLocated) {
		this.mainSiteLocated = mainSiteLocated;
		return this;
	}

	public String getLeadSite() {
		return leadSite;
	}

	public void setLeadSite(String leadSite) {
		this.leadSite = leadSite;
	}

	public Post leadSite(String leadSite) {
		this.leadSite = leadSite;
		return this;
	}

	public String getEmployingBody() {
		return employingBody;
	}

	public void setEmployingBody(String employingBody) {
		this.employingBody = employingBody;
	}

	public Post employingBody(String employingBody) {
		this.employingBody = employingBody;
		return this;
	}

	public String getTrainingBody() {
		return trainingBody;
	}

	public void setTrainingBody(String trainingBody) {
		this.trainingBody = trainingBody;
	}

	public Post trainingBody(String trainingBody) {
		this.trainingBody = trainingBody;
		return this;
	}

	public String getApprovedGrade() {
		return approvedGrade;
	}

	public void setApprovedGrade(String approvedGrade) {
		this.approvedGrade = approvedGrade;
	}

	public Post approvedGrade(String approvedGrade) {
		this.approvedGrade = approvedGrade;
		return this;
	}

	public String getPostSpecialty() {
		return postSpecialty;
	}

	public void setPostSpecialty(String postSpecialty) {
		this.postSpecialty = postSpecialty;
	}

	public Post postSpecialty(String postSpecialty) {
		this.postSpecialty = postSpecialty;
		return this;
	}

	public Float getFullTimeEquivelent() {
		return fullTimeEquivelent;
	}

	public void setFullTimeEquivelent(Float fullTimeEquivelent) {
		this.fullTimeEquivelent = fullTimeEquivelent;
	}

	public Post fullTimeEquivelent(Float fullTimeEquivelent) {
		this.fullTimeEquivelent = fullTimeEquivelent;
		return this;
	}

	public String getLeadProvider() {
		return leadProvider;
	}

	public void setLeadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
	}

	public Post leadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
		return this;
	}

	public String getOldPostId() {
		return oldPostId;
	}

	public void setOldPostId(String oldPostId) {
		this.oldPostId = oldPostId;
	}

	public String getNewPostId() {
		return newPostId;
	}

	public void setNewPostId(String newPostId) {
		this.newPostId = newPostId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Post post = (Post) o;
		if (post.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, post.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Post{" +
				"id=" + id +
				", nationalPostNumber='" + nationalPostNumber + "'" +
				", status='" + status + "'" +
				", postOwner='" + postOwner + "'" +
				", mainSiteLocated='" + mainSiteLocated + "'" +
				", leadSite='" + leadSite + "'" +
				", employingBody='" + employingBody + "'" +
				", trainingBody='" + trainingBody + "'" +
				", approvedGrade='" + approvedGrade + "'" +
				", postSpecialty='" + postSpecialty + "'" +
				", fullTimeEquivelent='" + fullTimeEquivelent + "'" +
				", leadProvider='" + leadProvider + "'" +
				'}';
	}
}
