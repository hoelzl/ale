package ale

import ale.utils.UserChoice

class AleService {

  static transactional = true

  // We cannot store the current level directly, since this leads to Hibernate transaction errors
  // when accessing attributes of the level in calls to service methods.
  int currentLevelNumber = 1
  int currentExerciseId = -1
  Boolean exerciseAnswered = false

  def listLevels() {
    Level.list().collect { it.getInfo() }
  }

  def getCurrentLevel() {
    Level.findByNumber(currentLevelNumber)?.getInfo()
  }

  def setCurrentLevel(int levelNumber) {
    // Only set the current level if a level with that number actually exists.
    Level level = Level.findByNumber(levelNumber)
    if (level) {
      currentLevelNumber = level.number
      nextExercise()
      getCurrentLevel()
    }
    else {
      false
    }
  }

  def returnExerciseInfo(exercise) {
    if (exercise) [
            success: true,
            result: exercise.getInfo()
    ]
    else [
            success: false,
            errors: [
                    exercise: "No current exercise."
            ]
    ]
  }

  def getCurrentExercise() {
    def exercise = Exercise.findById(currentExerciseId)
    if (exercise) {
      returnExerciseInfo(exercise)
    }
    else {
      nextExercise()
    }
  }

  def nextExercise() {
    Level level = Level.findByNumber(currentLevelNumber)
    def exercise = level.randomExercise()
    currentExerciseId = exercise.id
    exerciseAnswered = false
    returnExerciseInfo(exercise)
  }

  Boolean answerCurrentExercise(UserChoice choice) {
    exerciseAnswered = true
    def currentExercise = Exercise.findById(currentExerciseId)
    if (currentExercise)
      currentExercise.validateUserChoice(choice)
    else
      false
  }

  def returnAnswer(Boolean value) {
    def exercise = Exercise.findById(currentExerciseId)
    if (exercise) {[
            success: true,
            result: value,
            info: [
                    class: exercise.getClass(),
                    exerciseId: exercise.id,
                    question: exercise.question,
                    answers: exercise.correctAnswersAsText() ]
    ]}
    else {[
            success: false,
            errors: [
                    exercise: "No current exercise."
            ]]
    }
  }


  def returnRightAnswer() {
    returnAnswer(true)
  }

  def returnWrongAnswer() {
    returnAnswer(false)
  }
}
