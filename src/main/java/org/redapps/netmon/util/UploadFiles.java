package org.redapps.netmon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UploadFiles {

    private static final Logger logger = LoggerFactory.getLogger(UploadFiles.class);
    private static String upload_folder;

    /*
     * Read upload configuration from properties file.
    */
    @Value("${app.upload_folder}")
    public void setUpload_folder(String value) {
        upload_folder = value;
    }

    /**
     * Store files in directory.
     * @param files list of files
     * @return file name
     * @throws IOException the file exception
     */
    public static String saveUploadedFiles(List<MultipartFile> files) throws IOException {

        String fileName= "";
        upload_folder = System.getProperty("user.dir") +  upload_folder;
        createDir(upload_folder);
        logger.info("upload_folder: "+ upload_folder);
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                logger.info("empty");
                continue;
            }

            byte[] bytes = file.getBytes();
            fileName = file.getOriginalFilename();
            logger.info(file.getOriginalFilename());
            Path path = Paths.get(upload_folder + file.getOriginalFilename());
            Files.write(path, bytes);
        }
        logger.info("saveUploadedFiles");

        return fileName;
    }

    /**
     * Create a directory if doesn't exists.
     * @param path path of directory
     */
    private static void createDir(String path){

        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdir()) {
                logger.info("Directory is created!");
            } else {
                logger.info("Failed to create directory!");
            }
        }
    }
}
