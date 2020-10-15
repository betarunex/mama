package com.mama.money.controller.ussd;

import com.mama.money.model.dtos.UssdRequest;
import com.mama.money.model.dtos.UssdResponse;
import com.mama.money.service.ussdservice.USSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ussd")
public class USSDController {

    @Autowired
    USSDService ussdService;

    @GetMapping("/test")
    public int test(){
        return 200;
    }

    @PostMapping("/sendussd")
    public UssdResponse sendUssd(@RequestBody UssdRequest request){

        return ussdService.selectMenu(request);
    }

}
