package com.gabsleo.portfolio.services;

import com.gabsleo.portfolio.entitites.Project;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

@Service
public class CloudStorageService {
    private final Storage storage;
    private final ProjectService projectService;

    @Value("${env.GCP_BUCKET_NAME}")
    private String bucket_name;

    @Autowired
    public CloudStorageService(Storage storage, ProjectService projectService) {
        this.storage = storage;
        this.projectService = projectService;
    }

    public void save(
            String path,
            MultipartFile mpfile
    ) throws IOException {
        BlobId id = BlobId.of(bucket_name, path+ "/" + "image.png");
        BlobInfo info = BlobInfo.newBuilder(id).build();

        storage.create(info, mpfile.getBytes());
    }

    public void save(Long id, String path, MultipartFile file) throws IOException {
        this.save(path + "/" + id, file);
    }

    public byte[] download(String path) throws IOException {
        try (ReadChannel channel = storage.reader(bucket_name, path)) {
            ByteBuffer bytes = ByteBuffer.allocate(1024*1024);
            while(channel.read(bytes) > 0) {
                bytes.flip();
                bytes.clear();
            }
            return bytes.array();
        }
    }

    public byte[] download(Long id) throws IOException {
        Optional<Project> project = projectService.findById(id);
        if(project.isEmpty())
            return null;
        return this.download("projects/" + id + "/image.png");
    }
}
