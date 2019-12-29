package com.faforever.client.remote;

import com.faforever.client.FafClientApplication;
import com.faforever.client.fa.relay.GpgGameMessage;
import com.faforever.client.game.Faction;
import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.game.NewGameInfo;
import com.faforever.client.i18n.I18n;
import com.faforever.client.net.ConnectionState;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.remote.domain.Avatar;
import com.faforever.client.remote.domain.GameAccess;
import com.faforever.client.remote.domain.GameInfoMessage;
import com.faforever.client.remote.domain.GameLaunchMessage;
import com.faforever.client.remote.domain.GameStatus;
import com.faforever.client.remote.domain.IceServersServerMessage.IceServer;
import com.faforever.client.remote.domain.LoginMessage;
import com.faforever.client.remote.domain.PeriodType;
import com.faforever.client.remote.domain.ServerMessage;
import com.faforever.client.task.CompletableTask;
import com.faforever.client.task.TaskService;
import com.google.common.eventbus.EventBus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.faforever.client.task.CompletableTask.Priority.HIGH;
import static java.util.Collections.emptyList;

@Lazy
@Component
@Profile(FafClientApplication.PROFILE_OFFLINE)
@RequiredArgsConstructor
// NOSONAR
public class MockFafServerAccessor implements FafServerAccessor {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String USER_NAME = "MockUser";
  private final Timer timer = new Timer("LobbyServerAccessorTimer", true);
  private final HashMap<Class<? extends ServerMessage>, Collection<Consumer<ServerMessage>>> messageListeners = new HashMap<>();

  private final TaskService taskService;
  private final NotificationService notificationService;
  private final I18n i18n;
  private final EventBus eventBus;

  private final ObjectProperty<ConnectionState> connectionState = new SimpleObjectProperty<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T extends ServerMessage> void addOnMessageListener(Class<T> type, Consumer<T> listener) {
    if (!messageListeners.containsKey(type)) {
      messageListeners.put(type, new LinkedList<>());
    }
    messageListeners.get(type).add((Consumer<ServerMessage>) listener);
  }

  @Override
  public <T extends ServerMessage> void removeOnMessageListener(Class<T> type, Consumer<T> listener) {
    messageListeners.get(type).remove(listener);
  }

  @Override
  public ReadOnlyObjectProperty<ConnectionState> connectionStateProperty() {
    return connectionState;
  }

  @Override
  public CompletableFuture<LoginMessage> connectAndLogIn(String refreshToken) {
    return null;
  }

  @Override
  public CompletableFuture<GameLaunchMessage> requestHostGame(NewGameInfo newGameInfo) {
    return taskService.submitTask(new CompletableTask<GameLaunchMessage>(HIGH) {
      @Override
      protected GameLaunchMessage call() throws Exception {
        updateTitle("Hosting game");

        GameLaunchMessage gameLaunchMessage = new GameLaunchMessage();
        gameLaunchMessage.setArgs(Arrays.asList("/ratingcolor d8d8d8d8", "/numgames 1234"));
        gameLaunchMessage.setMod("faf");
        gameLaunchMessage.setUid(1234);
        return gameLaunchMessage;
      }
    }).getFuture();
  }

  @Override
  public CompletableFuture<GameLaunchMessage> requestJoinGame(int gameId, String password) {
    return taskService.submitTask(new CompletableTask<GameLaunchMessage>(HIGH) {
      @Override
      protected GameLaunchMessage call() throws Exception {
        updateTitle("Joining game");

        GameLaunchMessage gameLaunchMessage = new GameLaunchMessage();
        gameLaunchMessage.setArgs(Arrays.asList("/ratingcolor d8d8d8d8", "/numgames 1234"));
        gameLaunchMessage.setMod("faf");
        gameLaunchMessage.setUid(1234);
        return gameLaunchMessage;
      }
    }).getFuture();
  }

  @Override
  public void disconnect() {

  }

  @Override
  public void reconnect() {

  }

  @Override
  public void addFriend(int playerId) {

  }

  @Override
  public void addFoe(int playerId) {

  }

  @Override
  public CompletableFuture<GameLaunchMessage> startSearchLadder1v1(Faction faction) {
    logger.debug("Searching 1v1 match with faction: {}", faction);
    GameLaunchMessage gameLaunchMessage = new GameLaunchMessage();
    gameLaunchMessage.setUid(123);
    gameLaunchMessage.setMod(KnownFeaturedMod.DEFAULT.getTechnicalName());
    return CompletableFuture.completedFuture(gameLaunchMessage);
  }

  @Override
  public void stopSearchingRanked() {
    logger.debug("Stopping searching 1v1 match");
  }

  @Override
  public void sendGpgMessage(GpgGameMessage message) {

  }

  @Override
  public void removeFriend(int playerId) {

  }

  @Override
  public void removeFoe(int playerId) {

  }

  @Override
  public void selectAvatar(URL url) {

  }

  @Override
  public void banPlayer(int playerId, int duration, PeriodType periodType, String reason) {

  }

  @Override
  public void closePlayersGame(int playerId) {

  }

  @Override
  public void closePlayersLobby(int playerId) {

  }

  @Override
  public void broadcastMessage(String message) {

  }

  @Override
  public List<Avatar> getAvailableAvatars() {
    return emptyList();
  }

  @Override
  public CompletableFuture<List<IceServer>> getIceServers() {
    return CompletableFuture.completedFuture(Collections.emptyList());
  }

  @Override
  public void restoreGameSession(int id) {

  }

  @Override
  public void ping() {

  }


  private GameInfoMessage createGameInfo(int uid, String title, GameAccess access, String featuredMod, String mapName, int numPlayers, int maxPlayers, String host) {
    GameInfoMessage gameInfoMessage = new GameInfoMessage();
    gameInfoMessage.setUid(uid);
    gameInfoMessage.setTitle(title);
    gameInfoMessage.setFeaturedMod(featuredMod);
    gameInfoMessage.setMapname(mapName);
    gameInfoMessage.setNumPlayers(numPlayers);
    gameInfoMessage.setMaxPlayers(maxPlayers);
    gameInfoMessage.setHost(host);
    gameInfoMessage.setState(GameStatus.OPEN);
    gameInfoMessage.setSimMods(Collections.emptyMap());
    gameInfoMessage.setTeams(Collections.emptyMap());
    gameInfoMessage.setFeaturedModVersions(Collections.emptyMap());
    gameInfoMessage.setPasswordProtected(access == GameAccess.PASSWORD);

    return gameInfoMessage;
  }
}
