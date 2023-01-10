package com.example.hw3_4.school.Controllr;


import com.example.hw3_4.school.Model.Faculty;
import com.example.hw3_4.school.Model.Student;
import com.example.hw3_4.school.Service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student student1 = studentService.editStudent(student);
        if (student1 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student1);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("students/all")
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping(params = {"min", "max"})
    public ResponseEntity<Collection<Student>> findStudentByAgeIsBetween(@RequestParam int min, @RequestParam int max) {
        return ResponseEntity.ok(studentService.findStudentByAgeIsBetween(min, max));
    }

       @GetMapping("/{id}/faculty")
       public ResponseEntity<Faculty> findStudentByFaculty(@PathVariable Long id) {
           return ResponseEntity.ok(studentService.findStudentByFaculty(id));
       }

       @GetMapping("/students/name/{name}")
    public List<Student> getStudentsByName(@PathVariable("name") String name){
        return studentService.getStudentsByName(name);
       }

       @GetMapping("/students/name/A")
    public List<String> getStudentsNameA(){
        return studentService.getStudentsByNameA();
    }

    @GetMapping("/students/mid/age")
    public Double getStudentsMidAge(){
        return studentService.getStudentsMidlAge();
    }

    @GetMapping("students/name/stream")
    public void getStudentsNameStream(){
        studentService.getThreadOne();
    }

    @GetMapping("students/name/stream/synchronized")
    public void getStudentsNameStreamSynchronized(){
        studentService.getThreadSynchronized();
    }
}
