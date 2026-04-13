package edu.eci.dosw.techcup_futbol.ApiTests;

import edu.eci.dosw.techcup_futbol.controller.ImageController;
import edu.eci.dosw.techcup_futbol.document.ImageDocument;
import edu.eci.dosw.techcup_futbol.dtos.ImageMetadataDTO;
import edu.eci.dosw.techcup_futbol.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @Test
    void shouldUploadImageAndReturnCreatedStatus() {
        MockMultipartFile file = new MockMultipartFile("file", "pic.png", "image/png", new byte[] { 1, 2 });
        ImageMetadataDTO metadata = new ImageMetadataDTO("id-1", "pic.png", "image/png", 2, Instant.now());
        when(imageService.uploadImage(file)).thenReturn(metadata);

        ResponseEntity<ImageMetadataDTO> response = imageController.uploadImage(file);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("id-1", response.getBody().id());
    }

    @Test
    void shouldReturnAllImageMetadata() {
        ImageMetadataDTO item = new ImageMetadataDTO("id-2", "banner.jpg", "image/jpeg", 10, Instant.now());
        when(imageService.getAllImages()).thenReturn(List.of(item));

        ResponseEntity<List<ImageMetadataDTO>> response = imageController.getAllImages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("banner.jpg", response.getBody().get(0).fileName());
    }

    @Test
    void shouldReturnImageWithOriginalContentTypeWhenPresent() {
        ImageDocument image = new ImageDocument("x.webp", "image/webp", new byte[] { 5, 6 }, Instant.now());
        when(imageService.getImageById("img-1")).thenReturn(image);

        ResponseEntity<byte[]> response = imageController.getImageById("img-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("image/webp"), response.getHeaders().getContentType());
        assertArrayEquals(new byte[] { 5, 6 }, response.getBody());
    }

    @Test
    void shouldFallbackToOctetStreamWhenContentTypeIsNull() {
        ImageDocument image = new ImageDocument("x.bin", null, new byte[] { 7 }, Instant.now());
        when(imageService.getImageById("img-2")).thenReturn(image);

        ResponseEntity<byte[]> response = imageController.getImageById("img-2");

        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals("inline; filename=\"x.bin\"", response.getHeaders().getFirst("Content-Disposition"));
    }

    @Test
    void shouldDeleteImageAndReturnNoContent() {
        ResponseEntity<Void> response = imageController.deleteImage("img-3");

        verify(imageService).deleteImageById("img-3");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
