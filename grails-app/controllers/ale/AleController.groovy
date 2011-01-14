package ale

import grails.converters.JSON

class AleController {
  def aleService

  def index = {
    render(contentType: "text/plain", text: aleService.listLevels() as JSON)
  }

  def listLevels = {
    render(contentType: "text/plain", text: aleService.listLevels() as JSON)
  }

  def nextExercise = {
    render(contentType: "text/plain", text: aleService.getNextExercise() as JSON)
  }
}
