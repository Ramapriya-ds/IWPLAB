package com.example.aopdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AopDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AopDemoApplication.class, args);
    }
}

Service Class
package com.example.aopdemo.service;

import org.springframework.stereotype.Service;

@Service
public class StudentService {

    public String getStudentById(int id) {
        return "Student{id=" + id + ", name='John Doe'}";
    }

    public void registerStudent(String name) {
        System.out.println("Registering student: " + name);
    }
}

Aspect Class for Logging
package com.example.aopdemo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.example.aopdemo.service.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        System.out.println("👉 Executing method: " + methodName);
        System.out.println("📥 Arguments: ");
        for (Object arg : args) {
            System.out.println("   - " + arg);
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // Proceed with actual method call
        long endTime = System.currentTimeMillis();

        System.out.println("✅ Method " + methodName + " executed successfully");
        System.out.println("📤 Return Value: " + result);
        System.out.println("⏱ Execution Time: " + (endTime - startTime) + " ms");

        return result;
    }
}

Controller to Test
package com.example.aopdemo.controller;

import com.example.aopdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public String getStudent(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    @PostMapping("/register")
    public String registerStudent(@RequestParam String name) {
        studentService.registerStudent(name);
        return "Student registered successfully!";
    }
}