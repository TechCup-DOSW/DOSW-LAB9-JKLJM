package edu.eci.dosw.techcup_futbol.controller;

import edu.eci.dosw.techcup_futbol.document.ImageDocument;
import edu.eci.dosw.techcup_futbol.dtos.ApiErrorResponse;
import edu.eci.dosw.techcup_futbol.dtos.ImageMetadataDTO;
import edu.eci.dosw.techcup_futbol.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
@Tag(name = "Images", description = "Endpoints for uploading and managing images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Operation(summary = "Upload image", description = "Uploads an image and stores it in MongoDB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image uploaded successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ImageMetadataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid image file",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageMetadataDTO> uploadImage(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        ImageMetadataDTO uploadedImage = imageService.uploadImage(file);
        return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED);
    }

    @Operation(summary = "List images", description = "Returns metadata for all uploaded images.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image list retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ImageMetadataDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<ImageMetadataDTO>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @Operation(summary = "Get image by id", description = "Returns the raw image content for the provided id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found"),
            @ApiResponse(responseCode = "404", description = "Image not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(
            @Parameter(description = "Image identifier", required = true)
            @PathVariable String id) {
        ImageDocument imageDocument = imageService.getImageById(id);

        HttpHeaders headers = new HttpHeaders();
        String contentType = imageDocument.getContentType() == null
                ? MediaType.APPLICATION_OCTET_STREAM_VALUE
                : imageDocument.getContentType();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(ContentDisposition.inline().filename(imageDocument.getFileName()).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageDocument.getData());
    }

    @Operation(summary = "Delete image", description = "Deletes an image by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "Image identifier", required = true)
            @PathVariable String id) {
        imageService.deleteImageById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}