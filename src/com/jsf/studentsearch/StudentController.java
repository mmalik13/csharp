package com.jsf.studentsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class StudentController {

	private List<Student> students;
	private StudentDbUtil studentDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	private String theSearchName;

	public String getTheSearchName() {
		return theSearchName;
	}

	public void setTheSearchName(String theSearchName) {
		this.theSearchName = theSearchName;
	}

	public StudentController() throws Exception {
		students = new ArrayList<>();

		studentDbUtil = StudentDbUtil.getInstance();
	}

	public List<Student> getStudents() {
		return students;
	}

	public void loadStudents() {

		logger.info("\n\n-------------------- Loading students");

		students.clear();

		try {

			// get all students from database
			students = studentDbUtil.getStudents();
			
			if (theSearchName != null && theSearchName.trim().length() > 0) {
				
				students = studentDbUtil.searchStudents(theSearchName);
			}
			
			else {
				students = studentDbUtil.getStudents();
			}

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading students", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	// Add ----------------------------------------------------------
	public String addStudent(Student theStudent) {

		logger.info("\n\n-------------------- Adding student: " + theStudent);
		
		try {

			// add student to the database
			studentDbUtil.addStudent(theStudent);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding students", exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "list-students?faces-redirect=true";
	}
	// --------------------------------------------------------------

	public String loadStudent(int studentId) {

		
		logger.info("\n\n --------------- loading student: " + studentId);
		
		try {
			//1- get student from database
			
			Student theStudent = studentDbUtil.getStudent(studentId);
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			//3- Get a reference object to request object Map attribute from ExternalContext object
			
			Map<String, Object> requestMap = externalContext.getRequestMap();
			
			requestMap.put("student", theStudent);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading student id:" + studentId, exc);
			
			addErrorMessage(exc);
			
			return null;
		}
		
		return "update-student-form.xhtml";
	}
	
	public String updateStudent(Student theStudent) {
		logger.info("\n\n------------- updating Student:" + theStudent );
		
		try {
			//1-Update student in the database
			studentDbUtil.updateStudent(theStudent);
			
		} catch(Exception exc) {
			//2- Send this to server logs
			logger.log(Level.SEVERE, "Error loading student" + theStudent, exc);
			
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-students?faces-redirect=true";
	}

	
	public String deleteStudent(int studentId) {
		logger.info("\n\n---------------- Deleting student id: " + studentId );
		
		try {
			//delte the student from the database
			studentDbUtil.deleteStudent(studentId);
			
		} catch (Exception exc) {
			logger.log(Level.SEVERE,"Error deleting student id: " + studentId, exc);
			
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-students";
	}

	
	

}



