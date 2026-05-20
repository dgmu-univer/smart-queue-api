package ru.dgmu.smartqueue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.dtos.OperatorLoginSuccessDto;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.services.DegreeProgramService;
import ru.dgmu.smartqueue.services.OperatorLoginService;

@Service
@RequiredArgsConstructor
public class OperatorLoginServiceImpl implements OperatorLoginService {

  private final DegreeProgramService degreeProgramService;

  @Override
  public OperatorLoginSuccessDto login(User user) {
    var degreeProgramDto = degreeProgramService.findDegreeProgramByUserId(user);

    return new OperatorLoginSuccessDto(degreeProgramDto.name(),
        degreeProgramDto.id().toString(), user.getRole().name());
  }
}
