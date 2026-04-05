package edu.eci.dosw.techcup_futbol.repository;

import edu.eci.dosw.techcup_futbol.document.ImageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDocumentRepository extends MongoRepository<ImageDocument, String> {
}
