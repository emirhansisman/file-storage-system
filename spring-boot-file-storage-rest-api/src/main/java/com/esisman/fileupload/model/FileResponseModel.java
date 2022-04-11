package com.esisman.fileupload.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseModel {
    private String name;
    private String path;
    private Long size;
    private String extension;
}