package com.ihrm.web.model;

import java.io.Serializable;

public class SystemRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer systemRoleId;
    private String code;

    public SystemRole() {}

    public SystemRole(String code) {
        this.code = code;
    }

    public SystemRole(Integer systemRoleId, String code) {
        this.systemRoleId = systemRoleId;
        this.code = code;
    }

    public Integer getSystemRoleId() {
        return systemRoleId;
    }

    public void setSystemRoleId(Integer systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SystemRole{" +
                "systemRoleId=" + systemRoleId +
                ", code='" + code + '\'' +
                '}';
    }
}