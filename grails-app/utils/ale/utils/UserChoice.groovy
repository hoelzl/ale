package ale.utils

/**
 * Created by IntelliJ IDEA.
 * User: tc
 * Date: 1/14/11
 * Time: 5:07 PM
 */
class UserChoice {
  List<Boolean> selectedAnswers = []

  int numberOfAnswers() {
    selectedAnswers.count(true)
  }
}
