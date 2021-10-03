package ch.welld.patternrecognition.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;

import ch.welld.patternrecognition.model.LineSegment;
import ch.welld.patternrecognition.model.Point;
import ch.welld.patternrecognition.model.Space;

@Controller
public class PatternRecognitionController {
	
	@Autowired
	private Space space;
	
	/*****************************************************
	 * just the home page with the text of this exercise *
	 *****************************************************/
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String homePage() {
		return "exercise";
	}
	
	/*******************************
	 * 1) Add a point to the space *
	 *******************************/
	@RequestMapping(method = RequestMethod.POST, value = "/point", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> addPoint(@RequestBody Point point) {
		if (point == null) {
			return new ResponseEntity<>("Input point not provided", HttpStatus.BAD_REQUEST);
		}
		try {
			space.addPoint(point);
		} catch (IllegalArgumentException iae) {
			return new ResponseEntity<>(iae.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>("Input point added", HttpStatus.OK);
	}
	
	/**********************************
	 * 2) Get all points in the space *
	 **********************************/
	@RequestMapping(method = RequestMethod.GET, value = "/space", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Space getAllPoints() {
		return space;
	}
	
	/**************************************************************
	 * 3) Get all line segments passing through at least N points *
	 **************************************************************/
	@RequestMapping(method = RequestMethod.GET, value = "/lines/{n}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LineSegment>> getAllLineSegments(@PathVariable int n) {
		if (n < 2) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(space.getAllLineSegments(n), HttpStatus.OK);
	}
	
	/***************************************
	 * 4) Remove all points from the space *
	 ***************************************/
	@RequestMapping(method = RequestMethod.DELETE, value = "/space", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> removeAllPoints() {
		if (space.isEmpty()) {
			return new ResponseEntity<>("The space is empty", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		space.clear();
		return new ResponseEntity<>("All points are removed", HttpStatus.OK);
	}
	
	/***********************************
	 * EXTRA - Some exception handling *
	 ***********************************/
	@ExceptionHandler(ValueInstantiationException.class)
	public ResponseEntity<String> handleValueInstantiationException(ValueInstantiationException vie) {
		String message;
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		Throwable cause = vie.getCause();
		if (cause != null) {
			message = cause.getMessage();
		} else {
			message = vie.getOriginalMessage();
		}
		if (!StringUtils.hasText(message)) {
			message = "Bad request";
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(message, status);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
		Throwable cause = hmnre.getCause();
		if(cause != null && cause instanceof ValueInstantiationException) {
			return handleValueInstantiationException((ValueInstantiationException) cause);
		}
		return new ResponseEntity<>("Wrong input format", HttpStatus.BAD_REQUEST);
	}

}
