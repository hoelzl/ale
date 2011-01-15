package ale

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement
import ale.utils.UserChoice

class AleController {
  def aleService

  def jsonMethod(obj) {
    render(contentType: "text/plain", text: obj as JSON)
  }

  def jsonMethod(Boolean obj) {
    jsonMethod([result: obj])
  }

  def jsonMethod(Integer obj) {
    jsonMethod([result: obj])
  }

  def index = {
    jsonMethod(aleService.listLevels())
  }

  def listLevels = {
    jsonMethod(aleService.listLevels())
  }

  def getLevel = {
    jsonMethod(aleService.getLevel())
  }

  def setLevel = {
    String levelNumberString = params["levelNumber"] ?: "1"
    Integer levelNumber = levelNumberString.toInteger()
    // Cannot convert basic values to JSON, so we encapsulate it as an object.
    jsonMethod(aleService.setLevel(levelNumber))
  }

  def nextExercise = {
    jsonMethod(aleService.getNextExercise())
  }

  def answerCurrentExercise = {
    String answerString = params["answer"] ?: "[]"
    JSONElement answer = JSON.parse(answerString)
    def choice = new UserChoice(selectedAnswers: answer as List)
    jsonMethod(aleService.answerCurrentExercise(choice))
  }
}
