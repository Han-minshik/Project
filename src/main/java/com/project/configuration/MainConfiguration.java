package com.project.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "my.team_project_test.aspect")
@EnableAspectJAutoProxy
public class MainConfiguration {

}
