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

  def jsonMethod(obj) {
    render(contentType: "text/plain", text: obj as JSON)
  }

  def jsonMethod(Boolean obj) {
    jsonMethod([ success: false ])
  }

  def jsonMethod(Integer obj) {
    jsonMethod([ success: true ])
  }

  def index = {
    jsonMethod([
            success: true,
            result: publicServices
    ])
  }

  def listLevels = {
    jsonMethod(aleService.listLevels())
  }

  def getCurrentLevel = {
    jsonMethod(aleService.getCurrentLevel())
  }

  def setCurrentLevel = {
    String levelNumberString = params["levelNumber"] ?: "1"
    Integer levelNumber = levelNumberString.toInteger()
    jsonMethod(aleService.setCurrentLevel(levelNumber))
  }

  def getCurrentExercise = {
    jsonMethod(aleService.getCurrentExercise())
  }

  def setCurrentExercise = {
    String exerciseIdString = params["exerciseId"]
    Integer exerciseId = exerciseIdString?.toInteger()
    jsonMethod(aleService.setCurrentExerciseId(exerciseId))
  }

  def nextExercise = {
    jsonMethod(aleService.nextExercise())
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
    jsonMethod(aleService.returnRightAnswer())
  }

  // TODO: Should only be allowed as a redirect.
  def wrongAnswer = {
    jsonMethod(aleService.returnWrongAnswer())
  }
}
