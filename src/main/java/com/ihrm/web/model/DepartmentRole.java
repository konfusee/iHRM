package com.ihrm.web.model;

import java.io.Serializable;

public class DepartmentRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer departmentRoleId;
    private String name;

    public DepartmentRole() {}

    public DepartmentRole(String name) {
        this.name = name;
    }

    public DepartmentRole(Integer departmentRoleId, String name) {
        this.departmentRoleId = departmentRoleId;
        this.name = name;
    }

    public Integer getDepartmentRoleId() {
        return departmentRoleId;
    }

    public void setDepartmentRoleId(Integer departmentRoleId) {
        this.departmentRoleId = departmentRoleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DepartmentRole{" +
                "departmentRoleId=" + departmentRoleId +
                ", name='" + name + '\'' +
                '}';
    }
}