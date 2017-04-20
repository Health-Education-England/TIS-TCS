package com.transformuk.hee.tis.domain;


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

	@OneToOne
	@JoinColumn(unique = true)
	private Post oldPost;

	@OneToOne
	@JoinColumn(unique = true)
	private Post newPost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNationalPostNumber() {
		return nationalPostNumber;
	}

	public Post nationalPostNumber(String nationalPostNumber) {
		this.nationalPostNumber = nationalPostNumber;
		return this;
	}

	public void setNationalPostNumber(String nationalPostNumber) {
		this.nationalPostNumber = nationalPostNumber;
	}

	public String getStatus() {
		return status;
	}

	public Post status(String status) {
		this.status = status;
		return this;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPostOwner() {
		return postOwner;
	}

	public Post postOwner(String postOwner) {
		this.postOwner = postOwner;
		return this;
	}

	public void setPostOwner(String postOwner) {
		this.postOwner = postOwner;
	}

	public String getMainSiteLocated() {
		return mainSiteLocated;
	}

	public Post mainSiteLocated(String mainSiteLocated) {
		this.mainSiteLocated = mainSiteLocated;
		return this;
	}

	public void setMainSiteLocated(String mainSiteLocated) {
		this.mainSiteLocated = mainSiteLocated;
	}

	public String getLeadSite() {
		return leadSite;
	}

	public Post leadSite(String leadSite) {
		this.leadSite = leadSite;
		return this;
	}

	public void setLeadSite(String leadSite) {
		this.leadSite = leadSite;
	}

	public String getEmployingBody() {
		return employingBody;
	}

	public Post employingBody(String employingBody) {
		this.employingBody = employingBody;
		return this;
	}

	public void setEmployingBody(String employingBody) {
		this.employingBody = employingBody;
	}

	public String getTrainingBody() {
		return trainingBody;
	}

	public Post trainingBody(String trainingBody) {
		this.trainingBody = trainingBody;
		return this;
	}

	public void setTrainingBody(String trainingBody) {
		this.trainingBody = trainingBody;
	}

	public String getApprovedGrade() {
		return approvedGrade;
	}

	public Post approvedGrade(String approvedGrade) {
		this.approvedGrade = approvedGrade;
		return this;
	}

	public void setApprovedGrade(String approvedGrade) {
		this.approvedGrade = approvedGrade;
	}

	public String getPostSpecialty() {
		return postSpecialty;
	}

	public Post postSpecialty(String postSpecialty) {
		this.postSpecialty = postSpecialty;
		return this;
	}

	public void setPostSpecialty(String postSpecialty) {
		this.postSpecialty = postSpecialty;
	}

	public Float getFullTimeEquivelent() {
		return fullTimeEquivelent;
	}

	public Post fullTimeEquivelent(Float fullTimeEquivelent) {
		this.fullTimeEquivelent = fullTimeEquivelent;
		return this;
	}

	public void setFullTimeEquivelent(Float fullTimeEquivelent) {
		this.fullTimeEquivelent = fullTimeEquivelent;
	}

	public String getLeadProvider() {
		return leadProvider;
	}

	public Post leadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
		return this;
	}

	public void setLeadProvider(String leadProvider) {
		this.leadProvider = leadProvider;
	}

	public Post getOldPost() {
		return oldPost;
	}

	public Post oldPost(Post post) {
		this.oldPost = post;
		return this;
	}

	public void setOldPost(Post post) {
		this.oldPost = post;
	}

	public Post getNewPost() {
		return newPost;
	}

	public Post newPost(Post post) {
		this.newPost = post;
		return this;
	}

	public void setNewPost(Post post) {
		this.newPost = post;
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
