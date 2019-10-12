package com.evapps.chargepoints;

import com.evapps.chargepoints.model.Session;
import com.evapps.chargepoints.repo.SessionRepository;
import com.evapps.chargepoints.service.XmlService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {
    private static final Logger log = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CommandLineRunner run(SessionRepository repository) {
        return (args) -> {
            String URL = "https://www.esb.ie/electric-cars/kml/charging-locations.kml";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            XmlService parse = new XmlService();

//			StationInfo st = ctrlr.getSessionById(831);
//			System.out.println( st.toString() );

            //System.exit(0);

//            log.info("-------------------------------");
//            log.info("Finding all sessions");
//            log.info("-------------------------------");
//            for (Session session: repository.findAll()) {
//                System.out.println( session.toString() );
//            }

            for(;;)
            {
                doc = builder.parse(URL);
                // normalize XML response
                doc.getDocumentElement().normalize();

                // Read the station locations
                NodeList nodeList = doc.getElementsByTagName("Placemark");

                //loop all available student nodes
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) node;
                        String name = elem.getElementsByTagName("name").item(0).getTextContent();
                        String description = elem.getElementsByTagName("description").item(0).getTextContent();

                        parse.isWatchedLocation(name, description, repository);
                    }
                }
                parse.printLocationInfo();

                TimeUnit.MINUTES.sleep(1);
            }
        };
    }

}