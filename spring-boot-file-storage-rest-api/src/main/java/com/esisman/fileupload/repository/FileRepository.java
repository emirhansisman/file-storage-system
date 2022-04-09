package com.esisman.fileupload.repository;

import com.esisman.fileupload.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findFileByName(String fileName);
    void deleteFileByName(String fileName);
    boolean existsByName(String fileName);
}