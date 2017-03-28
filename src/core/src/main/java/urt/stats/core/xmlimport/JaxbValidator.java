package urt.stats.core.xmlimport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;

import org.apache.log4j.Logger;

/**
 * JaxbValidator
 * 
 * @author ghost
 *
 */
public class JaxbValidator extends ValidationEventCollector {

        /**
         * Validation error list
         */
        private List<JaxbError> errorList = new ArrayList<JaxbError>() ;
        
        public boolean handleEvent(ValidationEvent event) {
            if (event.getSeverity() == ValidationEvent.ERROR || event.getSeverity() == ValidationEvent.FATAL_ERROR){
                ValidationEventLocator locator = event.getLocator();
                JaxbError error = new JaxbError(
                        locator.getLineNumber()
                      , locator.getColumnNumber()
                      , event.getMessage()
                );
                errorList.add(error);
            }
            return true;
        }

        /**
         * @return true if imported xml is valid and false if not
         */
        public boolean isValid() {
            return errorList.isEmpty();
        }

        /**
         * @return {@link List <JaxbError>}
         */
        public  List <JaxbError>  getErrors() {
            return errorList;
        }
        
        /**
         * Prints validation errors to logger
         * 
         * @param logger {@link Logger} logger
         */
        public void printErrors(Logger logger){
            if(!errorList.isEmpty()){
                StringBuilder sb = new StringBuilder();
                sb.append("There are " + errorList.size() + " errors: \n");
                for(JaxbError error : errorList){
                    sb.append(error.toString());
                    sb.append("\n");
                }
                logger.error(sb.toString());
            }
        }
}
