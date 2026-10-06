package com.example.studyagent.repository;

import com.example.studyagent.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    List<DocumentChunk> findByPageNumber(int pageNumber);

    @Query(value = """
        SELECT id, content, embedding, page_number,
               embedding <=> CAST(:embedding AS vector) AS distance
        FROM document_chunks
        ORDER BY distance
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findSimilarChunks(
            @Param("embedding") String embedding,
            @Param("limit") int limit);
}