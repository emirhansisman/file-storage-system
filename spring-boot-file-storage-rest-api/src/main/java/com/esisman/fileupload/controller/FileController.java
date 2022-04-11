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
    public List<FileResponseModel> getFiles() {
        return fileStorageService.listAllFiles();
    }

    @Operation(summary = "Get file content as byte array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got file content as byte array",
                    content = @Content) })
    @GetMapping("/{fileName}/content")
    public byte[] getFileContent(@PathVariable String fileName) {
        return fileStorageService.getFileContent(fileName);
    }

    @Operation(summary = "Upload file to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                    content = @Content) })
    @PostMapping("/upload")
    public void uploadFile(@RequestParam(name = "file") MultipartFile file) {
        fileStorageService.storeFile(file);
    }

    @Operation(summary = "Delete file from server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File deleted successfully",
                    content = @Content) })
    @DeleteMapping("/{fileName}")
    public void deleteFile(@PathVariable String fileName) {
        fileStorageService.deleteFile(fileName);
    }
}
