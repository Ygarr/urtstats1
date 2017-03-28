package urt.stats.core.xmlimport;

import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;

import urt.stats.core.xmlimport.exception.JaxbUnmarshallException;

/**
 * jaxb marshall/unmarshall handler
 * @author ghost
 *
 * @param <T> result object type
 */
public class JaxbHandler<T> {

     /**
      * Logger
      */
     protected Logger logger = Logger.getLogger(getClass());

     /**
      * Validator
      */
     protected JaxbValidator validator = new JaxbValidator();
     
     /**
      * XSD schema URL
      */
     private URL schemaURL;
     
     /**
      * Constructor
      * 
      * @param schemaURL {@link URL} xsd schema URL
      */
     public JaxbHandler(URL schemaURL) {
         this.schemaURL = schemaURL;
     }
      
     /**
      * Returns validator
      * @return validator {@link JaxbValidator}
      */
     public JaxbValidator getValidator(){
         return validator; 
     }

     /**
      * Unmarshall XML to Object
      * @param is {@link InputStream} XML input stream
      * @param objectClass {@link Class} result object class
      * 
      * @return unmarshalled XML as object
      * 
     * @throws JaxbUnmarshallException on failure 
      */
     public T unmarshallXML(InputStream is, Class<T> objectClass) throws JaxbUnmarshallException {
         T result = null;
         validator.getErrors().clear();
         try{ 
             SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
             Schema schema = schemaFactory.newSchema(schemaURL);
             XMLInputFactory inputFactory = XMLInputFactory.newInstance();
             XMLStreamReader streamReader = inputFactory.createXMLStreamReader(is);
             JAXBContext context = JAXBContext.newInstance(objectClass);
             Unmarshaller unmarshaller = context.createUnmarshaller();
             unmarshaller.setSchema(schema);
             unmarshaller.setEventHandler(validator);
             result = (T)unmarshaller.unmarshal(streamReader);
         } catch(Exception e){
             throw new JaxbUnmarshallException("Failed to unmarshall XML", e);
         }
         return result;
     }

}
