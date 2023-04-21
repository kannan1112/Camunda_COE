package com.camundasaas.orchestration.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camundasaas.orchestration.Model.Client.Retailer;
import com.camundasaas.orchestration.Model.Client.VendorRegistration;
import com.camundasaas.orchestration.Model.Client.logindata;
import com.camundasaas.orchestration.Model.Client.vmsdata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.auth.SimpleAuthentication;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;

@RestController
public class VmsController {

	@Autowired
	ZeebeClient zeebeClient;

	final RestTemplate rest = new RestTemplate();

	@PostMapping("/startWorkFlow")
	public void startWorkFlow() throws Exception {
		System.out.println("starting");

//		JSONParser jsonparser = new JSONParser();
//		Object obj = jsonparser.parse(new FileReader("D:\\FileReader\\WounderSoft\\shopaid.txt"));
//		JSONObject jsonObject = (JSONObject) obj;
//		System.out.println(jsonObject);
//
//		String str = jsonObject.toString();
//		System.out.println("String===" + str);
//		Map jsonObjMap = jsonObject;
//		System.out.println(jsonObjMap);

		zeebeClient.newCreateInstanceCommand().bpmnProcessId("VendorManagementSystem").latestVersion().send().join();

		// client.newCreateInstanceCommand().bpmnProcessId("UserTask1").latestVersion().variables(str).send().join();
		System.out.println("started");
	}

