package com.sabc.digitalchampions.services;

import com.sabc.digitalchampions.entity.Course;
import com.sabc.digitalchampions.entity.Skills;
import com.sabc.digitalchampions.exceptions.SkillNotFoundException;
import com.sabc.digitalchampions.exceptions.SkillsExistException;
import com.sabc.digitalchampions.repository.SkillsRepository;
import com.sabc.digitalchampions.utils.codegenerator.CodeConfig;
import com.sabc.digitalchampions.utils.codegenerator.CodeConfigBuilder;
import com.sabc.digitalchampions.utils.codegenerator.RbCodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class SkillService {
    private SkillsRepository skillsRepository;

    public SkillService(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    public Skills saveSkill(Skills skills) throws SkillsExistException {
        if (skillsRepository.existsByLabel(skills.getLabel())){
            throw new SkillsExistException();
        }
        CodeConfig codeConfig = (new CodeConfigBuilder())
                .setLength(12)
                .setWithDigits(true).build();
        RbCodeGenerator generator = new RbCodeGenerator(codeConfig);
        skills.setRef(generator.generate());
        return skillsRepository.save(skills);
    }

    public Skills findByRef(String ref) throws SkillNotFoundException {
        if (!skillsRepository.existsByRef(ref)){
            throw new SkillNotFoundException();
        }
        return skillsRepository.findByRef(ref);
    }

    public Page<Skills> findAllSkills(String label, Pageable pageable){
        return skillsRepository.findAll(label, pageable);
    }

    public Skills updateSkills(String ref,Skills skills) throws SkillsExistException, SkillNotFoundException {
        if (!skillsRepository.existsByRef(ref)){
            throw new SkillNotFoundException();
        }
        Skills tmpSkills = skillsRepository.findByRef(ref);
        if (skillsRepository.existsByLabel(ref) && !skills.getLabel().equals(tmpSkills.getLabel())){
            throw new SkillsExistException();
        }
        skills.setLastUpdatedAt(new Date());
        return skillsRepository.save(skills);
    }

    public boolean deleteSkill(String skillRef) throws SkillNotFoundException {
        if (!skillsRepository.existsByRef(skillRef)){
            throw new SkillNotFoundException();
        }
        Skills skills = skillsRepository.findByRef(skillRef);
        skillsRepository.delete(skills);
        return !skillsRepository.existsByRef(skillRef);
    }

    public boolean existsByLabel(String skills) {
        return skillsRepository.existsByLabel(skills);
    }

    public Skills findByLabel(String skillLabel) {
        return skillsRepository.findByLabel(skillLabel);
    }

    public boolean existByRef(String ref) {
        return skillsRepository.existsByRef(ref);
    }
    public List<Skills> findAllByCourse(Course course) {
        return skillsRepository.findByCoursesContaining(course);
    }
}
