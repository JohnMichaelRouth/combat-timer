package com.combatTimer;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class CombatTimerOverlay extends OverlayPanel {
    private final Client client;
    private final CombatTimerPlugin plugin;
    private Instant startTime;
    private long startGameTick;

    @Getter
    private boolean timerRunning;
    @Getter
    private WorldPoint startLocation;

    private CombatTimerConfig config;

    @Inject
    public CombatTimerOverlay(Client client, CombatTimerPlugin plugin, CombatTimerConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setPreferredSize(new Dimension(150, 0));
    }

    public void startTimer() {
        this.startTime = Instant.now();
        this.startGameTick = client.getTickCount();
        this.startLocation = client.getLocalPlayer().getWorldLocation();
        this.timerRunning = true;
    }

    public void resetTimer() {
        this.startTime = null;
        this.startGameTick = -1;
        this.startLocation = null;
        this.timerRunning = false;
    }

    // Thanks https://github.com/PortAGuy/thrall-helper/ for having a good example of this flashing :D
    @Override
    public Dimension render(Graphics2D graphics) {
        if (startTime == null) {
            return null;
        }

        long elapsedMillis = Duration.between(startTime, Instant.now()).toMillis();
        long elapsedSeconds = elapsedMillis / 1000;

        String timerText = String.format("%d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Timer:")
                .right(timerText)
                .build());

        if (config.shouldFlash()) {
            if (elapsedSeconds >= config.alertTime()) {
                if (client.getGameCycle() % 40 >= 20) {
                    panelComponent.setBackgroundColor(config.flashColor1());
                } else {
                    panelComponent.setBackgroundColor(config.flashColor2());
                }
            } else {
                panelComponent.setBackgroundColor(config.flashColor2());
            }
        } else {
            if (elapsedSeconds >= config.alertTime()){
                panelComponent.setBackgroundColor(config.flashColor1());
            }
            else {
                panelComponent.setBackgroundColor(config.flashColor2());
            }
        }

        return super.render(graphics);
    }
}
