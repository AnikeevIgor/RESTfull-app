package com.example.hw3_4.school.Service;


import com.example.hw3_4.school.Model.Faculty;
import com.example.hw3_4.school.Model.Student;
import com.example.hw3_4.school.repositories.FacultyRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FacultyService {
    private final FacultyRepo facultyRepo;

   private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepo facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.debug("Was invoked method for create faculty");
        return facultyRepo.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.debug("Was invoked method for find faculty");
        return facultyRepo.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.debug("Was invoked method for edit faculty");
        return facultyRepo.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.debug("Was invoked method for delete faculty");
        facultyRepo.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        logger.debug("Was invoked method for getAllFaculty");
        return facultyRepo.findAll();
    }
/* поиска факультета по имени или цвету, игнорируя регистр */
    public Collection<Faculty> findFacultiesByColorIgnoreCaseOrNameIgnoreCase(String colorOrName) {
        logger.debug("Was invoked method for findFacultiesByColorIgnoreCaseOrNameIgnoreCase");
        return facultyRepo.findFacultiesByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }
/* Получить студентов факультета */
    public Collection<Student> getStudentByFaculty(Long id) {
        logger.debug("Was invoked method for getStudentByFaculty");
        return findFaculty(id).getStudents();
    }
    /* Поиск по названию и цвету факультета */
    public List<Faculty> getFacultiesByNameAndColor(String name, String color) {
        logger.debug("Was invoked method for getFacultiesByNameAndColor");
        return facultyRepo.getFacultiesByNameAndColor(name, color);
    }
/* Возвращать самое длинное название факультета */
    public String getFacultyLongName() {
        List<Faculty> faculties = new ArrayList<>(facultyRepo.findAll());
        return faculties.stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                        .get();

    }

    public Integer getParallel() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b );
    }
}
