package ale.utils

/**
 * Created by IntelliJ IDEA.
 * User: tc
 * Date: 1/14/11
 * Time: 5:07 PM
 */
public enum ExerciseType {
  SINGLE_ANSWER("Single Answer"),
  ALL_ANSWERS("All Answers"),
  ANY_ANSWER("Any Answer");

  ExerciseType(String name) {
    this.name = name
  }

  String name
  String toString() {
    name
  }

  static boolean validateUserChoice(ExerciseType type, UserChoice choice, List correctAnswers) {
    def result = true
    switch (type) {
      case SINGLE_ANSWER:
        if (choice.numberOfAnswers() != 1)
          result = false
        else {
          choice.selectedAnswers.eachWithIndex {a, i ->
            if (a != correctAnswers[i])
              result = false
          }
        }
        break
      case ALL_ANSWERS:
      choice.selectedAnswers.eachWithIndex {c, i ->
        if (c != correctAnswers[i])
          result = false
      }
      break
      case ANY_ANSWER:
      result = false
      choice.selectedAnswers.eachWithIndex {c, i ->
        if (c && correctAnswers[i])
          result = true
      }
    }
    result
  }
}