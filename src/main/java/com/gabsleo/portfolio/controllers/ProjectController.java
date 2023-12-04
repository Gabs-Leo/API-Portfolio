package com.gabsleo.portfolio.controllers;

import com.gabsleo.portfolio.dtos.ProjectDto;
import com.gabsleo.portfolio.entitites.Project;
import com.gabsleo.portfolio.services.ProjectService;
import com.gabsleo.portfolio.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public ResponseEntity<Response<List<Project>>> findAll(){
        return ResponseEntity.ok(new Response<>(projectService.findAll()));
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Response<Project>> findById(@PathVariable Long id){
        Response<Project> response = new Response<>();
        if(projectService.findById(id).isEmpty()){
            response.getErrors().add("Project not found.");
            return ResponseEntity.badRequest().body(response);
        }
        response.setContent(projectService.findById(id).get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/projects")
    public ResponseEntity<Response<Project>> save(@RequestBody Project project){
        Response<Project> response = new Response<>();
        response.setContent(projectService.save(project));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/projects/{id}")
    public ResponseEntity<Response<ProjectDto>> update(@PathVariable Long id, @RequestBody ProjectDto projectDto){
        Response<ProjectDto> response = new Response<>();
        if(projectService.findById(id).isEmpty()){
            response.getErrors().add("Project not found.");
            return ResponseEntity.badRequest().body(response);
        }
        response.setContent(
            projectService.toDto( projectService.save(
                projectService.update(projectService.findById(id).get(), projectService.toEntity(projectDto))
            ))
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/projects/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable Long id){
        Response<String> response = new Response<>();
        if(projectService.findById(id).isEmpty()){
            response.getErrors().add("Project not found.");
            return ResponseEntity.badRequest().body(response);
        }
        projectService.delete(projectService.findById(id).get());
        response.setContent("Project deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