	@CrossOrigin
	@RequestMapping(value = "/startWorkFlow", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public String getVendorDetails(@RequestBody String reqBody) {
		System.out.println("Vendor Details : " + reqBody);
		Map<String, Object> reqBodyMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			reqBodyMap = mapper.readValue(reqBody, Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		zeebeClient.newCreateInstanceCommand().bpmnProcessId("VendorManagementSystem").latestVersion()
				.variables(reqBodyMap).send().join();
		System.out.println("flow  Started");

		return reqBody;
	}

///////////////////////// Get Active Task List ///////////////////////////////

	@GetMapping("/getCreatedTask")
	public TaskList getTask() throws TaskListException {

		String inputBaseUrl = "https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8";
		String inputAuthUrl = "https://login.cloud.camunda.io/oauth/token";
		String inputClientId = "jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ";
		String inputClientSecret = "wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf";

		SaasAuthentication sa = new SaasAuthentication(inputClientId, inputClientSecret);

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8").build();

		return client.getTasks(true, TaskState.CREATED, 50);
//		System.out.println("TaskList---: "+client.toString());
//
//		List<Task> getTaskList = client.getTasks(false, TaskState.CREATED, 50);
//		
//		 for(Object getTask : getTaskList ) {
//			 
//			 System.out.println(getTask.toString());
//			// Map getTaskAsMap = (Map) getTask;
//			 //System.out.println(getTaskAsMap);
//		 }
//		 System.out.println("TaskList---: "+getTaskList.toString());
//		
//		 return getTaskList;

//		SimpleAuthentication sa = new SimpleAuthentication("demo", "demo");
//
//		CamundaTaskListClient client = new CamundaTaskListClient.Builder().taskListUrl("http://localhost:8080")
//				.shouldReturnVariables().authentication(sa).build();
//
		// List<Task> tasks = client.getAssigneeTasks(null, TaskState.CREATED, null);
//		
//		System.out.println("Active Task List : "+tasks);
//
////		 Task task = client.getTask(jobkey);

	}

//////////////////////////////////////////////////

//////////////////////////////////////////////////
	@CrossOrigin
	@GetMapping("/getAssigendTaskList/{userName}")
	public List<Object> getTaskList(@PathVariable String userName) {

//HttpHeaders headers = new HttpHeaders();
//HttpEntity<String> reqEntity = new HttpEntity<String>(headers);
		String url = "http://localhost:8081/getAssigineeTask/" + userName;
//ResponseEntity<Map> respEntity = rest.exchange(url, HttpMethod.POST,reqEntity,Map.class );

//Map getBody = respEntity.getBody();
//System.out.println(getBody);

		RestTemplate restTemplate = new RestTemplate();
//List<Object> result = restTemplate.getForObject(url, List.class);

		Map result = restTemplate.getForObject(url, Map.class);

		List getItems = (List) result.get("items");

		System.out.println("Items---" + getItems);

		List<Object> finalTaskList = new ArrayList<Object>();

		for (Object getList : getItems) {

//System.out.println("getList--"+getList);

			Map getListAsMap = (Map) getList;

			String getId = (String) getListAsMap.get("id");
			String getName = (String) getListAsMap.get("name");
			String getAssignee = (String) getListAsMap.get("assignee");
			String getProcessDefinitionId = (String) getListAsMap.get("processDefinitionId");

			Map<String, Object> finalMap = new HashMap<String, Object>();

			finalMap.put("id", getId);
			finalMap.put("name", getName);
			finalMap.put("assignee", getAssignee);
			finalMap.put("processDefinitionId", getProcessDefinitionId);

			finalTaskList.add(finalMap);

		}
		return finalTaskList;

	}

	////////////////////////////////////////////

	//// claim all task :-

	@CrossOrigin
	@GetMapping("/getUnAssigendTaskList")
	public List<Object> getTaskList1() {

//HttpHeaders headers = new HttpHeaders();
//HttpEntity<String> reqEntity = new HttpEntity<String>(headers);
		String url = "http://localhost:8081/getUnclaimTask";
//ResponseEntity<Map> respEntity = rest.exchange(url, HttpMethod.POST,reqEntity,Map.class );

//Map getBody = respEntity.getBody();
//System.out.println(getBody);

		RestTemplate restTemplate = new RestTemplate();
//List<Object> result = restTemplate.getForObject(url, List.class);

		Map result = restTemplate.getForObject(url, Map.class);

		List getItems = (List) result.get("items");

		System.out.println("Items---" + getItems);

		List<Object> finalTaskList = new ArrayList<Object>();

		for (Object getList : getItems) {

//System.out.println("getList--"+getList);

			Map getListAsMap = (Map) getList;

			String getId = (String) getListAsMap.get("id");
			String getName = (String) getListAsMap.get("name");
			String getAssignee = (String) getListAsMap.get("assignee");
			String getProcessDefinitionId = (String) getListAsMap.get("processDefinitionId");

			Map<String, Object> finalMap = new HashMap<String, Object>();

			finalMap.put("id", getId);
			finalMap.put("name", getName);
			finalMap.put("assignee", getAssignee);
			finalMap.put("processDefinitionId", getProcessDefinitionId);

			finalTaskList.add(finalMap);

		}
		return finalTaskList;

	}

	@CrossOrigin
	@GetMapping("/getTaskList")
	public List<Object> getunclaimTaskList() {

//HttpHeaders headers = new HttpHeaders();
//HttpEntity<String> reqEntity = new HttpEntity<String>(headers);
		String url = "http://localhost:8081/getCreatedTask";
//ResponseEntity<Map> respEntity = rest.exchange(url, HttpMethod.POST,reqEntity,Map.class );

//Map getBody = respEntity.getBody();
//System.out.println(getBody);

		RestTemplate restTemplate = new RestTemplate();
//List<Object> result = restTemplate.getForObject(url, List.class);

		Map result = restTemplate.getForObject(url, Map.class);

		List getItems = (List) result.get("items");

		System.out.println("Items---" + getItems);

		List<Object> finalTaskList = new ArrayList<Object>();

		for (Object getList : getItems) {

//System.out.println("getList--"+getList);

			Map getListAsMap = (Map) getList;

			String getId = (String) getListAsMap.get("id");
			String getName = (String) getListAsMap.get("name");
			String getAssignee = (String) getListAsMap.get("assignee");
			String getProcessDefinitionId = (String) getListAsMap.get("processDefinitionId");

			Map<String, Object> finalMap = new HashMap<String, Object>();

			finalMap.put("id", getId);
			finalMap.put("name", getName);
			finalMap.put("assignee", getAssignee);
			finalMap.put("processDefinitionId", getProcessDefinitionId);

			finalTaskList.add(finalMap);

		}
		return finalTaskList;

	}

	@CrossOrigin
	@PostMapping("/getapprove")
//@RequestMapping(value = "/getapprove", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public VendorRegistration approvedmail() throws Exception {

		String approve = "Request Approved";
		System.out.println(approve);
		return null;

		// client.newSetVariablesCommand(Long.valueOf().variables(input).local(true).send().join();

	}

	@CrossOrigin
	@RequestMapping(value = "/getrejected", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public VendorRegistration rejectedmail() throws Exception {

		String reject = "Request Rejected";
		System.out.println(reject);
		return null;

	}

	@CrossOrigin
	@RequestMapping(value = "/taskCompleted/{taskId}", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
	public void completedTask(@RequestBody Map dataVendor, @PathVariable long taskId) {

		System.out.println("Complete Task.....: " + taskId);

		System.out.println("Decision : " + dataVendor.toString());

//	String completeTaskStatus = runtimeService.completeTask(taskId, dataVendor);
//
//	System.out.println("completeTaskStatus...:" + completeTaskStatus);

		// client.newCompleteCommand(dataVendor).variables("").send();

		zeebeClient.newCompleteCommand(taskId).variables(dataVendor).send().join();

	}

	/////////////////////// getvariables from ui//////////////

	@GetMapping("/getTaskvariable/{taskId}")
	public Task getvariabletask(@PathVariable String taskId) throws TaskListException {

		System.out.println(taskId);
		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",
				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder().authentication(sa)
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8/").build();

		return client.getTask(taskId, true);
	}

	///////////////////////////// Get Variables ///////////////

	@CrossOrigin
	@GetMapping("/getIndividualVariable/{taskId}")
	public List<Task> getVariable(@PathVariable String taskId) {

		System.out.println("Get Variable Method Called");

		String url = "http://localhost:8081/getTaskvariable/" + taskId;
		System.out.println("url--" + url);

		RestTemplate restTemplate = new RestTemplate();
		Map result = restTemplate.getForObject(url, Map.class);

		System.out.println("result ===" + result);

		List<Object> getVariableList = (List<Object>) result.get("variables");

		List finalVarList = new ArrayList<>();

		Map<String, String> finalVariable = new HashMap<String, String>();

		for (Object getVariable : getVariableList) {

			Map veriableMap = (Map) getVariable;
			String varName = (String) veriableMap.get("name");
			String varValue = (String) veriableMap.get("value");

			finalVariable.put(varName, varValue);

			System.out.println(finalVarList);
		}
		finalVarList.add(finalVariable);

		// System.out.println(finalVarList);

		return finalVarList;

	}

//////////////////////////Claim Task /////////////////////////////
	@CrossOrigin
	@GetMapping("/cliamTask/{jobKey}/{assigne}")
	public Task claimTask(@PathVariable String jobKey, @PathVariable String assigne) throws TaskListException {

		System.out.println("cliam Task");

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",
				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
				.shouldReturnVariables().authentication(sa).build();
		Task task = client.claim(jobKey, assigne);

		return task;

	}

	//////////////////////////// Uncliam Task///////////////////////////////

	@CrossOrigin
	// @GetMapping("/uncliamTask/{jobKey}")
	@RequestMapping(value = "/uncliamTask/{jobKey}", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")

	public Task uncliamTask(@PathVariable String jobKey) throws TaskListException {

		System.out.println("Uncliam Task");

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")

				.shouldReturnVariables().authentication(sa).build();

		Task task = client.unclaim(jobKey);

		return task;

	}

/////////////////////////////// Complete user Task /////////////////////////////////////
	@CrossOrigin
	@GetMapping("/completeTask/{TaskId}")
	public Task completeTask(@PathVariable String TaskId) throws TaskListException {
		System.out.println("Complete Task");

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",
				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
				.shouldReturnVariables().authentication(sa).build();
		Task task = client.completeTask(TaskId, null);

		return task;

	}

	@CrossOrigin

	@RequestMapping(value = "/completeTask/{taskId}", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")

	public Task claimTask(@RequestBody Map dataVendor, @PathVariable String taskId) throws TaskListException {

		System.out.println("Complete Task");

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",

				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()

				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")

				.shouldReturnVariables().authentication(sa).build();

		Task task = client.completeTask(taskId, dataVendor);

		return task;

	}

///////////////////////////// Get Variables ///////////////
	@CrossOrigin
	@GetMapping("/getVariable")
	public Task getVariable() {

		System.out.println("Get Variable Method Called");

		String url = "http://localhost:8081/getCreatedTask";

		RestTemplate restTemplate = new RestTemplate();
		List<Object> result = restTemplate.getForObject(url, List.class);

		List finalVarList = new ArrayList<>();

		for (Object getvariableList : result) {

			Map getVariable = (Map) getvariableList;

			List<Object> variables = (List<Object>) getVariable.get("variables");

			Map<String, String> finalVariable = new HashMap<String, String>();

			for (Object getVar : variables) {

				Map veriableMap = (Map) getVar;
				String varName = (String) veriableMap.get("name");
				String varValue = (String) veriableMap.get("value");

				finalVariable.put(varName, varValue);
				finalVarList.add(finalVariable);

			}

		}

		System.out.println(finalVarList);

		return null;

	}

	@GetMapping("/getAssigineeTask/{userName}")
	public TaskList getAssigineeTask(@PathVariable String userName) throws TaskListException {
		System.out.println("Complete Task");

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",
				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
				.shouldReturnVariables().authentication(sa).build();
		return client.getAssigneeTasks(userName, TaskState.CREATED, 50);

	}

	/////////////////////////////////

	@GetMapping("/getUnclaimTask")
	public TaskList getUnclaimTask() throws TaskListException {

		SaasAuthentication sa = new SaasAuthentication("jiIaOU5bGP1HJbyR3jZ.bhqsiCpTMTZZ",
				"wz0YxMw.oapyIi48t8aUrqOMXfubR9953gBuwa8cMqMG-595cyhM16wPAhNIKdJf");

		CamundaTaskListClient client = new CamundaTaskListClient.Builder()
				.taskListUrl("https://bru-2.tasklist.camunda.io/1a8d8e18-4054-4bd2-afad-6f2adf8c58b8")
				.shouldReturnVariables().authentication(sa).build();
		return client.getTasks(false, TaskState.CREATED, true, 50);

	}

// read json :-
//	@CrossOrigin
//	@GetMapping("/readjson")
//
//	public JSONArray jsonFileReader1() throws FileNotFoundException, IOException, ParseException {
//		JSONParser jsonParser = new JSONParser();
//		Object obj = jsonParser.parse(new FileReader("D:\\Camunda-Workspace\\VMS-Saas\\src\\main\\resources\\Address.json"));
//		JSONArray jsonObject = (JSONArray) obj;
//		return jsonObject;
//
//	}
	@CrossOrigin
	@GetMapping("/readjson")

	public Object jsonFileReader1() throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser
				.parse(new FileReader("D:\\Camunda-Workspace\\VMS-Saas\\src\\main\\resources\\Address.json"));
		JSONArray obj2 = (JSONArray) obj;
		String string = obj2.toString();

		ObjectMapper objectMapper = new ObjectMapper();

		List<Retailer> listCar = objectMapper.readValue(string, new TypeReference<List<Retailer>>() {
		});
		return listCar;

	}
	@CrossOrigin
	@GetMapping("/loginjson")
	
	public List<Map<String, String>> getCredentials()throws IOException, ParseException {
		  
		  JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser
					.parse(new FileReader("D:\\Camunda-Workspace\\VMS-Saas\\src\\main\\resources\\users.json"));
			JSONArray obj2 = (JSONArray) obj;
			String string = obj2.toString();

			ObjectMapper objectMapper = new ObjectMapper();


			 List<Map<String, String>> credentials = new ArrayList<>();
        List<vmsdata> users = objectMapper.readValue(string, new TypeReference<List<vmsdata>>() {

	  });
        
        for (vmsdata person : users) {
            Map<String, String> credential = new LinkedHashMap<>();
            credential.put("email", person.geteMail());
            credential.put("password", person.getPassword());
            credentials.add(credential);
        }

     return credentials;
    }


	

	@GetMapping("/writejson")
	public List<VendorRegistration> subscribe(@RequestBody List<VendorRegistration> input) {
//		 VendorRegistration person = new VendorRegistration();
		System.out.println(input.toString());
//	        person.setCompanyName("John");
//	        person.setVendorId("30");

		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(new File("D:\\Camunda-Workspace\\VMS-Saas\\src\\main\\resources\\Address.json"), input);
			System.out.println("Successfully wrote JSON object to file");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;

	}

	@CrossOrigin
	@PostMapping("/users")
	public ResponseEntity<String> createEmployee(@RequestBody vmsdata employee) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File("D:\\Camunda-Workspace\\VMS-Saas\\src\\main\\resources\\users.json");

		// Create the file if it doesn't exist
		if (!file.exists()) {
			file.createNewFile();
		}
		// Read the existing data from the file into a list
		List<vmsdata> employees = new ArrayList<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			TypeReference<List<vmsdata>> typeReference = new TypeReference<List<vmsdata>>() {
			};
			employees = objectMapper.readValue(fileInputStream, typeReference);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set the ID for the new employee

		// Add the new employee to the list
		employees.add(employee);

		// Write the updated data to the file
		try {
			FileWriter fileWriter = new FileWriter(file);
			objectMapper.writeValue(fileWriter, employees);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok("Employee created");
	}

}
