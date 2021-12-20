package com.sidezbros.double_hotbar;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = "double_hotbar")
public class DHModConfig implements ConfigData {

	@ConfigEntry.Gui.Excluded
	public static DHModConfig INSTANCE;

	public static void init() {
		AutoConfig.register(DHModConfig.class, JanksonConfigSerializer::new);
		INSTANCE = AutoConfig.getConfigHolder(DHModConfig.class).getConfig();
	}

	public boolean displayDoubleHotbar = true;
	@ConfigEntry.BoundedDiscrete(min = 1, max = 3)
	public int inventoryRow = 3;

}