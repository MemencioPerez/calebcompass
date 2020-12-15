package calebcompass.calebcompass.citizens;

import calebcompass.calebcompass.mythicmobs.MythicInstance;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

public class CitizensInstance {

    private static CitizensInstance instance;
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("CalebCompass");
    public static boolean isPluginInstalled = false;

    private File subfolder = new File(plugin.getDataFolder(), "citizens");
    private File mythicFile = new File(subfolder, "npcpoints.yml");
    private YamlConfiguration config;

    private int maxRange;
    private boolean defaultMobShow;
    private String defaultRegular;
    private String defaultHovered;
    private ArrayList<String[]> regularOverrides;
    private ArrayList<String[]> hoveredOverride;

    public static CitizensInstance getInstance() {
        if (instance == null) instance = new CitizensInstance();
        return instance;
    }

    public CitizensInstance() {
        if (!mythicFile.exists())plugin.saveResource("citizens" + File.separator + "npcpoints.yml", false);
        config = YamlConfiguration.loadConfiguration(mythicFile);
        isPluginInstalled = true;
        load();
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void load() {
        if (!mythicFile.exists())plugin.saveResource("citizens" + File.separator + "npcpoints.yml", false);
        config = YamlConfiguration.loadConfiguration(mythicFile);

        setupDefaults();
        loadRegularOverrides();
        loadHoveredOverrides();

        System.out.println(config.getString("default-settings.default-symbol-regular"));
        maxRange = config.getInt("default-settings.npc-detection-range");
        defaultMobShow = config.getBoolean("default-settings.show-all-npcs-by-default");
        defaultRegular = config.getString("default-settings.default-symbol-regular");
        defaultHovered = config.getString("default-settings.default-symbol-hovered");

    }
    public void save() {
        try {
            config.save(mythicFile);
        } catch (Exception e) {}
    }

    private void loadRegularOverrides() {
        regularOverrides = new ArrayList<>();
        if (config.getConfigurationSection("custom-overrides.regular") == null) return;
        for (String str : config.getConfigurationSection("custom-overrides.regular").getKeys(false)) {
            String curPath = "custom-overrides.regular." + str;
            System.out.println("Mythic" + str);
            regularOverrides.add(new String[]{str, config.getString(curPath)});
        }
    }

    private void loadHoveredOverrides() {
        hoveredOverride = new ArrayList<>();
        if (config.getConfigurationSection("custom-overrides.hovered") == null) return;
        for (String str : config.getConfigurationSection("custom-overrides.hovered").getKeys(false)) {
            String curPath = "custom-overrides.hovered." + str;
            System.out.println(str);
            hoveredOverride.add(new String[]{str, config.getString(curPath)});
        }
    }
    private void setupDefaults() {
        System.out.println("SETTING UP DEFAULTS");
        String curPath = "default-settings.";
        setDefaultValue(curPath + "show-all-npcs-by-default", false);
        setDefaultValue(curPath + "default-symbol-regular", "&4&l !! ");
        setDefaultValue(curPath + "default-symbol-hovered", "&b&l !! ");
        setDefaultValue(curPath + "npc-detection-range", 11);

        save();
    }

    private void setDefaultValue(String loc, String val) {
        if (config.getString(loc) != null) return;
        config.set(loc, val);
    }

    private void setDefaultValue(String loc, int va) {
        System.out.println(config.getInt(loc));
        if (config.getInt(loc) >0 ) return;
        config.set(loc, va);
    }

    private void setDefaultValue(String loc, boolean value) {
        if (config.isBoolean(loc)) return;
        config.set(loc, value);
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public boolean isDefaultNpcShow() {
        return defaultMobShow;
    }

    public String getRegularOverride(String mythicMob) {
        for (String[] cur : regularOverrides) {
            if (cur[0].equals(mythicMob)) return cur[1];
        }
        System.out.println(defaultRegular);
        return defaultRegular;
    }

    public String getHoveredOverride(String mythicMob) {
        for (String[] cur : hoveredOverride) {
            if (cur[0].equals(mythicMob)) return cur[1];
        }
        return defaultHovered;
    }

    public boolean hasCustomOverride(String mobName) {
        for (String[] cur : regularOverrides) { if (cur[0].equals(mobName)) return true; }
        for (String[] cur : hoveredOverride) { if (cur[0].equals(mobName)) return true; }
        return false;
    }
}
