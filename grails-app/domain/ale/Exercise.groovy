package ale

import ale.utils.ExerciseType
import ale.utils.UserChoice
import org.hibernate.collection.PersistentList

class Exercise implements Comparable{

  static belongsTo = [level: Level]
  static hasMany = [answers: Answer]

  String question
  ExerciseType type = ExerciseType.SINGLE_ANSWER
  // Would like to use a list, but that doesn't seem to work, event though the book says it should.
  SortedSet answers
  
  boolean validateUserChoice(UserChoice choice) {
    ExerciseType.validateUserChoice(type, choice, answers.collect { it.isCorrect })
  }

  int compareTo(Object o) {
    if (o.getClass() == this.getClass()) {
      if (o.id == this.id)
        return 0
      else if (o.id < this.id)
        return 1
      else
      return -1
    }
    else
      return -1
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

  def correctAnswers() {
    answers.findAll { it.isCorrect }
  }

  def correctAnswersAsText() {
    correctAnswers().collectAll { it.text }
  }

  def answersAsText() {
    answers.collectAll { it.text }
  }

}
