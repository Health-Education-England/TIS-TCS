package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Post entity.
 */
public class PostDTO implements Serializable {

	private Long id;

	private String nationalPostNumber;

	private String status;

	private String postOwner;

	private String mainSiteLocated;

	private String leadSite;

	private String employingBody;

	private String trainingBody;

	private String approvedGrade;

	private String postSpecialty;

	private Float fullTimeEquivelent;

	private String leadProvider;

	private Long oldPostId;

	private Long newPostId;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPostOwner() {
		return postOwner;
	}

	public void setPostOwner(String postOwner) {
		this.postOwner = postOwner;
	}

	public String getMainSiteLocated() {
		return mainSiteLocated;
	}

	public void setMainSiteLocated(String mainSiteLocated) {
		this.mainSiteLocated = mainSiteLocated;
	}

	public String getLeadSite() {
		return leadSite;
	}

	public void setLeadSite(String leadSite) {
		this.leadSite = leadSite;
	}

	public String getEmployingBody() {
		return employingBody;
	}

	public void setEmployingBody(String employingBody) {
		this.employingBody = employingBody;
	}

	public String getTrainingBody() {
		return trainingBody;
	}

	public void setTrainingBody(String trainingBody) {
		this.trainingBody = trainingBody;
	}

	public String getApprovedGrade() {
		return approvedGrade;
	}

	public void setApprovedGrade(String approvedGrade) {
		this.approvedGrade = approvedGrade;
	}

	public String getPostSpecialty() {
		return postSpecialty;
	}

	public void setPostSpecialty(String postSpecialty) {
		this.postSpecialty = postSpecialty;
	}

	public Float getFullTimeEquivelent() {
		return fullTimeEquivelent;
	}

	public void setFullTimeEquivelent(Float fullTimeEquivelent) {
		this.fullTimeEquivelent = fullTimeEquivelent;
	}

	public String getLeadProvider() {
		return leadProvider;
	}

	public void setLeadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
	}

	public Long getOldPostId() {
		return oldPostId;
	}

	public void setOldPostId(Long postId) {
		this.oldPostId = postId;
	}

	public Long getNewPostId() {
		return newPostId;
	}

	public void setNewPostId(Long postId) {
		this.newPostId = postId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PostDTO postDTO = (PostDTO) o;

		if (!Objects.equals(id, postDTO.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "PostDTO{" +
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
