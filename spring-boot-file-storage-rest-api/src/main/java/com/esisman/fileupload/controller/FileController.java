package com.esisman.fileupload.controller;

import com.esisman.fileupload.model.FileResponseModel;
import com.esisman.fileupload.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            @ApiResponse(responseCode = "200", description = "File content returned as byte array",
                    content = @Content),
            @ApiResponse(responseCode = "204", description = "There is no file content",
                    content = @Content) })
    @GetMapping("/{fileName}/content")
    public byte[] getFileContent(@PathVariable String fileName) {
        return fileStorageService.getFileContent(fileName);
    }

    @Operation(summary = "Upload file to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "File type is not supported",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Maximum file upload size exceeded",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "File already exists",
                    content = @Content)
    })
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
