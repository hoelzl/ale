package ale

import ale.utils.UserChoice

/**
 * The service that performs most of the real work for ALE.
 *
 * The methods follow the convention that they return a list or a map in the case of success
 * and null in the case of an error.  This simplifies the controller somewhat.
 */
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
      null
    }
  }

  def returnExerciseInfo(exercise) {
    if (exercise) [
            success: true,
            result: exercise.getInfo()
    ]
    else
      null
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

  def setCurrentExercise(Exercise exercise) {
    if (exercise) {
      currentExerciseId = exercise.id
      exerciseAnswered = false
      returnExerciseInfo(exercise)
    }
    else
      null
  }

  def setCurrentExercise(int exerciseId) {
    // We need to check that there actually is an exercise with the given ID, therfore
    // we can't set currentExerciseId directly.
    def exercise = Exercise.findById(exerciseId)
    // Don't call the method on Exercise when exercise == null since it is not clear that
    // this call won't dispatch back here again (AFAIK int = Integer and hence can be null).
    if (exercise && exercise.level.number == currentLevelNumber)
      setCurrentExercise(exercise)
    else
      null
  }

  def nextExercise() {
    Level level = Level.findByNumber(currentLevelNumber)
    setCurrentExercise(level.randomExercise())
  }

  Boolean answerCurrentExercise(UserChoice choice) {
    exerciseAnswered = true
    def currentExercise = Exercise.findById(currentExerciseId)
    if (currentExercise)
      currentExercise.validateUserChoice(choice)
    else
      null
  }

  def returnAnswer(Boolean value) {
    def exercise = Exercise.findById(currentExerciseId)
    if (exercise) {[
            correctAnswer: value,
            answerInfo: [
                    class: exercise.getClass(),
                    exerciseId: exercise.id,
                    question: exercise.question,
                    answers: exercise.correctAnswersAsText()
            ]
    ]}
    else
      null
  }


  def returnRightAnswer() {
    returnAnswer(true)
  }

  def returnWrongAnswer() {
    returnAnswer(false)
  }

  def showAutomata(context) {
    def automata = context["automata"]
    if (automata) {[
            automata: automata
    ]}
    else
      null
  }
}
