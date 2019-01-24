package softLaunch.service;

import org.springframework.http.HttpStatus;
import softLaunch.domain.RequestWrapper;
import softLaunch.domain.WhiteList;
import softLaunch.exceptionHandler.ClientNotFoundException;
import softLaunch.repository.WhiteListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WhiteListService {

    @Autowired
    private WhiteListRepository repository;

    public List<WhiteList> getAllWhiteLists() {
        return repository.findAll();
    }

    public ResponseEntity<?> deleteWhiteList() {
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean exists(WhiteList c)  {
         if(!repository.findByCpf(c.getCpf()).isPresent())
             throw new ClientNotFoundException();
         else return true;
    }

    public WhiteList addWhiteList(WhiteList whiteList) {
        return repository.save(whiteList);
    }

    public ResponseEntity<RequestWrapper> addBatch(RequestWrapper requestWrapper) {
        requestWrapper.getWhiteLists().stream()
                .forEach(c-> this.addWhiteList(new WhiteList(c.getName(),c.getCpf())));

        return new ResponseEntity<RequestWrapper>(requestWrapper, HttpStatus.CREATED);
    }
}