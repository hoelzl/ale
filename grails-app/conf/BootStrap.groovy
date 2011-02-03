import ale.Level
import ale.Exercise
import ale.Answer
import ale.utils.Automata

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

      String preScript =
      """
      public class ConflictException extends Exception {
        public Object old;
        public Object setBy;
        public String name;
        public Object newValue;

        public ConflictException (String name, Object newValue, Object o, Object sb) {
            old = o;
            setBy = sb;
            this.name = name;
            this.newValue = newValue;
        }
      }

      rules = [:]
      currentRule = null


      class Event {
        String name

        static Event getEvent(String n) {
          Event e = new Event()
          e.name = n
          return e
        }

        boolean equals(Object o) {
          if (!(o instanceof Event)) return false
          Event e = (Event) o
          return e.name == this.name
        }

        int hashCode() {
          return name.hashCode()
        }

        String toString() {
          return name
        }
      }

      class EventExpression {
        Event event
        Range mult

        def call(Rule r) {
          return this
        }


        def getAt(int x) {
          mult = x..x
          return this
        }

        def getAt(Range r) {
          mult = r
          return this
        }

        def and(EventExpression e) {
          return new AndExpression(this, e)
        }

        String toString() {
          return event.name + "[" + mult.toString() + "]"
        }

        Set getEventSet() {
          Set s = new HashSet()
          getExpressionList().each { it ->
            s.addAll(it.collect {iter -> iter.event })
          }
          return s
        }

        List getExpressionList() {
          return [this]
        }

        List copy(int[] x) {
          List res = []
          x.each {it -> res << it}
        }

        boolean isAccepted(List index) {
          List expList = getExpressionList()
          for (int i = 0; i < index.size(); i++) {
            EventExpression exp = expList[i]
            int idx = index[i]
            if (!(exp.mult.contains(idx))) return false
          }
          return true
        }

        List generateAcceptedIndexes() {
          List allIndexes = generateIndexes()
          List res = []

          for (index in allIndexes) {
            if (isAccepted(index)) res << index;
          }

          return res
        }

        String generateAccepted(String prefix) {
          String s = "boolean isAccepted() {" +"\\n"
          List acceptedIndexes = generateAcceptedIndexes()
          acceptedIndexes.each {idx ->
             s += "if (" +  idx.inject(prefix) {x, y -> x + "_" + y} + ") return true\\n"
          }

          s += "return false \\n }\\n"

          return s
        }

        List generateIndexes() {
          List expList = getExpressionList()
          List res = []

          int[] a = new int[expList.size()]
          (0..a.length - 1).each {it ->  a[it] = -1}

          int pos = 0
          while (pos >= 0) {
            if (a[pos] < expList[pos].mult.max()) {
              a[pos]++
              pos++
              if (pos >= expList.size()) {
                res.add(copy(a))
                pos--
              }
            } else {
              a[pos] = -1
              pos--
            }
          }
          return res
        }

        boolean isOrigin(idx) {
          for (int i in idx) {
            if (i != 0) return false
          }
          return true
        }

        String generateVariables(String prefix, EnviExp enviExp) {
          List indexes = generateIndexes()
          String s = ""

          indexes.each { it ->
            s += "boolean " + it.inject(prefix) {x, y ->
              x + "_" + y
            }
            s += " = " + isOrigin(it) + "\\n"
          }
          return s
        }


        String generateTransition(String prefix, List smallIdx, List idx, EnviExp enviExp) {
          String smallVarName = smallIdx.inject(prefix) {x, y -> x + "_" + y}
          String varName = idx.inject(prefix) {x, y -> x + "_" + y}
          String s = "if (" + smallVarName + ") " +
                  "{ " + varName + " = true; " +
                  smallVarName + " = false; }"
          return s
        }

        String generateWrongTransition(String prefix, List idx, EnviExp enviExp) {
          String varName = idx.inject(prefix) {x, y -> x + "_" + y}
          String s = "if (" + varName + ") " + varName + " = false"
        }

        boolean containedInList(List expList, Event ev) {
          for (EventExpression exp in expList) {
            if (exp.event.equals(ev)) return true
          }
          return false
        }

        int getEventPosition(Event ev) {
          List expressionList = getExpressionList()
          int idx = -1
          for (i in 0..expressionList.size() - 1) {
            if (expressionList[i].event.equals(ev)) {
              idx = i
              break
            }
          }
          assert idx >= 0 && idx < expressionList.size()
          return idx
        }

        String setOriginTrue(String prefix, EnviExp enviExp) {
          List expList = getExpressionList()
          int[] a = new int[expList.size()]
          a.each { it = 0 }
          String varName = a.inject(prefix) {x, y -> x + "_" + y}
          return varName + " = true\\n"
        }

        String doEvent(String prefix, Event ev, EnviExp enviExp) {
          List indexes = generateIndexes()
          String s = ""

          List expressionList = getExpressionList()
          if (!(containedInList(expressionList, ev))) return ""
          int eventPos = getEventPosition(ev)
          EventExpression exp = expressionList[eventPos]

          if (exp.mult.max() >= 1) {

            for (idx in indexes) {
                if (idx[eventPos] == exp.mult.max()) {
                  s += generateWrongTransition(prefix, idx, enviExp) + "\\n"
                }
            }

            for (num in exp.mult.max()..1) {
              for (idx in indexes) {
                if (idx[eventPos] == num) {
                  List smallIdx = new ArrayList()
                  idx.each {it -> smallIdx << it}
                  smallIdx[eventPos] = smallIdx[eventPos] - 1
                  s += generateTransition(prefix, smallIdx, idx, enviExp) + "\\n"
                }
              }
            }
          } else {
            for (idx in indexes) {
              if (idx[eventPos] == 0) {
                s += generateWrongTransition(prefix, idx, enviExp) + "\\n"
              }
            }
          }

          return s
        }
      }

      class AndExpression extends EventExpression {
        EventExpression left, right

        AndExpression(l, r) {
          left = l; right = r
        }

        String toString() {
          return "(" + left.toString() + " and " + right.toString() + ")"
        }

        List getExpressionList() {
          List a = left.getExpressionList()
          List b = right.getExpressionList()
          a.addAll(b)
          return a
        }
      }

      class EnviOperator {
        private int type
        static final int EQUALSTYPE = 1

        static final EnviOperator EQUALS = new EnviOperator(type: EQUALSTYPE)

        String toString() {
          if (type == EQUALSTYPE) {
            return "e"
          }
        }

        String toJava() {
          if (type == EQUALSTYPE) {
            return "=="
          }
        }
      }

      class EnviExp {
        String name
        EnviOperator operator
        def value = null

        def propertyMissing(String name) {
          this.name = name
          return this
        }

        def is(args) {
          // println "is: "+args
          this.operator = EnviOperator.EQUALS
          this.value = args
          return this
        }

        String toVarName() {
          return name + "_" + operator.toString() + "_" + value.toString()
        }

        String toJava() {
          return " && (" + name + operator.toJava() + value.toString() + ")"
        }
      }

      class Rule {
        String name
        EventExpression condition
        Closure effect
        Closure constraint

        def when(Closure c) {
          condition = c()
          return this
        }

        def whereas(Closure c) {
          this.constraint = c
          return this
        }

        def call(Closure c) {
          return this
        }

        def then(Closure c) {
          effect = c
        }

        def parse(Closure c) {
          c.delegate = this
          c()
        }

        def propertyMissing(String name) {
          if (name == "env") {
            EnviExp exp = new EnviExp()
            exp.name = name
            return exp
          } else {
            Event e = Event.getEvent(name)
            EventExpression exp = new EventExpression()
            exp.event = e
            return exp
          }
        }

        def doEvent(Event ev) {
          EnviExp enviExp = getEnvironmentVars()
          return "def on_"+ev.name+"() {\\n"+condition.doEvent(name, ev, enviExp) \
           + setOriginTrue() +"\\n print('Running Event.\\\\n')}\\n"
        }

        def getEnvironmentVars() {
          // println "getting environment vars: " + this.name
          Closure c = constraint
          return (c != null) ? c() : null
        }

        boolean isConstraintSatisfied(Binding bd) {
          if (constraint == null)
            return true
          else {
            // TODO: this is only a quick hack!
            EnviExp enviExp = constraint()
            return bd.getVariable(enviExp.name).equals(enviExp.value)
          }
        }

        String setOriginTrue() {
          return condition.setOriginTrue(this.name, this.getEnvironmentVars())
        }

        String toString() {
          return name + ": [condition: " + condition.toString() + ", effect: " + effect.toString() + ", constraint: " + constraint.toString() + "]\\n"
        }
      }

      def rule(args) {
        rules[args.name] = args
      }

      def generateVariables(Rule rule) {
        EventExpression exp = rule.condition
        // exp.generateDos(rule.name)
        exp.generateVariables(rule.name, rule.getEnvironmentVars())
      }

      def methodMissing(String name, args) {
        Rule rule = new Rule()
        rule.name = name
        rule.parse args[0]
        return rule
      }
      """


      String ruleScript =
      """
      rule hist2 {
        when { rightAnswer[0..3](whereas {env.l.is(2)})} then { y =5; if (y == 5) x = 1 }
      }

      rule hist3 {
        when { rightAnswer[2..3](whereas {env.l.is(3)})} then { x = 2 }
      }

      rule low {
        when { rightAnswer[0..3](whereas {env.l.is(2)})} then { x = 0 }
      }

      rule ad {
        when { rightAnswer[3] & wrongAnswer[0] & t[5]} then { x = 1 }
      }
      """

      String setPropertyScript =
      """

      setBy = [:]

      void doSetProperty(String name, Object value) {
          super.setProperty (name, value)
          try {
            setBy[name] = currentRule
          } catch (MissingPropertyException) {
          }
      }

      void setProperty(String name, Object value) {
          if ("currentRule".equals(name)) {
            super.setProperty(name, value)
            return
          }

          def old
          try {
              old = getProperty(name)
          } catch (MissingPropertyException) {
              doSetProperty(name, value)
              return
          }

          if (old == null && value == null) {
              doSetProperty(name, value)
              return
          }

          if (old == null ) { // value != null!
              throw new ConflictException(name, value, old, setBy[name])
          }

          // now: old != null
          if (!(old.equals(value))) {
              throw new ConflictException(name, value , old, setBy[name])
          }
          super.setProperty (name, value)
      }
      """

      String postScript =
      """
      rule2Automata = [:]

      rules.each {it ->
        Rule rule = it.value

        String classAutomata = "def rule\\n"

        String s = generateVariables(rule)
        classAutomata += s

        def eventSet = rule.condition.getEventSet()

        for (ev in eventSet) {
          s = rule.doEvent(ev)
          classAutomata += s
        }

        classAutomata += rule.condition.generateAccepted(rule.name)
        classAutomata += "def methodMissing(String name, args){println: 'missing: ' + name}\\n"

        String className = "Automata" +rule.name
        classAutomata = "class "+className+ "{\\n" + classAutomata + "}\\n"
        classAutomata += "new " + className +"()"

        EnviExp enviExp = rule.getEnvironmentVars()
        String suffix = ""
        if (enviExp != null) suffix = "_" + enviExp.toVarName()
        String key = rule.name + suffix

        def am = new GroovyShell().evaluate(classAutomata)
        am.rule = it.getValue()
        rule2Automata[key] = am

        // println classAutomata
      }
      """

      String script = preScript + ruleScript + setPropertyScript + postScript

      def shell = new GroovyShell()
      shell.evaluate (script)

//println shell.rules
//println shell.rule2Automata
//println shell.properties

      def automata = shell.rule2Automata.values()
      Automata.automata = automata

    }
    def destroy = {
    }
}
