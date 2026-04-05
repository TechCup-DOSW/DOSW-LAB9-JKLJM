package edu.eci.dosw.techcup_futbol.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB document for image storage.
 */
@Document(collection = "images")
public class ImageDocument {

    @Id
    private String id;

    private String fileName;

    private String contentType;

    private byte[] data;

    private Instant uploadedAt;

    public ImageDocument() {
    }

    public ImageDocument(String fileName, String contentType, byte[] data, Instant uploadedAt) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.uploadedAt = uploadedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
