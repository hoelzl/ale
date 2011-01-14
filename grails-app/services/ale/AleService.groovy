package ale

import ale.utils.UserChoice

class AleService {

  static transactional = true

  // We cannot store the current level directly, since this leads to Hibernate transaction errors
  // when accessing attributes of the level in calls to service methods.
  int currentLevelNumber = 1
  int currentExerciseId
  Boolean exerciseAnswered = false

  def listLevels() {
    Level.list()
  }

  Boolean setLevel(int levelNumber) {
    // Only set the current level if a level with that number actually exists.
    Level level = Level.findByNumber(levelNumber)
    if (level) {
      currentLevelNumber = level.number
      true
    }
    else {
      false
    }
  }

  Exercise getNextExercise() {
    Level level = Level.findByNumber(currentLevelNumber)
    Exercise currentExercise = level.randomExercise()
    currentExerciseId = currentExercise.id
    exerciseAnswered = false
    currentExercise
  }

  Boolean answerCurrentExercise(UserChoice choice) {
    exerciseAnswered = true
    def currentExercise = Exercise.findById(currentExerciseId)
    currentExercise.validateUserChoice(choice)
  }

}
