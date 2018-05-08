package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PlacementDetailsDTO implements Serializable {
    @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    @Null(groups = Create.class, message = "Id must be null when creating a new placement")
    private Long id;

    private String intrepidId;

    @NotNull(message = "TraineeId is required", groups = {Update.class, Create.class})
    private Long traineeId;

    private String traineeFirstName;

    private String traineeLastName;

    private String traineeGmcNumber;

    private String nationalPostNumber;

    @NotNull(message = "Date from is required", groups = {Update.class, Create.class})
    private LocalDate dateFrom;

    @NotNull(message = "Date to is required", groups = {Update.class, Create.class})
    private LocalDate dateTo;

    private Double wholeTimeEquivalent;

    @ApiModelProperty("The site code")
    private String siteCode;

    @ApiModelProperty("The id of the site from the reference service")
    @NotNull(message = "SiteId is required", groups = {Update.class, Create.class})
    private Long siteId;

    private String siteName;

    @ApiModelProperty("The Grade Abbr")
    private String gradeAbbreviation;

    @ApiModelProperty("The Grade id from Reference service")
    @NotNull(message = "GradeId is required", groups = {Update.class, Create.class})
    private Long gradeId;

    private String gradeName;

    //TODO to add clinical supervisor and specialties

    @NotNull(message = "PlacementType is required", groups = {Update.class, Create.class})
    private String placementType;

    private String owner;

    private String trainingDescription;

    private PlacementStatus status;

    @NotNull(message = "PostId is required", groups = {Update.class, Create.class})
    private Long postId;

    private String localPostNumber;

    private Set<PlacementSpecialtyDTO> specialties;

    private Set<PlacementSupervisorDTO> supervisors = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIntrepidId() {
        return intrepidId;
    }

    public void setIntrepidId(final String intrepidId) {
        this.intrepidId = intrepidId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(final Long traineeId) {
        this.traineeId = traineeId;
    }

    public String getTraineeFirstName() {
        return traineeFirstName;
    }

    public void setTraineeFirstName(final String traineeFirstName) {
        this.traineeFirstName = traineeFirstName;
    }

    public String getTraineeLastName() {
        return traineeLastName;
    }

    public void setTraineeLastName(final String traineeLastName) {
        this.traineeLastName = traineeLastName;
    }

    public String getTraineeGmcNumber() {
        return traineeGmcNumber;
    }

    public void setTraineeGmcNumber(final String traineeGmcNumber) {
        this.traineeGmcNumber = traineeGmcNumber;
    }

    public String getNationalPostNumber() {
        return nationalPostNumber;
    }

    public void setNationalPostNumber(final String nationalPostNumber) {
        this.nationalPostNumber = nationalPostNumber;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(final LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(final LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Double getWholeTimeEquivalent() {
        return wholeTimeEquivalent;
    }

    public void setWholeTimeEquivalent(final Double wholeTimeEquivalent) {
        this.wholeTimeEquivalent = wholeTimeEquivalent;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(final String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(final String siteName) {
        this.siteName = siteName;
    }

    public String getGradeAbbreviation() {
        return gradeAbbreviation;
    }

    public void setGradeAbbreviation(final String gradeAbbreviation) {
        this.gradeAbbreviation = gradeAbbreviation;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(final String gradeName) {
        this.gradeName = gradeName;
    }

    public String getPlacementType() {
        return placementType;
    }

    public void setPlacementType(final String placementType) {
        this.placementType = placementType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(final String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public PlacementStatus getStatus() {
        return status;
    }

    public void setStatus(final PlacementStatus status) {
        this.status = status;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(final Long postId) {
        this.postId = postId;
    }

    public String getLocalPostNumber() {
        return localPostNumber;
    }

    public void setLocalPostNumber(final String localPostNumber) {
        this.localPostNumber = localPostNumber;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(final Long siteId) {
        this.siteId = siteId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(final Long gradeId) {
        this.gradeId = gradeId;
    }

    public Set<PlacementSpecialtyDTO> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(final Set<PlacementSpecialtyDTO> specialties) {
        this.specialties = specialties;
    }

    public Set<PlacementSupervisorDTO> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(final Set<PlacementSupervisorDTO> supervisors) {
        this.supervisors = supervisors;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PlacementDetailsDTO that = (PlacementDetailsDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) return false;
        if (traineeId != null ? !traineeId.equals(that.traineeId) : that.traineeId != null) return false;
        if (traineeFirstName != null ? !traineeFirstName.equals(that.traineeFirstName) : that.traineeFirstName != null)
            return false;
        if (traineeLastName != null ? !traineeLastName.equals(that.traineeLastName) : that.traineeLastName != null)
            return false;
        if (traineeGmcNumber != null ? !traineeGmcNumber.equals(that.traineeGmcNumber) : that.traineeGmcNumber != null)
            return false;
        if (nationalPostNumber != null ? !nationalPostNumber.equals(that.nationalPostNumber) : that.nationalPostNumber != null)
            return false;
        if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) return false;
        if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;
        if (wholeTimeEquivalent != null ? !wholeTimeEquivalent.equals(that.wholeTimeEquivalent) : that.wholeTimeEquivalent != null)
            return false;
        if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
        if (siteName != null ? !siteName.equals(that.siteName) : that.siteName != null) return false;
        if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
            return false;
        if (gradeName != null ? !gradeName.equals(that.gradeName) : that.gradeName != null) return false;
        if (placementType != null ? !placementType.equals(that.placementType) : that.placementType != null)
            return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (trainingDescription != null ? !trainingDescription.equals(that.trainingDescription) : that.trainingDescription != null)
            return false;
        if (status != that.status) return false;
        if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
        return localPostNumber != null ? localPostNumber.equals(that.localPostNumber) : that.localPostNumber == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
        result = 31 * result + (traineeId != null ? traineeId.hashCode() : 0);
        result = 31 * result + (traineeFirstName != null ? traineeFirstName.hashCode() : 0);
        result = 31 * result + (traineeLastName != null ? traineeLastName.hashCode() : 0);
        result = 31 * result + (traineeGmcNumber != null ? traineeGmcNumber.hashCode() : 0);
        result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
        result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
        result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
        result = 31 * result + (wholeTimeEquivalent != null ? wholeTimeEquivalent.hashCode() : 0);
        result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
        result = 31 * result + (siteName != null ? siteName.hashCode() : 0);
        result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
        result = 31 * result + (gradeName != null ? gradeName.hashCode() : 0);
        result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (trainingDescription != null ? trainingDescription.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        result = 31 * result + (localPostNumber != null ? localPostNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlacementDetailsDTO{" +
                "id=" + id +
                ", intrepidId='" + intrepidId + '\'' +
                ", traineeId=" + traineeId +
                ", traineeFirstName='" + traineeFirstName + '\'' +
                ", traineeLastName='" + traineeLastName + '\'' +
                ", traineeGmcNumber='" + traineeGmcNumber + '\'' +
                ", nationalPostNumber='" + nationalPostNumber + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", wholeTimeEquivalent=" + wholeTimeEquivalent +
                ", siteCode='" + siteCode + '\'' +
                ", siteName='" + siteName + '\'' +
                ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
                ", gradeName='" + gradeName + '\'' +
                ", placementType='" + placementType + '\'' +
                ", owner='" + owner + '\'' +
                ", trainingDescription='" + trainingDescription + '\'' +
                ", status=" + status +
                ", postId=" + postId +
                ", localPostNumber='" + localPostNumber + '\'' +
                '}';
    }
}
