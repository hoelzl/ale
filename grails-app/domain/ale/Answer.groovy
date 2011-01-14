package ale

class Answer {

  static belongsTo = [exercise: Exercise]

  // True if this is (part of) a correct answer; false otherwise.
  boolean isCorrect
  String text

  static constraints = {
    exercise(nullable: false)
    text(nullable: false, blank: false)
    isCorrect()
  }
}
