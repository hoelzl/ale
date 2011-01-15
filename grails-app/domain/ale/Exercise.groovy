package ale

import ale.utils.ExerciseType
import ale.utils.UserChoice
import org.hibernate.collection.PersistentList

class Exercise implements Comparable{

  static belongsTo = [level: Level]
  static hasMany = [answers: Answer]

  String question
  ExerciseType type = ExerciseType.SINGLE_ANSWER
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
}
