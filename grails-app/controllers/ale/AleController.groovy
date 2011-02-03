package ale

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement
import ale.utils.UserChoice

class AleController {
  def aleService
  // TODO: This list should automatically be generated from the service methods.
  def publicServices = [
          "index",
          "listLevels", "getCurrentLevel", "setCurrentLevel",
          "getCurrentExercise", "setCurrentExercise",
          "nextExercise",
          "answerCurrentExercise"
  ]

  def jsonMethod(success, errors) {
    if (success != null)
      render(contentType: "text/plain", text: [success: true, result: success] as JSON)
    else
      render(contentType: "text/plain", text: [success: false, errors: errors] as JSON)
  }

  def index = {
    jsonMethod(publicServices, ["Cannot list public services."])
  }

  def listLevels = {
    jsonMethod(aleService.listLevels(), ["Cannot list levels."])
  }

  def getCurrentLevel = {
    jsonMethod(aleService.getCurrentLevel(), ["No current level."])
  }

  def setCurrentLevel = {
    String levelNumberString = params["levelNumber"] ?: "1"
    Integer levelNumber = levelNumberString.toInteger()
    def result = aleService.setCurrentLevel(levelNumber)
    jsonMethod(result, ["$levelNumberString is not a valid Level number."])
  }

  def getCurrentExercise = {
    jsonMethod(aleService.getCurrentExercise(), ["No current exercise."])
  }

  def setCurrentExercise = {
    String exerciseIdString = params["exerciseId"]
    Integer exerciseId = exerciseIdString?.toInteger()
    def result = aleService.setCurrentExercise(exerciseId)
    jsonMethod(result, ["$exerciseIdString is not a valid exercise ID for level #${aleService.currentLevelNumber}."])
  }

  def nextExercise = {
    jsonMethod(aleService.nextExercise(), ["Could not advance to the next exercise."])
  }

  def answerCurrentExercise = {
    String answerString = params["answer"] ?: "[]"
    JSONElement answer = JSON.parse(answerString)
    def choice = new UserChoice(selectedAnswers: answer as List)
    def result = aleService.answerCurrentExercise(choice)
    if (result) {
      redirect([action: "rightAnswer"])
    }
    else {
      redirect([action: "wrongAnswer"])
    }
  }

  // TODO: Should only be allowed as a redirect.
  def rightAnswer = {
    jsonMethod(aleService.returnRightAnswer(), ["No current exercise."])
  }

  // TODO: Should only be allowed as a redirect.
  def wrongAnswer = {
    jsonMethod(aleService.returnWrongAnswer(), ["No current exercise."])
  }

  def showAutomata = {
    def context = servletContext
    jsonMethod(aleService.showAutomata(context), ["Automata not defined."])
  }
}
