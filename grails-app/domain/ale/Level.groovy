package ale

class Level {

  static hasMany = [exercises: Exercise]

  // Levels are numbered consecutively from 0 to the highest level.
  int number
  String name
  SortedSet exercises
  Random random = new Random()

  def getInfo(){[
          class: getClass(),
          number: number,
          name: name
  ]}
  
  Exercise randomExercise() {
    def n = exercises.size()
    assert n > 0 : "Exercises are empty?"
    exercises.toArray()[random.nextInt(n)] as Exercise
  }

  String toString() {
    "$name (Level $number)"
  }

  static constraints = {
    number(unique: true)
    name(nullable: false)
  }
}
