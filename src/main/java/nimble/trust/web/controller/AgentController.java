package nimble.trust.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nimble.trust.engine.domain.Agent;
import nimble.trust.engine.repository.AgentRepository;

@RestController
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    @GetMapping("/agents")
    public Page<Agent> getQuestions(Pageable pageable) {
        return agentRepository.findAll(pageable);
    }


    @PostMapping("/agents")
    public Agent createQuestion(@Valid @RequestBody Agent agent) {
        return agentRepository.save(agent);
    }
    
}