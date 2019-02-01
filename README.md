# Initial Learner Profiles for students

This component is responsible for creating initial learner profile for learners.

### Initial Skyline Specifications

The specifications are located [here](https://docs.google.com/document/d/1JuIowTy0ac8b5Gh9_YJqoloygR3VCCsBw_GLif-hOak/edit?ts=5c3fc204#bookmark=id.1yi9qhxfba66)

### Technical Scope

- State API will be exposed, which will govern the state of initial skyline setup flow when student comes to access a class. This API needs to be authenticated
- Batch processing infra would be provided to process the requests for calculating the skyline
- API to do Initial LP for the user in offline classes


### Build and Run

To create the shadow (fat) jar:

    ./gradlew

To run the binary which would be fat jar from the project base directory:

    java -jar skyline-initial.jar $(project_loc)/src/main/resources/skyline-initial.json

### API

#### State API
- This API should be called in context of students only
- This API will instruct which state the user needs to get into
- Following is the list of states
  - Incomplete Class setup
  - Access Course map
  - Do diagnostic: with asmt id
  - Show directions
  - Wait for ILP
  
##### API signature
POST /api/skyline-initial/v1/state

**Payload**
```{"classId": "class-uuid"}```

Needs session token for student to figure out student id. Error in case of where user 
is not active member specified class.

**Response**
```
{
  "destination": "diagnostic-play",
  "context": {
    "diagnosticId": "UUID for diag asmt"
  }
}
```

Valid values for destination are:
- course-map
- diagnostic-play
- show-directions
- ilp-in-progress
- class-setup-incomplete

The context object will be null in cases other then diagnostic-play.


  
#### Offline class ILP API
- This API should be called in context of teacher only and for offline classes only
- This API will take input as list of class members
- Do validations
  - The class should be valid and it should be offline
  - Each class members should have their origin set and they should be active
  - A premium course should be associated with class 
- If validation fails, no processing happens and 400 status is sent back to caller 
- If validation is successful, then requests are queued and status 200 is sent back

##### API signature
POST /api/skyline-initial/v1/calculate

**Payload**
```
{
  "classId": "class-uuid",
  "users": ["user1-uuid", "user2-uuid"]
}
```

**Response**

- HTTP Status : 200
- Payload: Empty 

### Design

#### DB changes needed

**Class membership enhancement**
- diagnostic assessment id
- diagnostic state
    - null 
    - not needed
    - suggested
    - done
    - not available
    - class offline
- ILP done
**Class enhancement**
- setup completed (calculate at run time or store in DB??)


#### State algorithm design
- Is class non navigator
    - Show course map. Done
- Is class setup (and student specific setup) complete
- If yes, then
    - Is baseline available
    - If yes
        - Show course map. Done
    - Else check state of diagnostic
        - If state == diag-suggested
            - Take specified diagnostic. Done
        - if state == diag-not-needed
            - Show directions. Done
        - If state == diag-done || state == diag-not-available
            - if initial lp state == done
                - Show directions. Done
            - Else
                - Wait for ILP being done. Done
        - if state == null
            - Is diagnostic needed
            - If yes
                - find diagnostic
                - if found
                    - update state to diag-suggested
                    - Do specified diagnostic. Done
                - else
                    - update state to diag-not-available
                    - update flag of ILP done to true
                    - Show directions. Done
            - Else
                - update state to diag-not-needed
                - Show directions. Done
- Else
    - Provide state as class setup pending from teacher side. Done

#### Batch processing flow
- User completes and submits a diagnostic assessment
- Log writer fires up and event to its post processor
- Post processor puts the data bits onto a table which acts as queue
- The batch processing infra polls the table
- If there is a new record available for processing, it is picked up
- Before the processing is actually started, the flag in class member table for diag-done is set
- The processing is done and ILP is updated, using ILP update flow
- The class member table is updated with flag for ILP generation completed

#### ILP update flow
- For diagnostic flows
  - Find out relevant competency in each domain
    - Arrange competency covered by diagnostic in each domain ordered by sequence id
    - Find maximum competency order for which all competency below are completed 
    - Choose it as destination competency
  - In LPCS, find if user has any competency in that domain already mastered or completed
  - Take better of user's LPCS evidenced competency and relevant competency from diagnostic 
  - Update LPCS

#### Class setup completion in student context
In student context, setup completion is defined by:
- The class context for specified student should have
  - Destination grade setup
  - Course is assigned to class
- The student context should have
  - Student origin setup 
  
#### Determination of whether ILP exists 
- Works in context of provided domains
- Domains could be inferred from Grade bound
- Domains could also be inferred from content course (but won't be supported)
- Given a grade find all the domains
- For each domain verify if there exists an entry in LPCS table with status as 4 or 5
- If there exists at least one domain for which such an entry is not there, then ILP does not exists  

#### Determination of whether diagnostic is needed or not
- Diagnostic is needed when
  - Class is using a navigator course and it is not offline
  - ILP does not exists for specified grade

#### Offline class with Premium course
If ILP is not done already, and there is at least one domain where in user does not have a LP, 
student origin will be used to populate ILP. Update logic will be governed by whichever is better
between origin and existing ILP.

If ILP is done, then no need to do anything. 

NOTE: From FE perspective, there is more to do. In case where class is offline with premium
course, any time teacher changes student settings, then:
- Call the API to save the settings
- If the baseline is already done for this user, then need to trigger the baseline again


### Miscellaneous tasks
- Baseline API should do delete and recompute
- R0 is useless, don't bother
- Baseline changes should trigger RSC (already in place)
- API to calculate baseline should work for either student or teacher/co-teacher context
  - Current API signature takes a list of userids
  - May have to create a new API for student
- Class origin should not be copied to student, destination should still be copied
- To calculate baseline, student origin should work off average line and not high/low line
- If class setup done flag is stored in class table, need to populate it based on class and prefs updates
- LogWriter post processor modifications
  - To remove processing of LPCS
  - Make it queue the request along with payload in queue table


