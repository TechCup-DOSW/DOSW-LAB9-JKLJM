package edu.eci.dosw.techcup_futbol.ServiceTest;

import edu.eci.dosw.techcup_futbol.document.ImageDocument;
import edu.eci.dosw.techcup_futbol.dtos.ImageMetadataDTO;
import edu.eci.dosw.techcup_futbol.exceptions.TechCupException;
import edu.eci.dosw.techcup_futbol.repository.ImageDocumentRepository;
import edu.eci.dosw.techcup_futbol.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceUnitTest {

    @Mock
    private ImageDocumentRepository imageDocumentRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    void shouldUploadImageAndReturnMetadata() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.png",
                "image/png",
                new byte[] { 1, 2, 3, 4 }
        );

        when(imageDocumentRepository.save(any(ImageDocument.class))).thenAnswer(invocation -> {
            ImageDocument saved = invocation.getArgument(0);
            saved.setId("img-1");
            return saved;
        });

        ImageMetadataDTO metadata = imageService.uploadImage(file);

        assertEquals("img-1", metadata.id());
        assertEquals("photo.png", metadata.fileName());
        assertEquals("image/png", metadata.contentType());
        assertEquals(4, metadata.size());
        assertTrue(metadata.uploadedAt() != null);
        verify(imageDocumentRepository).save(any(ImageDocument.class));
    }

    @Test
    void shouldFailUploadWhenFileIsMissingOrNotImage() {
        assertThrows(TechCupException.class, () -> imageService.uploadImage(null));

        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);
        assertThrows(TechCupException.class, () -> imageService.uploadImage(emptyFile));

        MockMultipartFile textFile = new MockMultipartFile("file", "note.txt", "text/plain", "hello".getBytes());
        assertThrows(TechCupException.class, () -> imageService.uploadImage(textFile));
    }

    @Test
    void shouldWrapIOExceptionDuringUpload() throws IOException {
        MultipartFile faultyFile = mock(MultipartFile.class);
        when(faultyFile.isEmpty()).thenReturn(false);
        when(faultyFile.getContentType()).thenReturn("image/jpeg");
        when(faultyFile.getOriginalFilename()).thenReturn("faulty.jpg");
        when(faultyFile.getBytes()).thenThrow(new IOException("boom"));

        assertThrows(TechCupException.class, () -> imageService.uploadImage(faultyFile));
    }

    @Test
    void shouldReturnImageByIdWhenItExists() {
        ImageDocument imageDocument = new ImageDocument("x.png", "image/png", new byte[] { 1 }, Instant.now());
        when(imageDocumentRepository.findById("img-9")).thenReturn(Optional.of(imageDocument));

        ImageDocument found = imageService.getImageById("img-9");

        assertEquals("x.png", found.getFileName());
    }

    @Test
    void shouldThrowWhenImageIdDoesNotExist() {
        when(imageDocumentRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> imageService.getImageById("missing"));
    }

    @Test
    void shouldReturnAllImagesSortedByDateAndMapMetadata() {
        ImageDocument older = new ImageDocument("old.png", "image/png", new byte[] { 9, 9 }, Instant.parse("2026-01-01T00:00:00Z"));
        older.setId("old");

        ImageDocument newerWithoutBytes = new ImageDocument("new.png", "image/png", null, Instant.parse("2026-02-01T00:00:00Z"));
        newerWithoutBytes.setId("new");

        when(imageDocumentRepository.findAll()).thenReturn(List.of(older, newerWithoutBytes));

        List<ImageMetadataDTO> all = imageService.getAllImages();

        assertEquals(2, all.size());
        assertEquals("new", all.get(0).id());
        assertEquals(0, all.get(0).size());
        assertEquals("old", all.get(1).id());
        assertEquals(2, all.get(1).size());
    }

    @Test
    void shouldDeleteImageWhenItExists() {
        when(imageDocumentRepository.existsById("img-5")).thenReturn(true);

        imageService.deleteImageById("img-5");

        verify(imageDocumentRepository).deleteById("img-5");
    }

    @Test
    void shouldFailDeleteWhenImageDoesNotExist() {
        when(imageDocumentRepository.existsById("img-6")).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> imageService.deleteImageById("img-6"));
    }
}
