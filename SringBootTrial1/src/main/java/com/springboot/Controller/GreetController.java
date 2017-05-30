package com.springboot.Controller;

import com.springboot.Geeting;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by san6685 on 11/15/2016.
 */

@RestController
public class GreetController {

    private static BigInteger nextId;
    private static Map<BigInteger,Geeting> greetingMap;
    private static Geeting save(Geeting geeting){
        if(greetingMap == null){
            greetingMap = new HashMap<BigInteger, Geeting>();
            nextId = BigInteger.ONE;
        }
        geeting.setId(nextId);
        nextId = nextId.add(BigInteger.ONE);
        greetingMap.put(geeting.getId(),geeting);
        return geeting;

    }

    static {
        Geeting g1 = new Geeting();
        g1.setMessage("Heello");
        save(g1);

        Geeting g2 = new Geeting();
        g1.setMessage("World");
        save(g2);

    }

    @RequestMapping(value = "/greeting",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Geeting>> getGreeting(){
        Collection<Geeting> geetings = greetingMap.values();
        return new ResponseEntity<Collection<Geeting>>(geetings, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/greeting/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Geeting> getGreetingById(@PathVariable("id") BigInteger id){
            Geeting geeting = greetingMap.get(id);
            if(geeting == null){
               return new ResponseEntity<Geeting>(HttpStatus.NOT_FOUND);
            }
        return new ResponseEntity<Geeting>(geeting,HttpStatus.OK);
    }


    @RequestMapping(
            value = "/greeting/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Geeting> createGreeting(@RequestBody  Geeting geeting){
            Geeting saveGeetings = save(geeting);
            return new ResponseEntity<Geeting>(geeting,HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/greeting/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Geeting> updateGreeting(@RequestBody  Geeting geeting){
        Geeting updatesGeetings = save(geeting);
        if(updatesGeetings==null){
            return new ResponseEntity<Geeting>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity<Geeting>(geeting,HttpStatus.OK);
        }
    }
}
