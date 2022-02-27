package com.hexaphor.liveclass.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
//public class SecurityConfig {

	 @Autowired private BCryptPasswordEncoder passwordEncoder;

	// @Autowired private NoOpPasswordEncoder nopasswordEncoder;

	@Autowired
	private UserDetailsService userService;

	/*
	 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 * 
	 * // Register resource handler for images
	 * 
	 * registry.addResourceHandler("/images/**").addResourceLocations(
	 * "/WEB-INF/images/") .setCacheControl(CacheControl.maxAge(2,
	 * TimeUnit.HOURS).cachePublic());
	 * 
	 * registry.addResourceHandler("/images/**").addResourceLocations( "/images/")
	 * .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
	 * 
	 * 
	 * }
	 */

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Without encoding password
		//auth.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
		
		//With encoding password
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}
	  
	  @Override protected void configure(HttpSecurity http) throws Exception {
	  
	  http.authorizeRequests().antMatchers("/user/showLogin", "/js**",
	  "/images**","/student/register","/student/save","/teacher/register","/teacher/save","/showLogin").permitAll()
	  
	  
	  
	  .antMatchers("/student/profile","/student/passwordModify","/student/passwordUpdate")
	  .hasAnyAuthority("ADMIN","TEACHER","STUDENT")
	  
	  //Batch
	  .antMatchers("/batch/alls").hasAuthority("ADMIN")
	  .antMatchers("/batch/allbySchool").hasAuthority("ADMIN")
	  .antMatchers("/batch/register").hasAuthority("ADMIN")
	  .antMatchers("/batch/save").hasAuthority("ADMIN")
	  .antMatchers("/batch/batchById").hasAuthority("ADMIN")
	  .antMatchers("/batch/removeBatchById").hasAuthority("ADMIN")
	  .antMatchers("/batch/update").hasAuthority("ADMIN")
	  .antMatchers("/batch/all").hasAuthority("ADMIN")
	  .antMatchers("/batch/Batch").hasAuthority("ADMIN")
	  //subject
	  .antMatchers("/subject/alls").hasAuthority("ADMIN")
	  .antMatchers("/subject/allbySchool").hasAuthority("ADMIN")
	  .antMatchers("/subject/register").hasAuthority("ADMIN")
	  .antMatchers("/subject/save").hasAuthority("ADMIN")
	  .antMatchers("/subject/subjectById").hasAuthority("ADMIN")
	  .antMatchers("/subject/removeSubjectById").hasAuthority("ADMIN")
	  .antMatchers("/subject/update").hasAuthority("ADMIN")
	  .antMatchers("/subject/all").hasAuthority("ADMIN")
	  .antMatchers("/subject/subject").hasAuthority("ADMIN")
	  //Student
	  .antMatchers("/student/alls").hasAuthority("ADMIN")
	  .antMatchers("/student/allbySchool").hasAuthority("ADMIN")
	  .antMatchers("/student/StudentById").hasAnyAuthority("ADMIN","TEACHER","STUDENT")
	  .antMatchers("/student/removeStudentById").hasAuthority("ADMIN")
	  .antMatchers("/student/update").hasAnyAuthority("ADMIN","STUDENT")
	  .antMatchers("/student/all").hasAuthority("ADMIN")
	  .antMatchers("/student/allConference").hasAuthority("STUDENT")
	  .antMatchers("/student/allMeeting").hasAuthority("STUDENT")
	  
	  //TEACHER
	  
	  .antMatchers("/teacher/allPage").hasAuthority("ADMIN")
	  .antMatchers("/teacher/allbySchool").hasAuthority("ADMIN")
	  .antMatchers("/teacher/teacherById").hasAnyAuthority("ADMIN","TEACHER")
	  .antMatchers("/teacher/removeTeacherById").hasAuthority("ADMIN")
	  .antMatchers("/teacher/update").hasAnyAuthority("ADMIN","TEACHER")
	  .antMatchers("/teacher/all").hasAuthority("ADMIN")
	  .antMatchers("teacher/viewAllStudentById").hasAuthority("TEACHER")
	  .antMatchers("teacher/allMeeting").hasAuthority("TEACHER")
	  
	  //Conference
	  .antMatchers("/conference/alls").hasAuthority("ADMIN")
	  .antMatchers("/conference/viewAllStudentById").hasAuthority("ADMIN")
	  .antMatchers("/conference/register").hasAuthority("ADMIN")
	  .antMatchers("/conference/save").hasAuthority("ADMIN")
	  .antMatchers("/conference/update").hasAuthority("ADMIN")
	  .antMatchers("/conference/registerteacher").hasAuthority("TEACHER")
	  .antMatchers("/conference/saveMeetingForTeacher").hasAuthority("TEACHER")
	  
	  
	  
	  
	 
	  .anyRequest().authenticated()
	  
	  .and().formLogin().loginPage("/showLogin")// to display login page
	  .loginProcessingUrl("/login")// form action
	  .defaultSuccessUrl("/setup",true)
	  .failureUrl("/showLogin?error")// default true
	  
	  .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	  .logoutSuccessUrl("/showLogin?success")
	  
	  .and().exceptionHandling().accessDeniedPage("/accessdenied"); }
	  
	  @Override public void configure(WebSecurity web) throws Exception {
	  web.ignoring() 
	  .antMatchers("/resource/**", "/static/**", "/images/**",
	  "/js/**"); }
	 

}
