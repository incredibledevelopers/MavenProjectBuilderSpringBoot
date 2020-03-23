package com.ipen.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipen.test.service.ProjectBuildService;

@RestController
@RequestMapping("/project/")
public class ProjectBuildController {

	@Autowired
	public ProjectBuildService projectBuildService;
	
	@GetMapping("build")
    public String  buildProjectFromPath(@RequestParam("path") String path) {
		if(projectBuildService.buildProject(path) != 0)	
			return "Maven Build Failed";
		else
			return "Build SUCCESS";
    }
}
