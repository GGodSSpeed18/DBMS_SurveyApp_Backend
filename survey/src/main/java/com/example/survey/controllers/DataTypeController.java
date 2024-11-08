package com.example.survey.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.survey.entities.DataType;
import com.example.survey.services.DataTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DataTypeController {

    @Autowired
    private DataTypeService dataTypeService;

    @GetMapping("/datatypes")
    @PreAuthorize("isAuthenticated()")
    public List<DataType> getAllTypes() {
        return dataTypeService.getAllTypes();
    }

    @GetMapping("/datatypes/{typeid}")
    @PreAuthorize("isAuthenticated()")
    public DataType getAllTypes(@PathVariable int typeid) {
        return dataTypeService.findTypeById(typeid);
    }

    @PostMapping("/datatypes")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_FORMS','ROLE_AUTHOR_FORM')")
    public void addType(@RequestBody DataType newtype) {
        dataTypeService.addType(newtype);
    }

    @DeleteMapping("datatypes/{typeid}/delete")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_FORMS','ROLE_AUTHOR_FORM')")
    public void dropType(@PathVariable int typeid) {
        dataTypeService.dropType(typeid);
    }

    @PutMapping("datatypes/{typeid}/update")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_FORMS','ROLE_AUTHOR_FORM')")
    public DataType updateDataType(@PathVariable int typeid, @RequestBody DataType newtype) {
        newtype.setType_id(typeid);
        return dataTypeService.updaDataType(newtype);
    }
}
