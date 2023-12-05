package com.gabsleo.portfolio.controllers;

import com.gabsleo.portfolio.services.CloudStorageService;
import com.gabsleo.portfolio.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ImageController {
    private final CloudStorageService cloudStorageService;

    @Autowired
    public ImageController(CloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @PostMapping(
            path = "/admin/projects/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long id
    ) throws IOException {
        var response = new Response<String>();

        cloudStorageService.save(
                "projects/"+id,
                file
        );

        response.setContent("File saved.");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/projects/{id}/image")
    public byte[] downloadCompanyLogo(
            @PathVariable("id") Long id
    ) throws IOException {
        return cloudStorageService.download(id);
    }
}
