package com.esisman.fileupload.model;

import lombok.Data;

@Data
public class FileResponseModel {
    private String name;
    private String path;
    private Long size;
    private String extension;
}