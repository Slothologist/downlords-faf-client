package com.faforever.client.preferences;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class LoginPrefs {

  private final StringProperty username;
  private final StringProperty password;
  private final BooleanProperty autoLogin;
  private final StringProperty refreshToken;

  public LoginPrefs() {
    username = new SimpleStringProperty();
    password = new SimpleStringProperty();
    autoLogin = new SimpleBooleanProperty();
    refreshToken = new SimpleStringProperty();
  }

  @Deprecated
  public LoginPrefs setUsername(String username) {
    this.username.set(username);
    return this;
  }

  @Deprecated
  public LoginPrefs setPassword(String password) {
    this.password.set(password);
    return this;
  }

  @Deprecated
  public LoginPrefs setAutoLogin(boolean autoLogin) {
    this.autoLogin.set(autoLogin);
    return this;
  }

  public String getRefreshToken() {
    return refreshToken.get();
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken.set(refreshToken);
  }

  public StringProperty refreshTokenProperty() {
    return refreshToken;
  }
}
