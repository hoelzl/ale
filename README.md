Adaptive Learning Environment (ALE)
===================================

A simple example for our Groovy adaptation engine.

The most important files are:

    grails-app/ale/LoggingFilters.groovy
      - Example filter, performs simple logging
    grails-app/conf/BootStrap.groovy
      - Some initial data for tests is generated here
    grails-app/controllers/ale/AleController.groovy
      - The one and only controller; delegates all of its work to
    grails-app/services/ale/AleService.groovy
      - The implementation of the user-visible services
    grails-app/domain/*
      - The domain model

A user interface will be added shortly.

Example interaction (manually formatted for better readability):

    tc@raven ~
    $ curl -L http://localhost:8080/ale/ale
    { "success":true,
      "result":[
        "index","listLevels","getCurrentLevel","setCurrentLevel",
        "getCurrentExercise","setCurrentExercise","nextExercise",
        "answerCurrentExercise"]}
    tc@raven ~
    $ curl -L http://localhost:8080/ale/ale/index
    { "success":true,
      "result":[
        "index","listLevels","getCurrentLevel","setCurrentLevel",
        "getCurrentExercise","setCurrentExercise","nextExercise",
        "answerCurrentExercise"]}
    tc@raven ~
    $ curl -L http://localhost:8080/ale/ale/listLevels
    { "success":true,
      "result":[
        {"class":"ale.Level","number":1,"name":"Beginner"},
        {"class":"ale.Level","number":2,"name":"Intermediate 1"},
        {"class":"ale.Level","number":3,"name":"Intermediate 2"},
        {"class":"ale.Level","number":4,"name":"Advanced}]}
    tc@raven ~
    $ curl -L http://localhost:8080/ale/ale/getCurrentLevel
    { "success":true,
      "result":{"class":"ale.Level","number":1,"name":"Beginner"}}
    tc@raven ~
    $ curl http://localhost:8080/ale/ale/setCurrentLevel?levelNumber=2
    { "success":true,
      "result":{"class":"ale.Level","number":2,"name":"Intermediate 1"}}
    tc@raven ~
    $ curl http://localhost:8080/ale/ale/setCurrentLevel?levelNumber=1
    { "success":true,
      "result":{"class":"ale.Level","number":1,"name":"Beginner"}}
    tc@raven ~
    $ curl http://localhost:8080/ale/ale/getCurrentExercise
    { "success":true,
      "result":{
        "class":"ale.Exercise",
        "exerciseId":1,
        "question":"Question 1.1",
        "answers":[
          "Answer 1.1.1","Answer 1.1.2","Answer 1.1.3",
	      "Answer 1.1.4","Answer 1.1.5"]}}
    tc@raven ~
    $ curl http://localhost:8080/ale/ale/nextExercise
    { "success":true,
      "result":{
        "class":"ale.Exercise",
        "exerciseId":2,
        "question":"Question 1.2",
        "answers":[
          "Answer 1.2.1","Answer 1.2.2","Answer 1.2.3"]}}
    tc@raven ~
    $ curl http://localhost:8080/ale/ale/nextExercise
    { "success":true,
      "result":{
        "class":"ale.Exercise",
        "exerciseId":1,
        "question":"Question 1.1",
        "answers":[
          "Answer 1.1.1","Answer 1.1.2","Answer 1.1.3",
	  "Answer 1.1.4","Answer 1.1.5"]}}
    tc@raven ~
    $ curl http://localhost:8080/ale/ale/nextExercise
    { "success":true,
      "result":{
        "class":"ale.Exercise",
        "exerciseId":3,
        "question":"Question 1.3",
        "answers":[
          "Answer 1.3.1","Answer 1.3.2","Answer 1.3.3"]}}
    tc@raven ~
    $ curl -L http://localhost:8080/ale/ale/answerCurrentExercise?answer=\\[false,false,true\\]
    { "success":true,
      "result":{
        "correctAnswer":true,
        "answerInfo":{
          "class":"ale.Exercise",
          "exerciseId":3,
          "question":"Question 1.3","answers":["Answer 1.3.3"]}}}
    tc@raven ~
    $ curl -L http://localhost:8080/ale/ale/answerCurrentExercise?answer=\\[false,false,false\\]
    { "success":true,
      "result":{
        "correctAnswer":false,
        "answerInfo":{
          "class":"ale.Exercise",
          "exerciseId":3,
          "question":"Question  1.3","answers":["Answer 1.3.3"]}}}
