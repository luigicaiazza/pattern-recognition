package ch.welld.patternrecognition.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import ch.welld.patternrecognition.model.LineSegment;
import ch.welld.patternrecognition.model.Point;
import ch.welld.patternrecognition.model.Space;
import ch.welld.patternrecognition.model.utils.PointDeserializer;

@SpringBootTest
@AutoConfigureMockMvc
public class PatternRecognitionControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
				.apply(sharedHttpSession())
				.build();
	}

	@Test
	public void testAddPoint() throws Exception {
		String urlTemplate = "/point";

		// a request without a body is not valid
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("Wrong input format")));

		Point point = new Point(1, 2);
		String pointToJSONString = new ObjectMapper().writeValueAsString(point);

		// the first request is valid
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE).content(pointToJSONString))
				.andExpectAll(status().isOk(), content().string(containsString("Input point added")));

		// a request to insert the same point is not valid
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE).content(pointToJSONString))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("The input point is already in the space")));
		
		// a request to insert a point with a negative x value is not valid
		String pointWithNegativeXToJSONString = pointToJSONString.replace("1", "-1");
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE).content(pointWithNegativeXToJSONString))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("Argument \"x\" cannot be negative")));

		// a request to insert a point without specifying the x value is not valid
		String pointWithoutXToJSONString = pointToJSONString.replace("x", "otherKey");
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE).content(pointWithoutXToJSONString))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("Argument \"x\" is missing")));
		
		// a request to insert a point with a negative y value is not valid
		String pointWithNegativeYToJSONString = pointToJSONString.replace("2", "-2");
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE).content(pointWithNegativeYToJSONString))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("Argument \"y\" cannot be negative")));
		
		// a request to insert a point without specifying the y value is not valid
		String pointWithoutYToJSONString = pointToJSONString.replace("y", "otherKey");
		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON_VALUE).content(pointWithoutYToJSONString))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("Argument \"y\" is missing")));
	}
	
	@Test
	public void testGetAllPoints() throws Exception {
		String urlTemplate = "/space";
		
		Space mockSpace = new Space();
		ObjectMapper jsonObjectMapper = new ObjectMapper();
		String emptyMockSpaceToJSONString = jsonObjectMapper.writeValueAsString(mockSpace);
		
		// the first request ever returns an empty space
		mockMvc.perform(get(urlTemplate))
				.andExpectAll(status().isOk(), content().string(containsString(emptyMockSpaceToJSONString)));
		
		// by adding two points using "POST /point", we expect to get from this service the same space we have by adding them in our mock space
		Point point1 = new Point(1, 2);
		Point point2 = new Point(4, 7);
		
		String urlTemplateForAddPointService = "/point";
		mockMvc.perform(post(urlTemplateForAddPointService).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonObjectMapper.writeValueAsString(point1)));
		mockMvc.perform(post(urlTemplateForAddPointService).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonObjectMapper.writeValueAsString(point2)));
		
		mockSpace.addPoint(point1);
		mockSpace.addPoint(point2);
		
		String mockSpaceWithTwoPointsToJSONString = jsonObjectMapper.writeValueAsString(mockSpace);
		mockMvc.perform(get(urlTemplate))
				.andExpectAll(status().isOk(), content().string(containsString(mockSpaceWithTwoPointsToJSONString)));
	}
	
	@Test
	public void testGetAllLineSegments() throws Exception {
		String urlTemplateToFormat = "/lines/%d";
		ObjectMapper jsonObjectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule(PointDeserializer.class.getSimpleName(), new Version(1, 0, 0, null, null, null));
		module.addDeserializer(Point.class, new PointDeserializer());
		jsonObjectMapper.registerModule(module);
		
		// path variables less than 2 are illegal
		String urlTemplate0 = String.format(urlTemplateToFormat, 0);
		mockMvc.perform(get(urlTemplate0)).andExpect(status().is4xxClientError());
		String urlTemplate1 = String.format(urlTemplateToFormat, 1);
		mockMvc.perform(get(urlTemplate1)).andExpect(status().is4xxClientError());
		
		// since the space is empty, each valid request should return no line segments
		String urlTemplate2 = String.format(urlTemplateToFormat, 2);
		MvcResult urlTemplate2Result = mockMvc.perform(get(urlTemplate2)).andExpect(status().isOk()).andReturn();
		List<LineSegment> urlTemplate2ResultList = jsonObjectMapper.readValue(urlTemplate2Result.getResponse().getContentAsString(),
				jsonObjectMapper.getTypeFactory().constructCollectionType(List.class, LineSegment.class));
		assertThat(urlTemplate2ResultList, is(empty()));
		String urlTemplate3 = String.format(urlTemplateToFormat, 3);
		MvcResult urlTemplate3Result = mockMvc.perform(get(urlTemplate3)).andExpect(status().isOk()).andReturn();
		List<LineSegment> urlTemplate3ResultList = jsonObjectMapper.readValue(urlTemplate3Result.getResponse().getContentAsString(),
				jsonObjectMapper.getTypeFactory().constructCollectionType(List.class, LineSegment.class));
		assertThat(urlTemplate3ResultList, is(empty()));
		
		Point point1 = new Point(5, 10);
		Point point2 = new Point(6, 11);
		Point point3 = new Point(7, 8);
		Point point4 = new Point(7, 11);
		Point point5 = new Point(7, 12);
		
		List<Point> samplePoints = new ArrayList<>(5);
		samplePoints.add(point1);
		samplePoints.add(point2);
		samplePoints.add(point3);
		samplePoints.add(point4);
		samplePoints.add(point5);
		
		// add the points in the space using "POST /point"
		String urlTemplateForAddPointService = "/point";
		for (Point samplePoint : samplePoints) {
			mockMvc.perform(post(urlTemplateForAddPointService).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(jsonObjectMapper.writeValueAsString(samplePoint)));
		}
		
		LineSegment expectedLineSegmentWith3Points = new LineSegment();
		expectedLineSegmentWith3Points.addPoint(point1);
		expectedLineSegmentWith3Points.addPoint(point2);
		expectedLineSegmentWith3Points.addPoint(point5);
		
		LineSegment expectedLineSegmentWith2Points = new LineSegment();
		expectedLineSegmentWith2Points.addPoint(point1);
		expectedLineSegmentWith2Points.addPoint(point4);
		
		// repeat the requests with path variable values 2 and 3
		urlTemplate3Result = mockMvc.perform(get(urlTemplate3)).andExpect(status().isOk()).andReturn();
		urlTemplate3ResultList = jsonObjectMapper.readValue(urlTemplate3Result.getResponse().getContentAsString(),
				jsonObjectMapper.getTypeFactory().constructCollectionType(List.class, LineSegment.class));
		assertThat(urlTemplate3ResultList, is(not(empty())));
		assertThat(urlTemplate3ResultList, hasItem(expectedLineSegmentWith3Points));
		assertThat(urlTemplate3ResultList, not(hasItem(expectedLineSegmentWith2Points)));
		
		urlTemplate2Result = mockMvc.perform(get(urlTemplate2)).andExpect(status().isOk()).andReturn();
		urlTemplate2ResultList = jsonObjectMapper.readValue(urlTemplate2Result.getResponse().getContentAsString(),
				jsonObjectMapper.getTypeFactory().constructCollectionType(List.class, LineSegment.class));
		assertThat(urlTemplate2ResultList, is(not(empty())));
		assertThat(urlTemplate2ResultList, hasItem(expectedLineSegmentWith3Points));
		assertThat(urlTemplate2ResultList, hasItem(expectedLineSegmentWith2Points));
	}
	
	@Test
	public void testRemoveAllPoints() throws Exception {
		String urlTemplate = "/space";
		
		// if no points are in the space, the request to remove all the points is not valid
		mockMvc.perform(delete(urlTemplate))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("The space is empty")));
		
		// otherwise, the same request is valid
		Point point = new Point(1, 2);
		String urlTemplateForAddPointService = "/point";
		mockMvc.perform(post(urlTemplateForAddPointService).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(point)));
		
		mockMvc.perform(delete(urlTemplate))
				.andExpectAll(status().isOk(), content().string(containsString("All points are removed")));
		
		// the space should be empty after the last request, hence we expect an error if we repeat the request
		mockMvc.perform(delete(urlTemplate))
				.andExpectAll(status().is4xxClientError(), content().string(containsString("The space is empty")));
	}

}
