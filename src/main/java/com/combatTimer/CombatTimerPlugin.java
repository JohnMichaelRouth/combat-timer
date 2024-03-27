package com.combatTimer;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Objects;

@PluginDescriptor(
		name = "Combat Timer"
)
public class CombatTimerPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private CombatTimerConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CombatTimerOverlay timerOverlay;

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(timerOverlay);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(timerOverlay);
	}

	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed) {
		if (soundEffectPlayed.getSoundId() == config.soundEffectId()) {
			if(!timerOverlay.isTimerRunning()){
				timerOverlay.startTimer();
			}
		}
	}

	@Provides
	CombatTimerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(CombatTimerConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick tick) {
		WorldPoint startLocation = timerOverlay.getStartLocation();
		if (timerOverlay.isTimerRunning()) {
			WorldPoint currentLocation = client.getLocalPlayer().getWorldLocation();
			if (startLocation != null && startLocation.distanceTo(currentLocation) > config.resetDistance()) {
				timerOverlay.resetTimer();
			}
		}
	}


	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event) {
		String resetAction = config.resetAction();
		if (resetAction != null && !resetAction.isEmpty()) {
			if (event.getMenuOption().equalsIgnoreCase(resetAction)) {
				if (timerOverlay.isTimerRunning()) {
					timerOverlay.resetTimer();
				}
			}
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned) {
		if (Objects.equals(npcDespawned.getNpc().getName(), config.monsterName())) {
			timerOverlay.resetTimer();
		}
	}
}
