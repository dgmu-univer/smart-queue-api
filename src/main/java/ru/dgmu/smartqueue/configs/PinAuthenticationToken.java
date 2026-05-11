package ru.dgmu.smartqueue.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class PinAuthenticationToken extends AbstractAuthenticationToken {

  private final Object pin;

  public PinAuthenticationToken() {
    super((Collection<? extends GrantedAuthority>) null);
    this.pin = null;
    setAuthenticated(false);
  }

  public PinAuthenticationToken(Object pin) {
    super((Collection<? extends GrantedAuthority>) null);
    this.pin = pin;
    setAuthenticated(false);
  }

  @JsonCreator
  public PinAuthenticationToken(@JsonProperty("principal") Object pin,
      @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.pin = pin;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return pin;
  }

  @Override
  public Object getPrincipal() {
    return pin;
  }
}
