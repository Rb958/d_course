package com.sabc.digitalchampions.controller;

import com.sabc.digitalchampions.entity.Skills;
import com.sabc.digitalchampions.exceptions.AbstractException;
import com.sabc.digitalchampions.exceptions.SkillNotFoundException;
import com.sabc.digitalchampions.security.payload.response.ResponseException;
import com.sabc.digitalchampions.security.payload.response.ResponseModel;
import com.sabc.digitalchampions.services.SkillService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SkillsController {
    private SkillService skillService;

    private Logger logger = LogManager.getLogger(SkillsController.class);

    public SkillsController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skill")
    public ResponseEntity<?> saveSkill(@RequestBody @Valid Skills skills){
        try{
            skills.checkEntity();
            return ResponseEntity.ok(new ResponseModel<>(skillService.saveSkill(skills)));
        }catch (AbstractException e){
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to save this skill. Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @GetMapping("/skill")
    public ResponseEntity<?> getSkills(@RequestParam Optional<String> label){
        System.out.println(label);
        return ResponseEntity.ok().body(
                new ResponseModel<>(skillService.findAllSkills(label.orElse(""), Pageable.unpaged()))
        );
    }

    @GetMapping("/skill/{ref}")
    public ResponseEntity<?> getSkillByRef(@PathVariable(name = "ref") String ref){
        try{
            return ResponseEntity.ok().body(
                    new ResponseModel<>(
                            skillService.findByRef(ref)
                    )
            );
        }catch (AbstractException e){
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to get skill with reference " + ref + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PutMapping("/skill/{ref}")
    public ResponseEntity<?> updateSkill(@PathVariable(name = "ref") String ref, @RequestBody @Valid Skills skills){
        try{
            skills.checkEntity();
            return ResponseEntity.ok().body(
                    new ResponseModel<>(
                            skillService.updateSkills(ref,skills)
                    )
            );
        }catch (AbstractException e){
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to update the skill with reference " + skills.getRef() + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @DeleteMapping("/skill/{ref}")
    public ResponseEntity<?> deleteSkill(@PathVariable(name = "ref") String ref){
        try {
            boolean result = skillService.deleteSkill(ref);
            if (result){
                return ResponseEntity.ok().body(
                    new ResponseModel<>("Successfully Deleted", HttpStatus.OK)
                );
            }else{
                return ResponseEntity.status(500).body(
                        new ResponseModel<>("Error the skill was not deleted",HttpStatus.INTERNAL_SERVER_ERROR)
                );
            }
        }catch (AbstractException e){
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to delete the skill with reference " + ref + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }
}
