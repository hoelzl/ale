package ale

class Answer implements Comparable {

  static belongsTo = [exercise: Exercise]

  // True if this is (part of) a correct answer; false otherwise.
  boolean isCorrect
  String text

  static constraints = {
    exercise(nullable: false)
    text(nullable: false, blank: false)
    isCorrect()
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
}
