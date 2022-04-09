package com.esisman.fileupload.controller;

import com.esisman.fileupload.entity.File;
import com.esisman.fileupload.model.FileResponseModel;
import com.esisman.fileupload.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @Operation(summary = "Get list of files' information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved list of files' information successfully",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<List<FileResponseModel>> getFiles() {
        List<File> files = fileStorageService.listAllFiles();
        List<FileResponseModel> fileResponseModels = files.stream()
                .map(this::convertToFileResponseModel)
                .collect(Collectors.toList());

        return new ResponseEntity(fileResponseModels, HttpStatus.OK);
    }

    @Operation(summary = "Get file content as byte array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got file content as byte array",
                    content = @Content) })
    @GetMapping("/{fileName}/content")
    public ResponseEntity<byte[]> getFileContent(@PathVariable String fileName) {
        final byte[] fileContent = fileStorageService.getFileContent(fileName);

        return new ResponseEntity(fileContent, HttpStatus.OK);
    }

    @Operation(summary = "Upload file to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                    content = @Content) })
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(name = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        final String fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
        final File uploadedFile = fileStorageService.storeFile(file);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete file from server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File deleted successfully",
                    content = @Content) })
    @DeleteMapping("/{fileName}")
    public ResponseEntity deleteFile(@PathVariable String fileName) {
        fileStorageService.deleteFile(fileName);

        return new ResponseEntity(HttpStatus.OK);
    }

    private FileResponseModel convertToFileResponseModel(File file) {
        FileResponseModel model = new FileResponseModel();
        model.setName(file.getName());
        model.setPath(file.getPath());
        model.setSize(file.getSize());
        model.setExtension(file.getExtension());

        return model;
    }
}
