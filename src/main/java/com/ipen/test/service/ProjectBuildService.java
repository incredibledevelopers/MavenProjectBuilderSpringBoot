package com.ipen.test.service;

import org.apache.maven.cli.MavenCli;
import org.springframework.stereotype.Component;

@Component
public class ProjectBuildService {
	
	public Integer buildProject(String path) {
		MavenCli cli = new MavenCli();
		return cli.doMain(new String[]{"clean", "install"}, path, System.out, System.out);
	}

}
