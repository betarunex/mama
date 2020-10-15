package com.mama.money.service.ussdservice;

import com.mama.money.model.dtos.UssdRequest;
import com.mama.money.model.dtos.UssdResponse;

public interface USSDService {

    UssdResponse selectMenu(final UssdRequest request);
}
