package ale

class Level {

  static hasMany = [exercises: Exercise]

  // Levels are numbered consecutively from 0 to the highest level.
  int number
  String name
  Random random = new Random()

  Exercise randomExercise() {
    def n = exercises.size()
    assert n > 0 : "Exercises are empty?"
    exercises.toArray()[random.nextInt(n)]
  }

  String toString() {
    "$name (Level $number)"
  }

  static constraints = {
    number(unique: true)
    name(nullable: false)
  }
}
