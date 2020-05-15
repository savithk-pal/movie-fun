package org.superbiz.moviefun.blobstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileStore implements BlobStore {
    @Override
    public void put(Blob blob) throws Exception {

        String ext = blob.contentType.split("/")[1];
        String name = blob.name + "." + ext;
        File targetFile = getCoverFile(name);

        String foundPath = getFilePath(name);
        if(foundPath != null) {
            targetFile.delete();
            targetFile.getParentFile().mkdirs();
            targetFile.createNewFile();
        }

        byte[] buffer = new byte[blob.inputStream.available()];
        blob.inputStream.read(buffer);

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(buffer);
        }

    }

    @Override
    public Optional<Blob> get(String name) throws Exception {
        String foundPath = getFilePath(name);

        if(foundPath == null) {
            return Optional.empty();
        }

        File file = getCoverFile(foundPath);
        InputStream inputStream = new FileInputStream(file);
        Blob blob = new Blob(name, inputStream, Files.probeContentType(file.toPath()));

        return Optional.of(blob);
    }


    @Override
    public void deleteAll() {

    }

    private File getCoverFile(String name) {
        String coverFileName = String.format("covers/%s", name);
        return new File(coverFileName);
    }

    private String getFilePath(String name) {
        List<String> names = getFileNames();
        if(names != null) {
            for(String fileName : names) {
                String actualName = fileName.split("\\.")[0];
                if(actualName.equalsIgnoreCase(name)) {
                    return fileName;
                }
            }
            return null;
        }
        return null;
    }

    private ArrayList<String> getFileNames() {
        File file = new File("covers");
        if(file.list() != null) {
            return new ArrayList<>(Arrays.asList(file.list()));
        }
        return null;
    }
}
