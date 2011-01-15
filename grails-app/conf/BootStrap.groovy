import ale.Level
import ale.Exercise
import ale.Answer

class BootStrap {

    def init = { servletContext ->

      if (!Level.count()) {
        
        def l1 = new Level(number: 1, name: "Beginner").save(failOnError: true)
        def l2 = new Level(number: 2, name: "Intermediate 1").save(failOnError: true)
        def l3 = new Level(number: 3, name: "Intermediate 2").save(failOnError: true)
        def l4 = new Level(number: 4, name: "Advanced").save(failOnError: true)

        def e11 = new Exercise(level: l1, question: "Question 1.1").save(failOnError: true)
        def e12 = new Exercise(level: l1, question: "Question 1.2").save(failOnError: true)
        def e13 = new Exercise(level: l1, question: "Question 1.3").save(failOnError: true)
        def e21 = new Exercise(level: l2, question: "Question 2.1").save(failOnError: true)
        def e22 = new Exercise(level: l2, question: "Question 2.2").save(failOnError: true)
        def e23 = new Exercise(level: l2, question: "Question 2.3").save(failOnError: true)
        def e24 = new Exercise(level: l2, question: "Question 2.4").save(failOnError: true)
        def e31 = new Exercise(level: l3, question: "Question 3.1").save(failOnError: true)
        def e32 = new Exercise(level: l3, question: "Question 3.2").save(failOnError: true)
        def e41 = new Exercise(level: l4, question: "Question 4.1").save(failOnError: true)
        def e42 = new Exercise(level: l4, question: "Question 4.2").save(failOnError: true)

        new Answer(exercise: e11, text: "Answer 1.1.1", isCorrect: true).save(failOnError: true)
        new Answer(exercise: e11, text: "Answer 1.1.2", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e11, text: "Answer 1.1.3", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e11, text: "Answer 1.1.4", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e11, text: "Answer 1.1.5", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e12, text: "Answer 1.2.1", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e12, text: "Answer 1.2.2", isCorrect: true).save(failOnError: true)
        new Answer(exercise: e12, text: "Answer 1.2.3", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e13, text: "Answer 1.3.1", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e13, text: "Answer 1.3.2", isCorrect: false).save(failOnError: true)
        new Answer(exercise: e13, text: "Answer 1.3.3", isCorrect: true).save(failOnError: true)
      }
    }
    def destroy = {
    }
}
