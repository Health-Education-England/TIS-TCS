package com.transformuk.hee.tis.tcs.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("Document")
public class DocumentDTO implements Serializable {
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @NotNull(groups = Update.class, message = "Id must not be null when updating a document")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    @Null(groups = Create.class, message = "Id must be null when creating a new document")
    private Long id;
    private LocalDateTime addedDate;
    private LocalDateTime amendedDate;
    private LocalDateTime inactiveDate;
    private String uploadedBy;
    private String name;
    private String fileName;
    private String fileExtension;
    private String contentType;
    private Long size;
    private Long personId;
    private String fileLocation;
    private String status;
    private Long version;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private byte[] bytes;
//    private Collection<TagDTO> tags;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(final LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public LocalDateTime getAmendedDate() {
        return amendedDate;
    }

    public void setAmendedDate(final LocalDateTime amendedDate) {
        this.amendedDate = amendedDate;
    }

    public LocalDateTime getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(final LocalDateTime inactiveDate) {
        this.inactiveDate = inactiveDate;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(final String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(final String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(final Long size) {
        this.size = size;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(final Long personId) {
        this.personId = personId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(final String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }
}
