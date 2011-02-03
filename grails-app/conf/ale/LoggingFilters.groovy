package ale

class LoggingFilters {

  def filters = {
    all(controller:'*', action:'*') {
      before = {
        print("Controller: ${params.controller}; Action: ${params.action}.\n")
        print("  Parameters: ${params.inspect()}.\n")
        print("  Automata: ${ale.utils.Automata.automata}.\n")
      }
      after = {

      }
      afterView = {

      }
    }
  }
    
}
