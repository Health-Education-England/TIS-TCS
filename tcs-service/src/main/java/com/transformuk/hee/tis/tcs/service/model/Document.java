package com.transformuk.hee.tis.tcs.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@ApiModel("Document")
@Entity
public class Document implements Serializable {
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Column(name = "addedDate", updatable = false, insertable = false)
    private LocalDateTime addedDate;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Version
    private LocalDateTime amendedDate;
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
    @Transient
    private byte[] bytes;
    @ManyToMany
    @JoinTable(
            name = "documenttag",
            joinColumns = @JoinColumn(name = "documentId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "id"))

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Collection<Tag> tags;

    /**
     * @deprecated TODO: To be removed after importing documents from Hicom
     */
    @ApiModelProperty(hidden = true)
    @Deprecated
    private String intrepidDocumentUId;
    /**
     * @deprecated TODO: To be removed after importing documents from Hicom
     */
    @ApiModelProperty(hidden = true)
    @Deprecated
    private String intrepidParentRecordId;
    /**
     * @deprecated TODO: To be removed after importing documents from Hicom
     */
    @ApiModelProperty(hidden = true)
    @Deprecated
    private String intrepidFolderPath;

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

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(final Collection<Tag> tags) {
        this.tags = tags;
    }

    public String getIntrepidDocumentUId() {
        return intrepidDocumentUId;
    }

    public void setIntrepidDocumentUId(final String intrepidDocumentUId) {
        this.intrepidDocumentUId = intrepidDocumentUId;
    }

    public String getIntrepidParentRecordId() {
        return intrepidParentRecordId;
    }

    public void setIntrepidParentRecordId(final String intrepidParentRecordId) {
        this.intrepidParentRecordId = intrepidParentRecordId;
    }

    public String getIntrepidFolderPath() {
        return intrepidFolderPath;
    }

    public void setIntrepidFolderPath(final String intrepidFolderPath) {
        this.intrepidFolderPath = intrepidFolderPath;
    }
}
