package edu.eci.dosw.techcup_futbol.service;

import edu.eci.dosw.techcup_futbol.document.ImageDocument;
import edu.eci.dosw.techcup_futbol.dtos.ImageMetadataDTO;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.repository.ImageDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ImageService {

    @Autowired
    private ImageDocumentRepository imageDocumentRepository;

    public ImageMetadataDTO uploadImage(MultipartFile file) {
        validateImage(file);

        try {
            ImageDocument imageDocument = new ImageDocument(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    Instant.now()
            );

            ImageDocument savedImage = imageDocumentRepository.save(imageDocument);
            return toMetadata(savedImage);
        } catch (IOException exception) {
            throw new TechCupException("Could not read image content from uploaded file.");
        }
    }

    public ImageDocument getImageById(String id) {
        return imageDocumentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No image found with id: " + id));
    }

    public List<ImageMetadataDTO> getAllImages() {
        return imageDocumentRepository.findAll().stream()
                .sorted(Comparator.comparing(ImageDocument::getUploadedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(this::toMetadata)
                .toList();
    }

    public void deleteImageById(String id) {
        if (!imageDocumentRepository.existsById(id)) {
            throw new NoSuchElementException("Cannot delete image. Image does not exist with id: " + id);
        }
        imageDocumentRepository.deleteById(id);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new TechCupException("Image file is required.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new TechCupException("Only image files are allowed.");
        }
    }

    private ImageMetadataDTO toMetadata(ImageDocument imageDocument) {
        int size = imageDocument.getData() == null ? 0 : imageDocument.getData().length;
        return new ImageMetadataDTO(
                imageDocument.getId(),
                imageDocument.getFileName(),
                imageDocument.getContentType(),
                size,
                imageDocument.getUploadedAt()
        );
    }
}