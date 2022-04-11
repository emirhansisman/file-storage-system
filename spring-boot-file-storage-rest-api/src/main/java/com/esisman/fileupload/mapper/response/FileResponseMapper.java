package com.esisman.fileupload.mapper.response;

import com.esisman.fileupload.entity.File;
import com.esisman.fileupload.model.FileResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileResponseMapper {
    public List<FileResponseModel> toFileResponseModel(List<File> files) {
        return files.stream()
                .map(this::toFileResponseModel)
                .collect(Collectors.toList());
    }

    public FileResponseModel toFileResponseModel(File file) {
        return FileResponseModel.builder()
                .name(file.getName())
                .extension(file.getExtension())
                .path(file.getPath())
                .size(file.getSize())
                .build();
    }
}
