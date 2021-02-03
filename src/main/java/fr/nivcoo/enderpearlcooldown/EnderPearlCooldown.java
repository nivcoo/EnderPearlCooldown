/**
 * 
 */
package fr.nivcoo.enderpearlcooldown;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.nivcoo.enderpearlcooldown.events.PlayerInteract;
import fr.nivcoo.enderpearlcooldown.utils.Config;

public class EnderPearlCooldown extends JavaPlugin {
	private static EnderPearlCooldown INSTANCE;
	private Config config;

	@Override
	public void onEnable() {
		INSTANCE = this;
		saveDefaultConfig();
		config = new Config(new File("plugins" + File.separator + "EnderPearlCooldown" + File.separator + "config.yml"));
		getCommand("epc").setExecutor(new EnderPearlCooldownCommand());
		Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
		Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
		Bukkit.getConsoleSender().sendMessage("§7EnderPearlCooldown §av" + this.getDescription().getVersion());
		Bukkit.getConsoleSender().sendMessage("§aPlugin Enabled !");
		Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
		Bukkit.getConsoleSender().sendMessage("§7EnderPearlCooldown §cv" + this.getDescription().getVersion());
		Bukkit.getConsoleSender().sendMessage("§cPlugin Disabled !");
		Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
	}

	public void reload() {
		config.loadConfig();
	}

	public static EnderPearlCooldown get() {
		return INSTANCE;
	}

	public Config getConfiguration() {
		return config;
	}
}
