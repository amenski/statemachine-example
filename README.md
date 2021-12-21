The allowed state transitions are:

ADDED -> IN-CHECK <-> APPROVED -> ACTIVE

Furthermore, IN-CHECK state is special and has the following orthogonal child substates:

  - SECURITY_CHECK_STARTED
  - SECURITY_CHECK_FINISHED
  

  - WORK_PERMIT_CHECK_STARTED
  - WORK_PERMIT_CHECK_FINISHED

with allowed state transitions:
- SECURITY_CHECK_STARTED -> SECURITY_CHECK_FINISHED
- WORK_PERMIT_CHECK_STARTED -> WORK_PERMIT_CHECK_FINISHED

This means that a complete state of an employee in the IN_CHECK state could look like (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED).

Examples of permitted transition:
* (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED) -> (IN_CHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED)
* (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED) -> (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED)

Transition from IN_CHECK state to APPROVED state happens automatically when the complete state is (IN_CHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_FINISHED).
Transition from IN_CHECK state to APPROVED without meeting the condition above is not allowed.

![state diagram](statemachine.png?raw=true "Statemachine diagram")

**How to run**
Note: `EmployeeTerms` table is populated automatically from a script in the resources forlder. This mock table serves as a table to hold contract and position details.
There are 2 modes to run it.
1. The traditional way with `java -jar`
	- Change directory(cd) in to the folder of the application source code
	- Run `mvn clean package`
	- Issue `java -jar ems-1.0.0.jar`
	- Use the requests provided below.
2. From docker container
	- Build the applicaiton `mvn clean package`
	- Build docker image `docker build -t ems .`
	- Run the image `docker run -d -it --name ems ems:latest`
	- Get the ip of the container `docker inspect ems | grep Address`
	- Replace the ip address below with the address from the above command and do the calls

Requests:
**Create employee**
```
curl -X 'POST'   'http://172.17.0.3:8080/v1/employee'   \
-H 'accept: application/json' \
-H 'Content-Type: application/json' -d '{
  "firstName": "string",
  "lastName": "string",
  "dob": "2021-12-16",
  "gender": "F",
  "passportNumber": "string",
  "employementTerms": {"id": 2}
}'
```
Sample response:
```
{
	"type": "ResponseBase",
	"success": true,
	"resultCode": 0,
	"transactionId": "b338a35e-9c1e-4a5f-bb87-7f3be0ce0475"
}
```

**Change state with an event**
```
curl -X 'PUT'   'http://172.17.0.3:8080/v1/employee/1'   -H 'accept: application/json'   -H 'Content-Type: application/json'   -d 'IN_CHECK'
```

***Error response(in case of wrong state is requested):***
```
{
	"type": "ResponseBase",
	"success": true,
	"resultCode": 0,
	"transactionId": "492eba92-a41e-4914-99b9-572b947b02bc"
}
```

**Get employee**
```
curl -X 'GET'   'http://172.17.0.3:8080/v1/employee?employeeId=1'  -H 'accept: application/json'
```

***Success response:***
```
{
	"type": "response-employee",
	"success": true,
	"resultCode": 0,
	"transactionId": "8ec635cc-e2ca-4cbf-b7ef-3c82886c76ed",
	"returnValue": {
		"id": 1,
		"firstName": "string",
		"lastName": "string",
		"dob": "2021-12-16",
		"gender": "F",
		"passportNumber": "string",
		"status": [
			"IN_CHECK",
			"WORK_PERMIT_CHECK_STARTED",
			"SECURITY_CHECK_STARTED"
		],
		"employementTerms": {
			"id": 2,
			"jobTitle": "Backend developer",
			"jobDescription": "Job description goes here",
			"skillRequirement": "Skill requirement",
			"educationRequirement": "Education requirement goes here",
			"annualSalary": 50000.0
		}
	}
}
```
**Error response:**
```
{
	"type": "response-employee",
	"success": false,
	"resultCode": 404001,
	"message": "Employee not found.",
	"transactionId": "e295944d-6fb5-49ad-83da-50fd5e74a3e5"
}
```