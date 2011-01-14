package ale.utils

/**
 * Created by IntelliJ IDEA.
 * User: tc
 * Date: 1/14/11
 * Time: 5:07 PM
 */
public enum ExerciseType {
  SINGLE_ANSWER("Single Answer") {
    boolean validateUserChoice(UserChoice choice, List<Boolean> correctAnswers) {
      if (choice.numberOfAnswers() != 1)
        false
      else {
        def result = true
        choice.selectedAnswers.eachWithIndex {c, i ->
          if (c && !correctAnswers[i])
            result = false
        }
        result
      }
    }
  },
  ALL_ANSWERS("All Answers") {
    boolean validateUserChoice(UserChoice choice, List<Boolean> correctAnswers) {
      def result = true
      choice.selectedAnswers.eachWithIndex {c, i ->
        if (c != correctAnswers[i])
        result = false
      }
      result
    }
  },
  ANY_ANSWER("Any Answer") {
    boolean validateUserChoice(UserChoice choice, List<Boolean> correctAnswers) {
      def result = false
      choice.selectedAnswers.eachWithIndex {c, i ->
        if (c && correctAnswers[i])
          result = true
      }
      result
    }
  };

  ExerciseType(String name) {
    this.name = name
  }

  String name
  String toString() {
    name
  }
}