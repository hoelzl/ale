package ale

class LoggingFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                log.debug("Parameters: ${params.inspect()}.")
            }
            after = {
                
            }
            afterView = {
                
            }
        }
    }
    
}
