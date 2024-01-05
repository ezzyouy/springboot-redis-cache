package org.redis.springbootrediscache.web;

import lombok.AllArgsConstructor;
import org.redis.springbootrediscache.entities.Employe;
import org.redis.springbootrediscache.repositories.EmployeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class EmployeController {

    private EmployeRepository employeRepository;

    @GetMapping("/employees")
    public ResponseEntity<List<Employe>> getAllEmploye(){
        return ResponseEntity.ok(employeRepository.findAll());
    }
    @GetMapping("/employees/{id}")
    @Cacheable(value = "employees", key = "#id")
    public Employe getEmploye(@PathVariable Integer id){
        return employeRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Employee not found %s", id)));
    }

    @PostMapping("/employees")
    public Employe addEmploye(@RequestBody Employe e){
        return employeRepository.save(e);
    }
    @PutMapping("/employee/{id}")
    @CachePut(value = "employees", key = "#id")
    public Employe updateEmploye(@PathVariable Integer id, @RequestBody Employe e){
        Employe employe= employeRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Employee not found %s", id)));
        employe.setName(e.getName());
        return employeRepository.save(employe);
    }
    @DeleteMapping("/employee/{id}")
    @CacheEvict(value = "employees", allEntries = true)
    public void deleteEmploye(@PathVariable Integer id){
        Employe employe= employeRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Employee not found %s", id)));
         employeRepository.delete(employe);
    }
}
