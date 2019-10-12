package com.evapps.chargepoints.controller;

import com.evapps.chargepoints.model.Session;
import com.evapps.chargepoints.repo.SessionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( path = "/api/v1/session" )
public class ApiController {

    private static final Logger log = LogManager.getLogger(ApiController.class);

    @RequestMapping( path = "/test", method = RequestMethod.GET )
    public Message getMessage() {
        return new Message("This is the message from the service!");
    }

    class Message {
        private final String message;

        public Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    @Autowired
    SessionRepository repository;

    @RequestMapping( path = "/all", method = RequestMethod.GET)
    public List<Session> getAllSessions() { return repository.findAll(); }

    @RequestMapping( path = "/{sessionId}", method = RequestMethod.GET)
    public Session getSessionById(@PathVariable("sessionId") long sessionId) { return repository.getOne(sessionId); }

    @RequestMapping( method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveSessionInfo( @RequestBody Session session ) {
        System.out.println("saveSessionInfo() -->");
        repository.save(session);
        log.debug("saveSessionInfo() <--");
    }

//    @RequestMapping( path = "/{session}", method = RequestMethod.DELETE)
//    public void deleteSession( @PathVariable("session") Session session ) { repository.delete(session); }

    @RequestMapping( path = "/{sessionId}", method = RequestMethod.DELETE)
    public void deleteSessionById( @PathVariable("sessionId") long sessionId) { repository.deleteById(sessionId);}
}
