package com.apress.hibernaterecipes.chapter2.recipe2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(EmployeeId.class)
public class Employee {
    @Id
    Long department;
    @Id
    Long idCard;
    @Column(unique = true, nullable = false)
    String name;

    public Employee(Long department, Long idCard, String name) {
        this.department = department;
        this.idCard = idCard;
        this.name = name;
    }

    public Employee() {
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
