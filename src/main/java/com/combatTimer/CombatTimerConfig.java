package com.combatTimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("CombatTimer")
public interface CombatTimerConfig extends Config
{
	@ConfigItem(
		keyName = "soundEffectId",
		name = "Monster Attack Sound ID",
		description = "The ID of the monsters first attack"
	)
	default int soundEffectId()
	{
		return 163;
	}

	@ConfigItem(
			keyName = "flashTime",
			name = "Flash Time",
			description = "Time (in seconds) at which the info box will start flashing"
	)
	default int alertTime() {
		return 56;
	}
	// Thanks https://github.com/PortAGuy/thrall-helper/ for having a good example of a config
	@ConfigItem(
			keyName = "shouldFlash",
			name = "Flash the Reminder Box",
			description = "Makes the reminder box flash between the defined colors."
	)
	default boolean shouldFlash() { return true; }

	@ConfigItem(
			keyName = "flashColor1",
			name = "Flash Color #1",
			description = "The first color to flash between, also controls the non-flashing color."
	)
	default Color flashColor1() { return new Color(255, 0, 0, 150); }


	@ConfigItem(
			keyName = "flashColor2",
			name = "Flash Color #2",
			description = "The second color to flash between."
	)
	default Color flashColor2() { return new Color(70, 61, 50, 150); }

	@ConfigItem(
			keyName = "resetAction",
			name = "Reset Action",
			description = "The action that will reset the timer."
	)
	default String resetAction() {
		return "Quick Pass";
	}
	@ConfigItem(
			keyName = "monsterName",
			name = "Monster Name",
			description = "The Monster you'll be killing"
	)
	default String monsterName() {
		return "Cerberus";
	}
	@ConfigItem(
			keyName = "resetDistance",
			name = "Reset Distance",
			description = "How far away you need to walk for the timer to reset"
	)
	default int resetDistance() {
		return 50;
	}

}
