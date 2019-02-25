package com.yesmywine.sso.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by SJQ on 2017/5/27.
 */
@RestController
@RequestMapping("/todos")
//@PreAuthorize("hasRole('USER')")
public class TodoController {

    @RequestMapping(method = RequestMethod.POST)
    public String todo(){
        System.out.println("to do something");
        return "SUCCESS";
    }
}
