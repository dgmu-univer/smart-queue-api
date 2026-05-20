package ru.dgmu.smartqueue.services;

import ru.dgmu.smartqueue.dtos.OperatorLoginSuccessDto;
import ru.dgmu.smartqueue.entites.User;

public interface OperatorLoginService {

  OperatorLoginSuccessDto login(User user);

}
