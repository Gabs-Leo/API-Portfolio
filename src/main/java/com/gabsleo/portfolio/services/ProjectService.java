package com.gabsleo.portfolio.services;

import com.gabsleo.portfolio.dtos.ProjectDto;
import com.gabsleo.portfolio.entitites.Project;
import com.gabsleo.portfolio.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Optional<Project> findById(Long id){
        return projectRepository.findById(id);
    }

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Project save(Project project){
        return projectRepository.save(project);
    }

    public void delete(Project project){
        projectRepository.delete(project);
    }

    public ProjectDto toDto(Project project){
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getRepositoryUrl()
        );
    }

    public Project toEntity(ProjectDto projectDto){
        return new Project(
                projectDto.id(),
                projectDto.name(),
                projectDto.description(),
                projectDto.repositoryUrl()
        );
    }

    public Project update(Project projectOld, Project projectNew){
        projectOld.setName(projectNew.getName());
        projectOld.setDescription(projectNew.getDescription());
        projectOld.setRepositoryUrl(projectNew.getRepositoryUrl());

        return projectOld;
    }
}
