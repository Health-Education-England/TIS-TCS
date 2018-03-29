package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@ApiModel("Document")
@Entity
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "addedDate", updatable = false, insertable = false)
    private LocalDateTime addedDate;
    @Version
    private LocalDateTime amendedDate;
    private LocalDateTime inactiveDate;
    private String uploadedBy;
    private String name;
    private String fileName;
    private String fileExtension;
    private String contentType;
    private Long size;
    private Long personId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer version;
    @Transient
    private byte[] bytes;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "DocumentTag",
            joinColumns = @JoinColumn(name = "documentId"),
            inverseJoinColumns = @JoinColumn(name = "tagId")
    )
    private Set<Tag> tags;

    /**
     * @deprecated TODO: To be removed after importing documents from Hicom
     */
    @Deprecated
    private String intrepidDocumentUId;
    /**
     * @deprecated TODO: To be removed after importing documents from Hicom
     */
    @Deprecated
    private String intrepidParentRecordId;
    /**
     * @deprecated TODO: To be removed after importing documents from Hicom
     */
    @Deprecated
    private String intrepidFolderPath;

    public void addTag(final Tag tag) {
        tags.add(tag);
        tag.getDocuments().add(this);
    }

    public void removeTag(final Tag tag) {
        tags.remove(tag);
        tag.getDocuments().remove(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Document document = (Document) o;
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", addedDate=" + addedDate +
                ", amendedDate=" + amendedDate +
                ", inactiveDate=" + inactiveDate +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", personId=" + personId +
                ", status=" + status +
                ", version=" + version +
                ", tags=" + tags +
                ", intrepidDocumentUId='" + intrepidDocumentUId + '\'' +
                ", intrepidParentRecordId='" + intrepidParentRecordId + '\'' +
                ", intrepidFolderPath='" + intrepidFolderPath + '\'' +
                '}';
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(final Set<Tag> tags) {
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
