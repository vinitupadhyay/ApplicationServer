package troubleshoot.util;

import java.util.Collection;

/**
 * @interface Validator
 * 
 * @description Validate an object and collect all the issues in the collection errors.
 * 
 * @version 1.0.0
 * 
 * @author jrubstein
 * */

public interface Validator
{
   
   /**
    * @description Validate object.
    * @param obj Object to be validated.
    * @param errors Collection of errors.
    * */
   public void validate(Object obj, Collection<Throwable> errors);
   
   /**
    * @description Find out if one class is supported by the validator.
    * @param clazz Class that may be supported.
    * @return true if the class is supported.
    * */
   public boolean supports(Class<?> clazz);
}
