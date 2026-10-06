package com.example.studyagent.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Column(columnDefinition = "vector(3072)")
    private float[] embedding;

    private int pageNumber;

    public DocumentChunk() {
    }

    public DocumentChunk(String content, int pageNumber) {
        this.content = content;
        this.pageNumber = pageNumber;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}