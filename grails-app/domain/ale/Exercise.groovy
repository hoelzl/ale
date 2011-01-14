package ale

import ale.utils.ExerciseType
import ale.utils.UserChoice

class Exercise {

  static belongsTo = [level: Level]
  static hasMany = [answers: Answer]

  String question
  ExerciseType type = ExerciseType.SINGLE_ANSWER

  boolean validateUserChoice(UserChoice choice) {
    type.validateUserChoice(choice, answers.collect { it.isCorrect })
  }

  String toString() {
    "Exercise: $question"
  }

  static constraints = {
    level(nullable: false)
    type()
    question(nullable: false, blank: false)
    answers()
  }
}
