package com.esisman.fileupload.service;

import com.esisman.fileupload.entity.File;
import com.esisman.fileupload.exception.FileConflictException;
import com.esisman.fileupload.exception.FileSizeLimitExceedException;
import com.esisman.fileupload.exception.FileTypeNotSupportedException;
import com.esisman.fileupload.exception.NoContentException;
import com.esisman.fileupload.mapper.response.FileResponseMapper;
import com.esisman.fileupload.model.FileResponseModel;
import com.esisman.fileupload.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileRepository fileRepository;
    private final FileResponseMapper fileResponseMapper;

    public File storeFile(MultipartFile uploadedFile) {

        if (!isSupportedContentType(uploadedFile.getContentType())) {
            throw new FileTypeNotSupportedException("File type is not supported.");
        }

        if (uploadedFile.getSize() > 5242880) {
            throw new FileSizeLimitExceedException("Maximum file size exceeded.");
        }

        boolean fileIsPresent = fileRepository.existsByName(uploadedFile.getOriginalFilename());

        if (fileIsPresent) {
            throw new FileConflictException("File with " + uploadedFile.getOriginalFilename() + " already exists.");
        }

        File file = File.builder()
                .name(uploadedFile.getOriginalFilename())
                .path("file-path")
                .size(uploadedFile.getSize())
                .content(getFileContent(uploadedFile))
                .extension(uploadedFile.getContentType())
                .build();

        return fileRepository.save(file);
    }

    public byte[] getFileContent(String fileName) {
        return fileRepository.findFileByName(fileName).getContent();
    }

    public List<FileResponseModel> listAllFiles() {
        List<File> files = fileRepository.findAll();
        return fileResponseMapper.toFileResponseModel(files);
    }

    @Transactional
    public void deleteFile(String fileName) {
        fileRepository.deleteFileByName(fileName);
    }

    private byte[] getFileContent(MultipartFile file) {
        byte[] fileContent = new byte[0];

        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            throw new NoContentException("File doesn't have content");
        }

        return fileContent;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
                || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || contentType.equals("application/pdf")
                || contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
