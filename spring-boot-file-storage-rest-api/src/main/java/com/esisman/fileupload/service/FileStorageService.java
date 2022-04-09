package com.esisman.fileupload.service;

import com.esisman.fileupload.entity.File;
import com.esisman.fileupload.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileRepository fileRepository;

    public File storeFile(MultipartFile uploadedFile) {

        if (!isSupportedContentType(uploadedFile.getContentType())) {
            throw new RuntimeException("Content type is not valid");
        }

        if (uploadedFile.getSize() > 5242880) {
            throw new RuntimeException("File exceeded size.");
        }

        File file = File.builder()
                .name(uploadedFile.getOriginalFilename())
                .path("file-path")
                .size(uploadedFile.getSize())
                .content(getFileContent(uploadedFile))
                .extension(uploadedFile.getContentType())
                .build();

        boolean fileIsPresent = fileRepository.existsByName(file.getName());

        if (fileIsPresent) {
            try {
                throw new FileUploadException("File with " + file.getName() + " already exists.");
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }

        return fileRepository.save(file);
    }

    public byte[] getFileContent(String fileName) {
        return fileRepository.findFileByName(fileName).getContent();
    }

    public List<File> listAllFiles() {
        return fileRepository.findAll();
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
            e.printStackTrace();
        }
        return fileContent;
    }

    public boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
                || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || contentType.equals("application/pdf")
                || contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
