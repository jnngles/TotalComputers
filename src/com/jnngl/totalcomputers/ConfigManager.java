package com.jnngl.totalcomputers;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author Foodyling
 */
public class ConfigManager {
    /**
     * Describes config file
     */
    public record ConfigPath(String configName, String resourcePath, String outputPath) {

        /**
         * Getter for configName field
         * @return configName
         */
        public String getName() {
            return configName;
        }

        /**
         * Getter for resourcePath field
         * @return resourcePath
         */
        public String getResourcePath() {
            return resourcePath;
        }

        /**
         * Getter for outputPath field
         * @return outputPath
         */
        public String getOutputPath() {
            return outputPath;
        }
    }

    /**
     * Describes config
     */
    public static class Configuration {
        private final File configFile;
        private FileConfiguration config;

        /**
         * Constructor
         * @param configFile Config file
         * @param config Bukkit FileConfiguration
         */
        public Configuration (File configFile, FileConfiguration config) {
            this.configFile = configFile;
            this.config = config;
        }

        /**
         * Getter
         * @return config
         */
        public FileConfiguration getConfig() {
            return config;
        }

        /**
         * Getter
         * @return configFile
         */
        public File getFile() {
            return configFile;
        }

        /**
         * Reloads config
         * @return Success or not
         */
        public boolean reloadConfig() {
            try {
                config = YamlConfiguration.loadConfiguration(configFile);
                return true;
            } catch (Exception error) {
                return false;
            }
        }

        /**
         * Saves config
         * @return Success or not
         */
        public boolean saveConfig() {
            if (configFile != null) {
                try {
                    config.save(configFile);
                    return true;
                } catch (Throwable ignored) {

                }
            }
            return false;
        }
    }

    private Plugin caller;
    private File configFolder;
    private final TreeMap<String, Configuration> configurations = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Create a new instance of a ConfigManager for a specific plugin
     * @param pluginInstance Plugin that calls the ConfigManager
     */
    public ConfigManager(Plugin pluginInstance) {
        if (pluginInstance != null) {
            this.caller = pluginInstance;
            this.configFolder = pluginInstance.getDataFolder();
            if (!configFolder.exists()) {
                configFolder.mkdirs();
            }
            caller.getLogger().log(Level.INFO, "Configuration Manager for plugin {0} successfully initialized. (Made by Foodyling)", pluginInstance.getName());
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "Configuration Manager failed to initialize");
        }
    }

    /**
     * Load all configuration files
     * @param configPaths Collection of configuration files to load
     */
    public void loadConfigFiles(ConfigPath... configPaths) {
        for (ConfigPath path : configPaths) {
            try {
                String resourcePath = path.getResourcePath(),
                        outputPath = path.getOutputPath();
                FileConfiguration config = null;
                File configFile = null;
                if (outputPath != null) {
                    configFile = new File(configFolder, outputPath);
                    if (configFile.exists()) {
                        config = YamlConfiguration.loadConfiguration(configFile);
                    } else {
                        if (resourcePath == null) {
                            configFile.getParentFile().mkdirs();
                            configFile.createNewFile();
                            config = YamlConfiguration.loadConfiguration(configFile);
                        } else {
                            InputStream inputStream = caller.getResource(resourcePath);
                            if (inputStream != null) {
                                config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                                config.save(configFile);
                            }
                        }
                    }
                } else {
                    InputStream inputStream = caller.getResource(resourcePath);
                    if (inputStream != null) {
                        config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                    }
                }
                if (resourcePath != null && outputPath != null) {
                    try {
                        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(caller.getResource(resourcePath))));
                    } catch (Throwable error) {
                        caller.getLogger().log(Level.SEVERE, "Failed to set defaults of config: " + path.getName());
                    }
                }
                if (config != null) {
                    configurations.put(path.getName(), new Configuration(configFile, config));
                } else {
                    caller.getLogger().log(Level.SEVERE, "Error loading configuration: " + path.getName());
                }
            } catch (Throwable error) {
                error.printStackTrace();
                caller.getLogger().log(Level.SEVERE, "Error loading configuration: " + path.getName());
            }
        }
    }

    /**
     * @param logError Log error or not
     * @param configName Configuration name to save
     * @return Whether configuration saved successfully
     */
    public boolean saveConfig(String configName, boolean logError) {
        if (configurations.containsKey(configName)) {
            Configuration config = configurations.get(configName);
            if (config != null) {
                boolean saved = config.saveConfig();
                if (logError && !saved) {
                    caller.getLogger().log(Level.SEVERE, "Failed to save configuration: " + configName);
                }
                return saved;
            }
        }
        return false;
    }

    /**
     * Saves specific config
     * @param configName Name of the config
     * @return Whether configuration saved successfully
     */
    public boolean saveConfig(String configName) {
        return saveConfig(configName, false);
    }

    /**
     * @param logError Log error or not
     * Saves all configuration files
     */
    public void saveAllConfigs(boolean logError) {
        for (String configName : configurations.keySet()) {
            if (logError && !saveConfig(configName, logError)) {
                caller.getLogger().log(Level.SEVERE, "Failed to save configuration: " + configName);
            }
        }
    }

    /**
     * @param configName Config Name
     * @return Whether configuration was reloaded successfully
     */
    public boolean reloadConfig(String configName) {
        if (configurations.containsKey(configName)) {
            Configuration config = configurations.get(configName);
            if (config != null) {
                return config.reloadConfig();
            }
        }
        return false;
    }

    /**
     * Reloads all registered configuration manager
     * @return Success
     */
    public boolean reloadAllConfigs() {
        boolean success = true;
        for (String configName : configurations.keySet()) {
            if(!reloadConfig(configName)) success = false;
        }
        return success;
    }

    /**
     *
     * @param configName Config to unload
     */
    public void unloadConfig(String configName) {
        configurations.remove(configName);
    }

    /**
     * Unloads all configuration files in memory
     */
    public void unloadAllConfigs() {
        for (Iterator<Configuration> iterator = configurations.values().iterator(); iterator.hasNext();) {
            iterator.next();
            iterator.remove();
        }
    }


    /**
     *
     * @param configName Configuration name
     * @return Configuration interface, returns null if not found
     */
    public Configuration getConfig(String configName) {
        return configurations.getOrDefault(configName, null);
    }

    /**
     *
     * @param configName Configuration name
     * @return FileConfiguration, returns null if not found
     */
    public FileConfiguration getFileConfig(String configName) {
        Configuration config = getConfig(configName);
        if (config != null) {
            return config.getConfig();
        } else {
            return null;
        }
    }
}