package calebcompass.calebcompass.SavePoints;

import calebcompass.calebcompass.CalebCompass;
import calebcompass.calebcompass.util.CompassInstance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class SavePointConfig {

    private static SavePointConfig instance;

    private File savePointFile;

    private FileConfiguration savePointConfig;

    private Plugin server = Bukkit.getPluginManager().getPlugin("CalebCompass");

    private ArrayList<SavePoint> currentPoints;

    public static SavePointConfig getInstance() {
        if (instance == null) instance = new SavePointConfig();
        return instance;
    }

    public SavePointConfig() {
        currentPoints = new ArrayList<SavePoint>();
        savePointFile = new File(server.getDataFolder(), "savepoints.yml");
        if (!savePointFile.exists()) server.saveResource("savepoints.yml", false);
        savePointConfig = YamlConfiguration.loadConfiguration(savePointFile);
        load();
    }

    public void togglePlayerPoint(UUID uuid, String point, boolean effect) {
        CompassInstance.getInstance().getCompassConfig().set("playerdata." + uuid.toString() + ".activepoints." + point, effect);
        CompassInstance.getInstance().saveData();
    }

    public void addSave(SavePoint newP) {
        this.currentPoints.add(newP);
    }

    public void removeSave(SavePoint save) {
        this.currentPoints.remove(save);
        savePointConfig.set("points." + save.getName(), null);
        if (CompassInstance.getInstance().getCompassConfig().getConfigurationSection("playerdata") == null) return;
        for(String uuid : CompassInstance.getInstance().getCompassConfig().getConfigurationSection("playerdata").getKeys(false)) {
            CompassInstance.getInstance().getCompassConfig().set("playerdata." + uuid + ".activepoints." + save.getName(), null);
        }
        serialiseValues();
        CompassInstance.getInstance().load();
    }

    public SavePoint getPointFromName(String name) {
        for (SavePoint cur : currentPoints) {
            if (name.equalsIgnoreCase(cur.getName())) {
                return cur;
            }
        }
        return null;
    }

    public boolean pointExists(String pointName) {
        for (SavePoint cur : currentPoints) {
            if (pointName.equalsIgnoreCase(cur.getName())) return true;
        }
        return false;
    }

    public boolean pointExistsExplicit(String pointName) {
        for (SavePoint cur : currentPoints) {
            if (pointName.equals(cur.getName())) return true;
        }
        return false;
    }

    public void serialiseValues() {
        for (SavePoint p : this.currentPoints) {
            savePointConfig.set("points." + p.getName() + ".world", p.getLoc1().getWorld().getName());
            savePointConfig.set("points." + p.getName() + ".x", p.getLoc1().getBlockX());
            savePointConfig.set("points." + p.getName() + ".y", p.getLoc1().getBlockY());
            savePointConfig.set("points." + p.getName() + ".z", p.getLoc1().getBlockZ());
        }
    }

    public void load() {
        this.currentPoints = new ArrayList<SavePoint>();
        if (savePointConfig.getConfigurationSection("points") == null) return;
        for (String load : savePointConfig.getConfigurationSection("points").getKeys(false)) {
            try {
                String curLoad = "points." + load +  ".";
                SavePoint point = new SavePoint(new Location(Bukkit.getWorld(savePointConfig.getString(curLoad + "world")),
                        Integer.valueOf(savePointConfig.getString(curLoad + "x")),
                        Integer.valueOf(savePointConfig.getString(curLoad + "y")),
                        Integer.valueOf(savePointConfig.getString(curLoad + "z"))), load, "NULL");
                this.currentPoints.add(point);
            } catch (Exception e) {

            }
        }
    }

    public void saveData() {
        serialiseValues();
        try {
            savePointConfig.save(savePointFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}