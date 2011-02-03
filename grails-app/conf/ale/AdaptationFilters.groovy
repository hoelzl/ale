package ale

import ale.utils.Automata

class AdaptationFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
              def automata = Automata.automata
              for (automaton in automata) {
                print("Running ${automaton}.on_${params.action}().\n")
                automaton."on_${params.action}"()
              }
            }
            after = {
                
            }
            afterView = {
                
            }
        }
    }
    
}
