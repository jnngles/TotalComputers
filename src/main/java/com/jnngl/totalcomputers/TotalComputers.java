/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jnngl.totalcomputers;

import com.jnngl.packet.MapPacketSender;
import com.jnngl.packet.MapPacketSenderFactory;
import com.jnngl.packet.PacketListener;
import com.jnngl.server.Server;
import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.exception.NoFreeIDException;
import com.jnngl.totalcomputers.motion.MotionCapabilities;
import com.jnngl.totalcomputers.motion.MotionCapture;
import com.jnngl.totalcomputers.motion.MotionCaptureDesc;
import com.jnngl.totalcomputers.sound.SoundWebServer;
import com.jnngl.totalcomputers.sound.SoundWebSocketServer;
import com.jnngl.totalcomputers.sound.discord.DiscordBot;
import com.jnngl.totalcomputers.system.RemoteOS;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.exception.AlreadyClientboundException;
import com.jnngl.totalcomputers.system.exception.AlreadyRequestedException;
import com.jnngl.totalcomputers.system.exception.TimedOutException;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base of TotalComputers plugin. (Excuse me for a tons of grammar mistakes :D)
 * @author JNNGL
 */
@SuppressWarnings("unused")
public class TotalComputers extends JavaPlugin implements Listener, MotionCapture {

    /* *************** CODE SECTION: FIELDS *************** */
    private static int free_session = 0;

    private final static String replyPrefix = ChatColor.GOLD + "[TotalComputers] " + ChatColor.RESET;
    private int delay = 1;
    private Logger logger;
    private ConfigManager configManager;
    private FileConfiguration config, computers, players;
    private Map<String, List<MonitorPiece>> monitors;
    private Map<ItemFrame, MonitorPieceIndex> interactiveTiles;
    /** Unhandled touch inputs */ public Set<InputInfo> unhandledInputs;
    private Map<Player, Location> firstPoses, secondPoses;
    private Map<String, SlotControl> slotsToRestore;
    private List<String> playersThatQuitWhileControl;
    private Map<Player, SelectionArea> areas;
    private Map<String, TotalOS> systems;
    private Map<String, BukkitTask> tasks;
    private Map<TotalOS, DoubleBuffer> packets;
    private List<String> registeredComputers;
    private Map<Player, String> links;
    private Map<String, SelectionArea> computersPhysicalData;
    private NamespacedKey recipe = null;
    private boolean is1_8 = false;
    private boolean isLegacy = false;
    private MapPacketSender sender;
    private Map<TotalOS, Player> executors;
    private Server server;
    private Map<Player, String> tokens;
    private boolean allowServerboundComputers;
    private boolean computersInitialized = false;
    private boolean invisibleFrames = false;

    /* *************** CODE SECTION: MOTION CAPTURE *************** */

    private record CaptureTarget(MotionCaptureDesc desc, Player target) {}
    private record SlotControl(BukkitTask task, ItemStack first, ItemStack second, ItemStack third, int s1, int s2, int s3) {}

    private Map<Player, TotalOS> locked;
    private Map<TotalOS, CaptureTarget> targets;
    private Map<Player, SlotControl> slots;
    private List<Player> drop;

    @Override
    public MotionCapabilities getCapabilities() {
        return new MotionCapabilities(true, false, true, true, true, true);
    }

    /**
     * Motion capture realization
     * @param desc See {@link MotionCaptureDesc}
     * @param os Operating System
     * @return
     */
    @Override
    public boolean startCapture(MotionCaptureDesc desc, TotalOS os) {
        if(!executors.containsKey(os)) {
            logger.warning("Motion Capture: Target not found.");
            return false;
        }
        if(targets.containsKey(os)) {
            logger.warning("Motion capture is busy.");
            executors.get(os).sendMessage(replyPrefix + ChatColor.RED + targets.get(os).target.getName() + Localization.get(2));
            return false;
        }
        if(desc.requiresGazeDirectionCapture() && !getCapabilities().supportsGaveDirectionCapture()) {
            logger.warning("This motion capture implementation does not support gave direction capture.");
            return false;
        }
        if(desc.requiresMovementCapture() && !getCapabilities().supportsMovementCapture()) {
            logger.warning("This motion capture implementation does not support movement capture.");
            return false;
        }
        if(desc.requiresJumpCapture() && !getCapabilities().supportsJumpCapture()) {
            logger.warning("This motion capture implementation does not support jump capture.");
            return false;
        }
        if(desc.requiresSneakCapture() && !getCapabilities().supportsShiftCapture()) {
            logger.warning("This motion capture implementation does not support shift capture.");
            return false;
        }
        if(desc.requiresSlotCapture() && !getCapabilities().supportsSlotCapture()) {
            logger.warning("This motion capture implementation does not support slot capture.");
            return false;
        }
        if(desc.requiresItemDropCapture() && !getCapabilities().supportsItemDropCapture()) {
            logger.warning("This motion capture implementation does not support item drop capture.");
            return false;
        }
        if(desc.requiresItemDropCapture() && !desc.requiresSlotCapture()) {
            logger.warning("Item drop caption requires slot capture.");
            return false;
        }

        Player target = executors.get(os);
        targets.put(os, new CaptureTarget(desc, target));
        locked.put(target, os);

        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(3));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(4));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(5) + (desc.requiresMovementCapture()? ChatColor.GREEN+Localization.get(13) : ChatColor.RED+Localization.get(14)));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(6) + (desc.requiresGazeDirectionCapture()? ChatColor.GREEN+Localization.get(13) : ChatColor.RED+Localization.get(14)));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(7) + (desc.requiresJumpCapture()? ChatColor.GREEN+Localization.get(13) : ChatColor.RED+Localization.get(14)));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(8) + (desc.requiresSneakCapture()? ChatColor.GREEN+Localization.get(13) : ChatColor.RED+Localization.get(14)));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(9) + (desc.requiresSlotCapture()? ChatColor.GREEN+Localization.get(13) : ChatColor.RED+Localization.get(14)));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(10) + (desc.requiresItemDropCapture()? ChatColor.GREEN+Localization.get(13) : ChatColor.RED+Localization.get(14)));
        target.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(11)+ChatColor.GREEN+"/tcmp release"+ChatColor.BLUE+Localization.get(12));

        Bukkit.getScheduler().runTask(this, () -> {
            Arrow arrow = target.getWorld().spawn(target.getLocation(), Arrow.class);
            try {
                arrow.getClass().getMethod("setGravity", boolean.class);
                arrow.setGravity(false);
            } catch (Throwable e) {
                logger.warning("Arrow#setGravity not found");
            }
            arrow.setInvulnerable(true);
            arrow.setPassenger(target);
        });

        if(desc.requiresSlotCapture()) {
            int slot1, slot2, slot3;
            target.getInventory().setHeldItemSlot(4);
            slot2 = target.getInventory().getHeldItemSlot();
            slot1 = slot2 - 1;
            slot3 = slot2 + 1;

            SlotControl slotControl = new SlotControl(Bukkit.getScheduler().runTaskTimer(this, () -> {
                if (target.getInventory().getHeldItemSlot() != slot2) {
                    int dif = target.getInventory().getHeldItemSlot() - slot2;
                    if (dif < 0) desc.slotCapture().slotLeft();
                    else if (dif > 0) desc.slotCapture().slotRight();
                    target.getInventory().setHeldItemSlot(slot2);
                }
            }, 0, 0), target.getInventory().getItem(slot1), target.getInventory().getItem(slot2),
                    target.getInventory().getItem(slot3), slot1, slot2, slot3);

            slots.put(target, slotControl);

            target.getInventory().setItem(slot1, new ItemStack(Material.AIR, 0));
            target.getInventory().setItem(slot2, new ItemStack(Material.BARRIER, 2));
            target.getInventory().setItem(slot3, new ItemStack(Material.AIR, 0));

            drop.add(target);
        }

        PacketListener.addPlayer(target, new PacketListener(new PacketListener.PacketHandler() {
            class AccessMethod {
                Method sneak, jump, forward, side;
            }

            private AccessMethod access;

            @Override
            public boolean read(Object packet) {
                if(!packet.getClass().getSimpleName().equals("PacketPlayInSteerVehicle")) return true;
                try {

                    Class<?> cls = packet.getClass();
                    if(access == null) {
                        access = new AccessMethod();
                        try {
                            access.sneak = cls.getMethod("a");
                            if(!access.sneak.getReturnType().equals(boolean.class)) throw new ReflectiveOperationException();
                            access.jump = cls.getMethod("d");
                            if(!access.jump.getReturnType().equals(boolean.class)) throw new ReflectiveOperationException();
                            access.forward = cls.getMethod("c");
                            if(!access.forward.getReturnType().equals(float.class)) throw new ReflectiveOperationException();
                            access.side = cls.getMethod("b");
                            if(!access.side.getReturnType().equals(float.class)) throw new ReflectiveOperationException();
                        } catch (Throwable e) {
                            try {
                                access.sneak = cls.getMethod("d");
                                if(!access.sneak.getReturnType().equals(boolean.class)) throw new ReflectiveOperationException();
                                access.jump = cls.getMethod("c");
                                if(!access.jump.getReturnType().equals(boolean.class)) throw new ReflectiveOperationException();
                                access.forward = cls.getMethod("b");
                                if(!access.forward.getReturnType().equals(float.class)) throw new ReflectiveOperationException();
                                access.side = cls.getMethod("a");
                                if(!access.side.getReturnType().equals(float.class)) throw new ReflectiveOperationException();
                            } catch (Throwable ex) {
                                access.sneak = cls.getMethod("f");
                                if(!access.sneak.getReturnType().equals(boolean.class)) throw new ReflectiveOperationException();
                                access.jump = cls.getMethod("e");
                                if(!access.jump.getReturnType().equals(boolean.class)) throw new ReflectiveOperationException();
                                access.forward = cls.getMethod("d");
                                if(!access.forward.getReturnType().equals(float.class)) throw new ReflectiveOperationException();
                                access.side = cls.getMethod("c");
                                if(!access.side.getReturnType().equals(float.class)) throw new ReflectiveOperationException();
                            }
                        }
                    }
                    boolean sneak = (boolean)access.sneak.invoke(packet);
                    boolean jump = (boolean)access.jump.invoke(packet);
                    float forward = (float)access.forward.invoke(packet);
                    float side = -(float)access.side.invoke(packet);

                    if(jump && desc.requiresJumpCapture())
                        desc.jumpEvent().onJump();

                    if((forward != 0 || side != 0) && desc.requiresMovementCapture())
                        desc.movementEvent().onMove(side, forward);

                    if(desc.requiresSneakCapture()) {
                        if(sneak)
                            desc.sneakEvent();
                    }

                    return false;
                } catch (Throwable e) {
                    logger.warning("Failed to access packet data");
                    return true;
                }
            }

            @Override
            public boolean write(Object packet) {
                return true;
            }
        }));

        return true;
    }

    /**
     * Realization. See {@link MotionCapture#stopCapture(TotalOS)}
     * @param os Operating System
     * @return Whether motion capture was stopped
     */
    @Override
    public boolean stopCapture(TotalOS os) {
        if(!executors.containsKey(os)) return false;
        if(executors.get(os).getName().equals(targets.get(os).target.getName())) {
            forceStopCapture(os);
            return true;
        }
        logger.warning("Motion capture is busy.");
        executors.get(os).sendMessage(replyPrefix + ChatColor.RED + targets.get(os).target.getName() + Localization.get(15));
        return false;
    }

    /**
     * Realization. See {@link MotionCapture#forceStopCapture(TotalOS)}
     * @param os Operating System
     */
    @Override
    public void forceStopCapture(TotalOS os) {
        if(!targets.containsKey(os)) return;
        Player target = targets.get(os).target;
        if(target == null) {
            System.err.println("Motion Capture: Target not found");
            return;
        }
        if(slots.containsKey(target)) {
            Inventory inv = target.getInventory();
            SlotControl event = slots.get(target);
            event.task.cancel();
            inv.setItem(event.s1, event.first);
            inv.setItem(event.s2, event.second);
            inv.setItem(event.s3, event.third);
            slots.remove(target);
        }
        drop.remove(target);
        PacketListener.removePlayer(target);
        Entity vehicle = target.getVehicle();
        if(vehicle != null) {
            vehicle.getPassengers().remove(target);
            vehicle.remove();
        }
        target.sendMessage(replyPrefix + ChatColor.BLUE +
                Localization.get(16));
        locked.remove(target);
        targets.remove(os);
    }

    /**
     * Checks if someone is using motion capture feature for given OS
     * @param os Operating System
     * @return boolean
     */
    @Override
    public boolean isCapturing(TotalOS os) {
        return targets.containsKey(os);
    }

    /**
     * Event Handler
     * @param e event
     */
    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        if(!computersInitialized) return;
        if(drop.contains(e.getPlayer())) {
            TotalOS os = locked.get(e.getPlayer());
            if(targets.get(os).desc.requiresItemDropCapture()) {
                if (e.getItemDrop().getItemStack().getAmount() == 1) {
                    targets.get(os).desc.itemDropCapture().itemDrop();
                } else if (e.getItemDrop().getItemStack().getAmount() == 2) {
                    targets.get(os).desc.itemDropCapture().stackDrop();
                }
            }
            e.setCancelled(true);
        }
    }

    /**
     * Event Handler
     * @param e event
     */
    @EventHandler
    public void dismount(EntityDismountEvent e) {
        if(!computersInitialized) return;
        if(!(e.getEntity() instanceof Player player)) return;
        if(!(e.getDismounted() instanceof Arrow vehicle)) return;

        if(locked.containsKey(player)) {
            forceStopCapture(locked.get(player));
            vehicle.remove();
        }
    }

    /* *************** CODE SECTION: RECORDS, ENUMS *************** */
    // Contains useful records and enums.

    public record InputInfo(TotalComputers.MonitorPieceIndex index, int x, int y, InteractType interactType,
                            Player player) {

        /**
         * Type of interaction with the computer
         */
        public enum InteractType {
            /**
             * Left click action
             */
            LEFT_CLICK,

            /**
             * Right click action
             */
            RIGHT_CLICK
        }

    }

    /**
     * Describes physical data of computer
     */
    public static record SelectionArea(Location firstPos, Location secondPos, TotalComputers.SelectionArea.Axis axis,
                                TotalComputers.SelectionArea.Direction direction, int width, int height, int area) {

        /**
         * Describes direction of surface normal; UP = UP; DOWN = DOWN; RIGHT = EAST; LEFT = WEST; FORWARD = SOUTH; BACKWARD = NORTH;
         */
        public enum Direction {
            /**
             * Up direction
             */
            UP,

            /**
             * Down direction
             */
            DOWN,

            /**
             * Right direction
             */
            RIGHT,

            /**
             * Left direction
             */
            LEFT,

            /**
             * Forward direction
             */
            FORWARD,

            /**
             * Backward direction
             */
            BACKWARD

        }

        /**
         * Describes axis of surface normal
         */
        public enum Axis {
            /**
             * X axis
             */
            X,

            /**
             * Y axis
             */
            Y,

            /**
             * Z axis
             */
            Z
        }

        /**
         * Default equals generated by IntelliJ IDEA
         * @param o Object to compare
         * @return Equals or not
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SelectionArea that = (SelectionArea) o;

            if (width != that.width) return false;
            if (height != that.height) return false;
            if (area != that.area) return false;
            if (!firstPos.equals(that.firstPos)) return false;
            if (!secondPos.equals(that.secondPos)) return false;
            if (axis != that.axis) return false;
            return direction == that.direction;
        }

        /**
         * Default hashCode generated by IntelliJ IDEA
         * @return Hash code
         */
        @Override
        public int hashCode() {
            int result = firstPos.hashCode();
            result = 31 * result + secondPos.hashCode();
            result = 31 * result + axis.hashCode();
            result = 31 * result + direction.hashCode();
            result = 31 * result + width;
            result = 31 * result + height;
            result = 31 * result + area;
            return result;
        }
    }

    /**
     * Information about monitor frame with map
     */
    public static record MonitorPiece(MapView mapView, ItemFrame frame) {}

    private static class DoubleBuffer {
        private final Object[][] frames;
        private static boolean warned = false;
        private int current = 0;

        public DoubleBuffer(Object[]... frames) {
            this.frames = frames;
        }

        public Object[] get() {
            try {
                int tmp = ++current;
                tmp %= frames.length;
                current = tmp;
                return frames[tmp];
            } catch(Throwable e) {
                if(!warned) {
                    System.err.println("Frames are processed too slowly. If this affects the server or plugin, try to increase delay-ticks in config.yml");
                    warned = true;
                }
                return frames[0];
            }
        }

    }

    /**
     * Information about part of the computer
     */
    public static record MonitorPieceIndex(String name, int index) {}

    /* *************** CODE SECTION: INITIALIZATION *************** */

    private String checkUpdates() throws IOException {
        try {
            Scanner scanner = new Scanner(new URL("https://raw.githubusercontent.com/JNNGL/TotalComputers/main/VERSION").openStream());
            String latestVersion = scanner.nextLine();
            if(!latestVersion.equals(super.getDescription().getVersion())) {
                return latestVersion;
            }
        } catch (IOException e) {
            logger.warning(Localization.get(155));
            throw e;
        }
        return null;
    }

    /**
     * Initializes config field
     */
    private void loadConfigs() {
        if(configManager == null) return;
        config = configManager.getFileConfig("config.yml");
        computers = configManager.getFileConfig("computers.yml");
        players = configManager.getFileConfig("players.yml");
        delay = config.getInt("delay-ticks");
        invisibleFrames = config.getBoolean("invisible-frames");
        String locale = config.getString("locale");
        if(locale == null) return;
        if(new Locale("ru").getLanguage().equals(new Locale(locale).getLanguage()))
            Localization.init(new Localization.HeccrbqZpsr());
        else Localization.init(new Localization.EnglishLang());


    }

    /**
     * Loads all computers from config
     */
    private void loadComputers() {
        for(BukkitTask task : tasks.values())
            task.cancel();
        tasks.clear();
        unhandledInputs = new HashSet<>();
        monitors = new HashMap<>();
        targets = new HashMap<>();
        locked = new HashMap<>();
        slots = new HashMap<>();
        tokens = new HashMap<>();
        slotsToRestore = new HashMap<>();
        playersThatQuitWhileControl = new ArrayList<>();
        drop = new ArrayList<>();
        executors = new HashMap<>();
        if(computers.isSet("computers.names")) registeredComputers = computers.getStringList("computers.names");
        else registeredComputers = new ArrayList<>();
        computersPhysicalData = new HashMap<>();
        systems = new HashMap<>();
        links = new HashMap<>();
        interactiveTiles = new HashMap<>();
        for(String name : registeredComputers) {
            World world = getServer().getWorld(computers.getString("computers."+name+".world"));
            computersPhysicalData.put(name, new SelectionArea(
                    new Location(
                            world, computers.getInt("computers."+name+".area.firstPos.x"),
                            computers.getInt("computers."+name+".area.firstPos.y"),
                            computers.getInt("computers."+name+".area.firstPos.z")
                    ),
                    new Location(
                            world, computers.getInt("computers."+name+".area.secondPos.x"),
                            computers.getInt("computers."+name+".area.secondPos.y"),
                            computers.getInt("computers."+name+".area.secondPos.z")
                    ),
                    SelectionArea.Axis.valueOf(computers.getString("computers."+name+".area.axis")),
                    SelectionArea.Direction.valueOf(computers.getString("computers."+name+".area.direction")),
                    computers.getInt("computers."+name+".area.width"),
                    computers.getInt("computers."+name+".area.height"),
                    computers.getInt("computers."+name+".area.area")
            ));

            initComputer(name);
        }
    }

    private void createRecipe() {
        try {
            if (recipe != null) Bukkit.removeRecipe(recipe);
        } catch (Throwable e) {
            logger.warning("Unable to remove recipe");
        }
        if(config.getBoolean("allowCraft")) {
            do {
                String row1 = config.getString("craft.row1");
                String row2 = config.getString("craft.row2");
                String row3 = config.getString("craft.row3");
                if (row1.trim().equals("") && row2.trim().equals("") && row3.trim().equals("")) {
                    logger.log(Level.WARNING, "Crafting recipe is empty!");
                    break;
                }
                List<String> raw = config.getStringList("craft.ingredients");

                record Ingredient(char key, Material mat) {
                }

                List<Ingredient> ingredients = new ArrayList<>();

                boolean isOK = true;

                for (String line : raw) {
                    String[] parts = line.split(" ");
                    if (parts.length != 2) {
                        logger.log(Level.WARNING, "Invalid ingredient data");
                        logger.log(Level.WARNING, "Should be: '<key char> <material>'");
                        isOK = false;
                        break;
                    }

                    if (parts[0].length() != 1) {
                        logger.log(Level.WARNING, "`" + parts[0] + "' is not a character!");
                        isOK = false;
                        break;
                    }

                    Material material = Material.matchMaterial(parts[1].replace('-', '_'));

                    if (material == null) {
                        logger.log(Level.WARNING, "Could not find material `" + parts[1] + "'!");
                        isOK = false;
                        break;
                    }

                    ingredients.add(new Ingredient(parts[0].charAt(0), material));
                }

                if (!isOK) break;

                {
                    String check = row1 + row2 + row3;
                    for (char key : check.toCharArray()) {
                        boolean found = false;
                        for (Ingredient i : ingredients) {
                            if (i.key == key) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            logger.log(Level.WARNING, "Ingredient `" + key + "' not found!");
                            isOK = false;
                            break;
                        }
                    }
                    if (!isOK) break;
                }

                Material expBottle;
                try {
                    if (isLegacy) expBottle = (Material) Material.class.getField("EXP_BOTTLE").get(null);
                    else expBottle = (Material) Material.class.getField("EXPERIENCE_BOTTLE").get(null);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Failed to find exp bottle material");
                    break;
                }
                ItemStack result = new ItemStack(expBottle, 1);
                ItemMeta meta = result.getItemMeta();
                meta.setDisplayName(Localization.get(122));
                List<String> lore = new ArrayList<>();
                lore.add(Localization.get(123));
                lore.add(Localization.get(124));
                lore.add(Localization.get(125));
                lore.add(Localization.get(126));
                meta.setLore(lore);
                result.setItemMeta(meta);

                ShapedRecipe recipe = new ShapedRecipe(this.recipe = new NamespacedKey(this, "computer_recipe"), result);
                recipe.shape(row1, row2, row3);

                for (Ingredient i : ingredients) recipe.setIngredient(i.key, i.mat);

                try {
                    getServer().addRecipe(recipe);
                } catch (Throwable e) {
                    logger.warning("Unable to add recipe: " + e.getMessage());
                }
                logger.log(Level.INFO, "Crafting recipe successfully created!");
            } while (false);
        }
    }

    /**
     * Initializes logger, config manager, computers etc.
     */
    @Override
    public void onEnable() {
        int pluginId = 14992;
        Metrics metrics = new Metrics(this, pluginId);

        logger = getLogger();
        configManager = new ConfigManager(this);
        configManager.loadConfigFiles(
                new ConfigManager.ConfigPath("config.yml", null, "config.yml"),
                new ConfigManager.ConfigPath("computers.yml", null, "computers.yml"),
                new ConfigManager.ConfigPath("players.yml", null, "players.yml"));
        String ver = Bukkit.getBukkitVersion();
        is1_8 = ver.contains("1.8");
        isLegacy = is1_8 || ver.contains("1.9") || ver.contains("1.10") || ver.contains("1.11") || ver.contains("1.12");
        try {
            sender = MapPacketSenderFactory.createMapPacketSender(ver);
        } catch (ReflectiveOperationException e) {
            logger.warning("Failed to create map packet sender");
            logger.warning(" -> "+e.getMessage());
        }
        loadConfigs();
        if(!config.isSet("delay-ticks")) config.set("delay-ticks", 1);
        delay = config.getInt("delay-ticks");
        if(!config.isSet("locale")) config.set("locale",
                Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())? "ru" : "en");
        if(!config.isSet("selection")) config.set("selection", true);
        if(!config.isSet("allowCraft")) config.set("allowCraft", false);
        if(!config.isSet("invisible-frames")) config.set("invisible-frames", false);
        if(!config.isSet("discord_bot_token")) config.set("discord_bot_token", "yourdiscordbottoken");
        if(!config.isSet("enable-server")) config.set("enable-server", true);
        if(!config.isSet("server-ip")) config.set("server-ip", "0.0.0.0");
        if(!config.isSet("server-port")) config.set("server-port", 29077);
        if(!config.isSet("server-name")) config.set("server-name", Bukkit.getServer().getName());
        if(!config.isSet("enable-encryption")) config.set("enable-encryption", true);
        if(!config.isSet("allow-serverbound-computers")) config.set("allow-serverbound-computers", true);
        if(!config.isSet("allow-clientbound-computers")) config.set("allow-clientbound-computers", true);
        if(!config.isSet("client-download-link")) config.set("client-download-link", "https://github.com/JNNGL/TotalComputers-Client/releases");
        if(!config.isSet("packet-debug")) config.set("packet-debug", false);
        if(!config.isSet("craft.row1")) config.set("craft.row1", "   ");
        if(!config.isSet("craft.row2")) config.set("craft.row2", "   ");
        if(!config.isSet("craft.row3")) config.set("craft.row3", "   ");
        if(!config.isSet("craft.ingredients")) config.set("craft.ingredients", new ArrayList<String>());
        configManager.saveAllConfigs(true);

        invisibleFrames = config.getBoolean("invisible-frames");

        tasks = new HashMap<>();
        packets = new HashMap<>();

        if(new Locale("ru").getLanguage().equals(new Locale(config.getString("locale")).getLanguage()))
            Localization.init(new Localization.HeccrbqZpsr());
        else Localization.init(new Localization.EnglishLang());

        allowServerboundComputers = config.getBoolean("allow-serverbound-computers");

        createRecipe();

        getServer().getPluginManager().registerEvents(this, this);
        firstPoses = new HashMap<>();
        secondPoses = new HashMap<>();
        areas = new HashMap<>();
        logger.info("Total Computers enabled. (Made by JNNGL)");

        if(!config.getString("discord_bot_token").equals("yourdiscordbottoken")) {
            logger.info("Starting discord bot...");
            TotalOS.audio = DiscordBot.start(config.getString("discord_bot_token"));
        }

        logger.info("Starting sound server...");
        try {
            SoundWebServer.run();
            SoundWebSocketServer.runServer();
        } catch (UnknownHostException e) {
            logger.warning("Failed to start sound server :(");
            logger.warning("[SoundServer] -> "+e.getMessage());
        }

        if(config.getBoolean("enable-server")) {
            logger.info("Starting TotalComputers server...");
            Server.DEBUG = config.getBoolean("packet-debug");
            if(Server.DEBUG)
                System.out.println("Debug is enabled.");
            server = new Server();
            server.name = config.getString("server-name");
            server.enableEncryption = config.getBoolean("enable-encryption");
            if(!server.enableEncryption) {
                logger.warning("!====================================!");
                logger.warning("Encryption is disabled");
                logger.warning("It is highly recommended to enable it!");
                logger.warning("!====================================!");
            }
            server.start(config.getString("server-ip"), config.getInt("server-port"));
            logger.info("Done.");
        }

        try {
            String update = checkUpdates();
            if(update != null) {
                logger.info(Localization.get(156)+update);
                logger.info(Localization.get(157)+"https://github.com/JNNGL/TotalComputers/releases");
            }
        } catch (IOException ignored) {}

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            loadComputers();
            computersInitialized = true;
        }, 200);
    }

    /* *************** CODE SECTION: COMMANDS AND AUTOCOMPLETION *************** */

    /**
     * Processing of command. Permission: <code>totalcomputers.command.totalcomputers</code>
     * @param sender Command executor
     * @param command Command
     * @param label Command label
     * @param args Command Arguments
     * @return Always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("totalcomputers")) {
                if(!computersInitialized) {
                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(158));
                    return true;
                }
                if(!sender.hasPermission("totalcomputers.command.totalcomputers")) { // Check permissions
                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(37));
                    return true;
                }
                if(args.length == 0 || args[0].equalsIgnoreCase("help")) { // Help subcommand
                    sender.sendMessage(replyPrefix + Localization.get(17));
                    sender.sendMessage(ChatColor.GOLD + Localization.get(18) + ChatColor.WHITE  + " /tcomputers, /tcmp");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers help" + ChatColor.WHITE + Localization.get(19));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers sound" + ChatColor.WHITE + Localization.get(20));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers create <name>" + ChatColor.WHITE + Localization.get(21));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers remove <name>" + ChatColor.WHITE + Localization.get(22));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers list" + ChatColor.WHITE + Localization.get(23));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers data <name>" + ChatColor.WHITE + Localization.get(24));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers selection <enable|disable|toggle|state>" + ChatColor.WHITE + Localization.get(25));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers wand" + ChatColor.WHITE + Localization.get(26));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers paste <text>" + ChatColor.WHITE + Localization.get(27));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers erase <all|numChars>" + ChatColor.WHITE + Localization.get(28));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers reload" + ChatColor.WHITE + Localization.get(29));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers token reset" + ChatColor.WHITE + Localization.get(30));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers token" + ChatColor.WHITE + Localization.get(31));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers client" + ChatColor.WHITE + Localization.get(32));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers client bind <name>" + ChatColor.WHITE + Localization.get(33));
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers client unbind <name>" + ChatColor.WHITE + Localization.get(34));
                }
                else if(args[0].equalsIgnoreCase("sound")) { // Sound subcommand
                    String link;
                    if(links.containsKey((Player)sender)) {
                        link = links.get((Player)sender);
                    } else {
                        link = "http://"+Bukkit.getServer().getIp()+":7254/index.html?name="+sender.getName()+"&sessionId="+(free_session++);
                        links.put((Player)sender, link);
                    }
                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(35));
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(36)+ChatColor.WHITE+link);
                }
                else if(args[0].equalsIgnoreCase("reload")) { // Reload subcommand
                    if(!sender.hasPermission("totalcomputers.plugin.manage")) {
                        sender.sendMessage(replyPrefix + ChatColor.RED +
                                Localization.get(37));
                        return true;
                    }

                    boolean success = configManager.reloadAllConfigs();
                    loadConfigs();
                    createRecipe();
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(38));
                    if(success) {
                        loadComputers();
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(39));
                    }
                    else sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(40));
                    sender.sendMessage(replyPrefix + ChatColor.BLUE + Localization.get(41));

                    try {
                        String update = checkUpdates();
                        if(update != null) {
                            sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(156)+ChatColor.RESET+update);
                            sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(157)+ChatColor.BLUE+"https://github.com/JNNGL/TotalComputers/releases\n");
                        }
                    } catch (IOException ignored) {
                        sender.sendMessage(replyPrefix+ChatColor.RED+Localization.get(155));
                    }
                }
                else if(args[0].equalsIgnoreCase("paste")) { // Paste subcommand
                    if(!sender.hasPermission("totalcomputers.use")) {
                        sender.sendMessage(replyPrefix + ChatColor.GRAY + Localization.get(42));
                        return true;
                    }

                    if(args.length == 1) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(43));
                        return true;
                    }
                    StringBuilder toPaste = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        toPaste.append(args[i]);
                        if (i != args.length - 1) toPaste.append(" ");
                    }

                    TotalOS os = getKeyboardHandle((Player) sender);
                    if(os == null) return true;
                    os.keyboard.typeString(toPaste.toString());

                }
                else if(args[0].equalsIgnoreCase("erase")) { // Erase subcommand
                    if(!sender.hasPermission("totalcomputers.use")) {
                        sender.sendMessage(replyPrefix + ChatColor.GRAY + Localization.get(44));
                        return true;
                    }

                    if(args.length > 2) {
                        invalidUsage(sender);
                        return true;
                    }

                    TotalOS os = getKeyboardHandle((Player) sender);
                    if(os == null) return true;
                    if(args.length == 1) os.keyboard.erase(1);
                    else if(args[1].equalsIgnoreCase("all")) os.keyboard.eraseAll();
                    else {
                        try {
                            os.keyboard.erase(Integer.parseInt(args[1]));
                        } catch (Throwable e) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(45)+args[1]+Localization.get(46));
                        }
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("selection")) { // Selection subcommand
                    if(args.length >= 2) {
                        if(!sender.hasPermission("totalcomputers.plugin.manage")) {
                            sender.sendMessage(replyPrefix + ChatColor.RED +
                                    Localization.get(47));
                        }
                        if(args[1].equalsIgnoreCase("enable")) {
                            config.set("selection", true);
                            sender.sendMessage(replyPrefix + Localization.get(48));
                            configManager.saveConfig("config.yml");
                        }
                        else if(args[1].equalsIgnoreCase("disable")) {
                            config.set("selection", false);
                            sender.sendMessage(replyPrefix + Localization.get(49));
                            configManager.saveConfig("config.yml");
                        }
                        else if(args[1].equalsIgnoreCase("toggle")) {
                            boolean currentState;
                            config.set("selection", (currentState = !isSelectionEnabled()));
                            sender.sendMessage(replyPrefix + Localization.get(50) + (currentState? Localization.get(51) : Localization.get(52)) + '.');
                            configManager.saveConfig("config.yml");
                        }
                        else if(args[1].equalsIgnoreCase("state")) {
                            sender.sendMessage(replyPrefix + Localization.get(53) + (config.getBoolean("selection")? Localization.get(54) : Localization.get(55)) + '.');
                        }
                        else invalidUsage(sender);
                    } else invalidUsage(sender);
                }
                else if(args[0].equalsIgnoreCase("create")) { // Create subcommand
                    if(args.length == 2) {
                        Player player = (Player)sender;
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            if(!sender.hasPermission("totalcomputers.manage.crafted")) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        Localization.get(56));
                                return true;
                            }
                            if(getUnregComputers(player) <= 0) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        Localization.get(57));
                                return true;
                            }
                        }
                        if(!areas.containsKey(player)) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(58));
                            return true;
                        }
                        SelectionArea area = areas.get(player);
                        if((float)area.width / (float)area.height < 4f/3f) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(59));
                            return true;
                        }
                        if(area.width > 16) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(60));
                            return true;
                        }
                        if(area.height > 9) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(61));
                            return true;
                        }
                        if(area.width < 4) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(62));
                            return true;
                        }
                        if(area.height < 3) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(63));
                            return true;
                        }
                        if(area.axis == SelectionArea.Axis.Y) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(64));
                            return true;
                        }
                        if(registeredComputers.contains(args[1])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(65));
                            return true;
                        }

                        computers.set("computers."+args[1]+".area.firstPos.x", area.firstPos.getBlockX());
                        computers.set("computers."+args[1]+".area.firstPos.y", area.firstPos.getBlockY());
                        computers.set("computers."+args[1]+".area.firstPos.z", area.firstPos.getBlockZ());
                        computers.set("computers."+args[1]+".area.secondPos.x", area.secondPos.getBlockX());
                        computers.set("computers."+args[1]+".area.secondPos.y", area.secondPos.getBlockY());
                        computers.set("computers."+args[1]+".area.secondPos.z", area.secondPos.getBlockZ());
                        computers.set("computers."+args[1]+".area.direction", area.direction.toString());
                        computers.set("computers."+args[1]+".area.axis", area.axis.toString());
                        computers.set("computers."+args[1]+".area.width", area.width);
                        computers.set("computers."+args[1]+".area.height", area.height);
                        computers.set("computers."+args[1]+".area.area", area.area);
                        computers.set("computers."+args[1]+".world", area.firstPos.getWorld().getName());
                        registeredComputers.add(args[1]);
                        computersPhysicalData.put(args[1], area);
                        computers.set("computers.names", registeredComputers);
                        configManager.saveConfig("computers.yml", true);
                        initComputer(args[1]);
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(66)+args[1]+Localization.get(67));
                        decreaseUnregComputers(player);
                        addOwner(player, args[1]);
                        if(!allowServerboundComputers) {
                            sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(68));
                            sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(69));
                        }
                    } else if(args.length > 2) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(70));
                    } else invalidUsage(sender);
                }
                else if(args[0].equalsIgnoreCase("client")) { // Client subcommand
                    if(!config.getBoolean("enable-server")) {
                        sender.sendMessage(replyPrefix+ChatColor.RED+Localization.get(71));
                        return true;
                    }
                    if(!config.getBoolean("allow-clientbound-computers")) {
                        sender.sendMessage(replyPrefix+ChatColor.RED+Localization.get(72));
                        return true;
                    }
                    if(args.length == 3) {
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            if(!sender.hasPermission("totalcomputers.manage.crafted")
                                    || !playerOwns((Player) sender, args[2])) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        Localization.get(73));
                                return true;
                            }
                        }
                        if(!registeredComputers.contains(args[2])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(74)+args[2]+Localization.get(75));
                            return true;
                        }
                        if(args[1].equalsIgnoreCase("bind")) {
                            String token = tokens.get((Player) sender);
                            if(token == null) {
                                sender.sendMessage(replyPrefix+ChatColor.RED+
                                        Localization.get(76));
                                return true;
                            }
                            TotalOS serverbound = systems.get(args[2]);
                            new Thread(() -> {
                                try {
                                    sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(77));
                                    RemoteOS os = RemoteOS.requestCreation(server, token,
                                            serverbound.name, serverbound.screenWidth, serverbound.screenHeight);
                                    sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(152));
                                } catch (AlreadyRequestedException e) {
                                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(78));
                                } catch (TimedOutException e) {
                                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(79));
                                } catch (InvalidTokenException e) {
                                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(80));
                                } catch (AlreadyClientboundException e) {
                                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(81));
                                } catch (NoFreeIDException e) {
                                    sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(159));
                                }
                            }).start();
                            return true;
                        } else if(args[1].equalsIgnoreCase("unbind")) {
                            RemoteOS remote = RemoteOS.fromName(args[2]);
                            if(remote == null) {
                                sender.sendMessage(replyPrefix+ChatColor.RED+Localization.get(82));
                                return true;
                            }
                            remote.destroy();
                            sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(83));
                        } else invalidUsage(sender);
                    } else {
                        sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(84)
                                +ChatColor.BLUE+config.getString("client-download-link"));
                        sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(85));
                        sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(86));
                        sender.sendMessage(replyPrefix+ChatColor.GRAY+Localization.get(87)+config.get("server-port")+Localization.get(88));
                        sender.sendMessage(replyPrefix+ChatColor.GREEN+Localization.get(89));
                        sender.sendMessage(replyPrefix+ChatColor.GRAY+Localization.get(90));
                    }
                }
                else if(args[0].equalsIgnoreCase("remove")) { // Remove subcommand
                    if(args.length == 2) {
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            if(!sender.hasPermission("totalcomputers.manage.crafted")
                                    || !playerOwns((Player) sender, args[1])) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        Localization.get(91));
                                return true;
                            }
                        }
                        if(!registeredComputers.contains(args[1])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(92)+args[1]+Localization.get(93));
                            return true;
                        }
                        removeComputer(args[1]);
                        computers.set("computers."+args[1], null);
                        computers.set("computers.names", registeredComputers);
                        configManager.saveConfig("computers.yml", true);
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(94)+args[1]+Localization.get(95));
                        increaseUnregComputers((Player) sender);
                        removeOwner((Player) sender, args[1]);
                    } else if(args.length > 2) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(96));
                    } else invalidUsage(sender);
                }
                else if(args[0].equalsIgnoreCase("list")) { // List subcommand
                    List<String> comps;
                    if(!sender.hasPermission("totalcomputers.manage.all")) {
                        if(!sender.hasPermission("totalcomputers.manage.crafted")) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(97));
                            return true;
                        }

                        comps = new ArrayList<>();
                        for(String comp : registeredComputers) {
                            if(playerOwns((Player) sender, comp)) comps.add(comp);
                        }
                    } else comps = registeredComputers;
                    if(comps.isEmpty()) {
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(98));
                        return true;
                    }
                    StringBuilder list = new StringBuilder();
                    for(String name : comps) list.append(name).append(", ");
                    list.delete(list.length()-2, list.length()-1);
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(99) + ChatColor.RESET + list);
                }
                else if(args[0].equalsIgnoreCase("wand")) { // Wand subcommand
                    ItemStack wand = new ItemStack(Material.STICK, 1);
                    ItemMeta meta = wand.getItemMeta();
                    meta.setDisplayName("TotalComputers");
                    List<String> lore = new ArrayList<>();
                    lore.add(Localization.get(153));
                    meta.setLore(lore);
                    wand.setItemMeta(meta);
                    ((Player) sender).getInventory().addItem(wand);
                }
                else if(args[0].equalsIgnoreCase("release")) { // Release subcommand
                    if(!locked.containsKey((Player)sender)) {
                        invalidUsage(sender);
                        return true;
                    }
                    forceStopCapture(locked.get((Player)sender));
                }
                else if(args[0].equalsIgnoreCase("token")) { // Token subcommand
                    String token;
                    if(!tokens.containsKey((Player)sender)
                        || args.length > 1 && args[1].equalsIgnoreCase("reset")) {
                        String oldToken = tokens.getOrDefault((Player)sender, null);
                        if(oldToken != null)
                            server.unregisterToken(oldToken);
                        if(tokens.size() >= Integer.MAX_VALUE) token = ChatColor.RED+Localization.get(154);
                        else {
                            while (true) {
                                token = RandomStringUtils.randomAlphanumeric(6);
                                boolean found = false;
                                for (String t : tokens.values()) {
                                    if (t.equals(token)) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (found) continue;
                                break;
                            }
                            server.registerToken(token, (Player)sender);
                            tokens.put((Player)sender, token);
                        }
                    } else token = tokens.get((Player)sender);
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(100)+
                            ChatColor.LIGHT_PURPLE+token);
                }
                else if(args[0].equalsIgnoreCase("data")) { // Data subcommand
                    if(args.length == 2) {
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            if(!sender.hasPermission("totalcomputers.manage.crafted")
                                    || !playerOwns((Player) sender, args[1])) {
                                sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(101));
                                return true;
                            }
                        }
                        if(!registeredComputers.contains(args[1])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(102)+args[1]+Localization.get(103));
                            return true;
                        }
                        SelectionArea area = computersPhysicalData.get(args[1]);
                        sender.sendMessage(replyPrefix + Localization.get(104));
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(105) + ChatColor.RESET + args[1]);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(106) + ChatColor.RESET);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(107) + ChatColor.RESET + area.firstPos.getBlockX());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(108) + ChatColor.RESET + area.firstPos.getBlockY());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(109) + ChatColor.RESET + area.firstPos.getBlockZ());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(110) + ChatColor.RESET);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(111) + ChatColor.RESET + area.secondPos.getBlockX());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(112) + ChatColor.RESET + area.secondPos.getBlockY());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(113) + ChatColor.RESET + area.secondPos.getBlockZ());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(114) + ChatColor.RESET + area.axis);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(115) + ChatColor.RESET + area.direction);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(116) + ChatColor.RESET + area.width);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(117) + ChatColor.RESET + area.height);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(118) + ChatColor.RESET + area.area);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + Localization.get(119) + ChatColor.RESET + area.firstPos.getWorld().getName());
                    } else if(args.length > 2) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(120));
                    } else invalidUsage(sender);
                } else {
                    invalidUsage(sender);
                }
            }
        } else sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(121));
        return true;
    }

    /**
     * Tab autocompletion of command. Permission: <code>totalcomputers.command.totalcomputers</code>
     * @param sender Command sender
     * @param command Command
     * @param alias Alias
     * @param args Command Arguments
     * @return List of autocompletes
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      String[] args) {
        if(!computersInitialized) return new ArrayList<>();
        List<String> variants = new ArrayList<>();
        if(!(sender instanceof Player player)) return variants;
        if(!sender.hasPermission("totalcomputers.command.totalcomputers")) return variants;
        String[] all = {};
        if(command.getName().equalsIgnoreCase("totalcomputers")) {
            if(args.length == 1) {
                if(locked.containsKey(player)) all = new String[]{"help", "sound", "create", "remove", "selection", "list",
                        "reload", "data", "wand", "paste", "erase", "release", "token", "client"};
                else all = new String[]{"help", "sound", "create", "remove", "selection", "list",
                        "reload", "data", "wand", "paste", "erase", "token", "client"};
            }
            else if(args[0].equalsIgnoreCase("token")) {
                if(args.length == 2) all = new String[] { "reset" };
            }
            else if(args[0].equalsIgnoreCase("client")) {
                if(args.length == 2) {
                    all = new String[] {"bind", "unbind"};
                } else if(args.length == 3) {
                    all = new String[0];
                    if(args[1].equalsIgnoreCase("bind")) {
                        List<String> comps;
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            comps = new ArrayList<>();
                            if(sender.hasPermission("totalcomputers.manage.crafted")) {
                                for(String comp : registeredComputers) {
                                    if(playerOwns(player, comp)) comps.add(comp);
                                }
                            }
                        } else {
                            comps = registeredComputers;
                        }
                        all = new String[comps.size()];
                        comps.toArray(all);
                    } else if(args[1].equalsIgnoreCase("unbind")) {
                        Set<String> comps;
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            comps = new HashSet<>();
                            if(sender.hasPermission("totalcomputers.manage.crafted")) {
                                for(String comp : RemoteOS.allNames()) {
                                    if(playerOwns(player, comp)) comps.add(comp);
                                }
                            }
                        } else {
                            comps = RemoteOS.allNames();
                        }
                        all = new String[comps.size()];
                        comps.toArray(all);
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("selection")) {
                if(args.length == 2) {
                    all = new String[] { "enable", "disable", "toggle", "state" };
                }
            }
            else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("data")) {
                List<String> comps;
                if(!sender.hasPermission("totalcomputers.manage.all")) {
                    comps = new ArrayList<>();
                    if(sender.hasPermission("totalcomputers.manage.crafted")) {
                        for(String comp : registeredComputers) {
                            if(playerOwns(player, comp)) comps.add(comp);
                        }
                    }
                } else {
                    comps = registeredComputers;
                }
                all = new String[comps.size()];
                comps.toArray(all);
            }
        }
        for(String str : all) {
            if(str.startsWith(args[args.length-1])) variants.add(str);
        }
        if(variants.isEmpty()) return null;
        return variants;
    }

    /* *************** CODE SECTION: EVENTS *************** */

    @EventHandler(priority = EventPriority.HIGH)
    public void craftEvent(CraftItemEvent e) {
        if(!computersInitialized) return;
        if(!(e.getWhoClicked() instanceof Player player)) return;
        ItemStack result = e.getRecipe().getResult();
        ItemMeta meta = result.getItemMeta();
        if(meta == null) return;
        String name = meta.getDisplayName();
        if(!name.equals(Localization.get(122))) return;
        List<String> lore = meta.getLore();
        if(lore == null || lore.size() != 4) return;
        if(!lore.get(0).equals(Localization.get(123))) return;
        if(!lore.get(1).equals(Localization.get(124))) return;
        if(!lore.get(2).equals(Localization.get(125))) return;
        if(!lore.get(3).equals(Localization.get(126))) return;
        increaseUnregComputers(player);
        for(String msg : lore) player.sendMessage(replyPrefix + ChatColor.GREEN + msg);
    }

    /**
     * Processes selection with wand. Permission:  <code>totalcomputers.selection</code>
     * @param event Event (PlayerInteractEvent)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void selectionEvent(PlayerInteractEvent event) {
        if(!computersInitialized) return;
        if(!isSelectionEnabled()) return;
        if(!is1_8)
            if(event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        if(!player.hasPermission("totalcomputers.selection")
                && !player.hasPermission("totalcomputers.place"))
            return;
        ItemStack itemInHand;
        if(is1_8) itemInHand = player.getInventory().getItemInHand();
        else itemInHand = player.getInventory().getItemInMainHand();
        if(!itemInHand.getType().equals(Material.STICK)) return;
        ItemMeta meta = itemInHand.getItemMeta();
        if(meta == null) return;
        if(!meta.getDisplayName().equals("TotalComputers")) return;
        List<String> lore = meta.getLore();
        if(lore == null || lore.size() != 1) return;
        if(!lore.get(0).equals(Localization.get(153))) return;
        Action action = event.getAction();
        Location loc, oldLoc = null;
        boolean displayArea = false;
        if(action.equals(Action.LEFT_CLICK_BLOCK)) {
            if(firstPoses.containsKey(player)) oldLoc = firstPoses.get(player);
            firstPoses.remove(player);
            firstPoses.put(player, loc = event.getClickedBlock().getLocation());
            event.setCancelled(true);
            if(oldLoc == null || !oldLoc.equals(loc)) {
                player.sendMessage(replyPrefix + ChatColor.LIGHT_PURPLE + Localization.get(127)+loc.getBlockX()+Localization.get(128)+loc.getBlockY()+Localization.get(129)+loc.getBlockZ()+Localization.get(130));
            }
            if(secondPoses.containsKey(player)) computeArea(player);
        } else if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if(secondPoses.containsKey(player)) oldLoc = secondPoses.get(player);
            secondPoses.remove(player);
            secondPoses.put(player, loc = event.getClickedBlock().getLocation());
            event.setCancelled(true);
            if(oldLoc == null || !oldLoc.equals(loc)) {
                player.sendMessage(replyPrefix + ChatColor.LIGHT_PURPLE + Localization.get(131)+loc.getBlockX()+Localization.get(132)+loc.getBlockY()+Localization.get(133)+loc.getBlockZ()+Localization.get(134));
            }
            if(firstPoses.containsKey(player)) computeArea(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoin(PlayerJoinEvent event) {
        if(!computersInitialized) return;
        if(event.getPlayer().isOp()
            || event.getPlayer().hasPermission("totalcomputers.plugin.manage")
            || event.getPlayer().hasPermission("totalcomputers.admin")) {
            try {
                String update = checkUpdates();
                if (update != null) {
                    event.getPlayer().sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(156) + ChatColor.RESET + update);
                    event.getPlayer().sendMessage(replyPrefix + ChatColor.GREEN + Localization.get(157) + ChatColor.BLUE + "https://github.com/JNNGL/TotalComputers/releases\n");
                }
            } catch (IOException ignored) {
                event.getPlayer().sendMessage(replyPrefix + ChatColor.RED + Localization.get(155));
            }
        }

        String player = event.getPlayer().getName();
        if (slotsToRestore.containsKey(player)) {
            SlotControl slot = slotsToRestore.get(player);
            event.getPlayer().getInventory().setItem(slot.s1, slot.first);
            event.getPlayer().getInventory().setItem(slot.s2, slot.second);
            event.getPlayer().getInventory().setItem(slot.s3, slot.third);
            slotsToRestore.remove(player);
        }
        if (playersThatQuitWhileControl.contains(player)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(this,
                    () -> Bukkit.getPlayer(player).getVehicle().remove(), 10);
            playersThatQuitWhileControl.remove(player);
        }
    }

    /**
     * Clean up when player quits the server
     * @param event Event (PlayerQuitEvent)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeave(PlayerQuitEvent event) {
        if(!computersInitialized) return;
        Player player = event.getPlayer();
        if(tokens.containsKey(player)) {
            server.unregisterToken(tokens.get(player));
            tokens.remove(player);
        }
        if(locked.containsKey(player)) {
            playersThatQuitWhileControl.add(player.getName());
        }
        if(slots.containsKey(player)) {
            slotsToRestore.put(player.getName(), slots.get(player));
        }
        if(locked.containsKey(player)) {
            TotalOS target = locked.get(player);
            stopCapture(target);
            logger.info("Player "+player.getName()+" left the game. Capture stopped for "+target.name+" computer.");
        }
        firstPoses.remove(player);
        secondPoses.remove(player);
        areas.remove(player);
    }

    /**
     * Protection and input handling
     * @param event Event (EntityDamageByEntityEvent)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(EntityDamageByEntityEvent event) {
        if(!computersInitialized) return;
        if(event.getEntity().getType() == EntityType.ITEM_FRAME) {
            if(interactiveTiles.containsKey(((ItemFrame) event.getEntity()))) {
                if(event.getDamager() instanceof Player)
                    processInput((Player) event.getDamager(), InputInfo.InteractType.LEFT_CLICK);
                event.setCancelled(true);
            }
        }
    }

    /**
     * Protection and input handling
     * @param event Event (PlayerInteractEntityEvent)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEntityEvent event) {
        if(!computersInitialized) return;
        if(event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            if(interactiveTiles.containsKey(((ItemFrame) event.getRightClicked()))) {
                processInput(event.getPlayer(), InputInfo.InteractType.RIGHT_CLICK);
                event.setCancelled(true);
            }
        }
    }

    /**
     * Protection and input handling
     * @param event Event (PlayerInteractEvent)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void mapInteract(PlayerInteractEvent event) {
        if(!computersInitialized) return;
        if(!is1_8)
            if(event.getHand() != EquipmentSlot.HAND) return;
        InputInfo.InteractType interactType;
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) interactType = InputInfo.InteractType.LEFT_CLICK;
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) interactType = InputInfo.InteractType.RIGHT_CLICK;
        else return;
        if(processInput(event.getPlayer(), interactType)) event.setCancelled(true);
    }

    /**
     * Clean up on plugin disable
     */
    @Override
    public void onDisable() {
        server.shutdown();
        if(TotalOS.audio != null) {
            TotalOS.audio.jda.shutdownNow();
        }
        for(TotalOS os : systems.values())
            forceStopCapture(os);
        String[] computers = new String[registeredComputers.size()];
        registeredComputers.toArray(computers);
        if(recipe != null) {
            getServer().removeRecipe(recipe);
        }
        for(String computer : computers)
            removeComputer(computer);
        logger.info("Total Computers disabled.");
        try {
            SoundWebSocketServer.shutdown();
            SoundWebServer.shutdown();
        } catch (Exception e) {
            logger.warning("Unable to shutdown server");
            logger.warning(e.getMessage());
        }
    }

    /* *************** CODE SECTION: HELPERS *************** */

    private TotalOS getKeyboardHandle(Player sender) {
        String nearest = null;
        double nearestD = Integer.MAX_VALUE;
        for(String name : computersPhysicalData.keySet()) {
            SelectionArea area = computersPhysicalData.get(name);
            Vector center = area.firstPos.add(area.secondPos).multiply(0.5).toVector();
            double d = center.distanceSquared(sender.getLocation().toVector());
            if(d < nearestD) {
                nearestD = d;
                nearest = name;
            }
        }

        if(nearestD > 100) {
            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(135));
            return null;
        }

        TotalOS os = systems.get(nearest);
        if(os.keyboard == null || !os.keyboard.isControlTaken()) {
            sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(136));
            return null;
        }

        return os;
    }

    public void increaseUnregComputers(Player p) {
        String path = p.getName()+".free";
        configManager.reloadConfig("players.yml");
        players = configManager.getFileConfig("players.yml");
        if(!players.isSet(path)) {
            players.set(path, 1);
        } else {
            players.set(path, players.getInt(path)+1);
        }
        configManager.saveConfig("players.yml", true);
    }

    public void decreaseUnregComputers(Player p) {
        String path = p.getName()+".free";
        configManager.reloadConfig("players.yml");
        players = configManager.getFileConfig("players.yml");
        if(!players.isSet(path)) {
            players.set(path, 0);
        } else {
            players.set(path, players.getInt(path)-1);
        }
        configManager.saveConfig("players.yml", true);
    }

    public int getUnregComputers(Player p) {
        String path = p.getName()+".free";
        configManager.reloadConfig("players.yml");
        players = configManager.getFileConfig("players.yml");
        if(!players.isSet(path)) return 0;
        return players.getInt(path);
    }

    public boolean playerOwns(Player p, String name) {
        if(!registeredComputers.contains(name)) return false;
        String path = p.getName()+".owns";
        configManager.reloadConfig("players.yml");
        players = configManager.getFileConfig("players.yml");
        if(!players.isSet(path)) return false;
        return players.getStringList(path).contains(name);
    }

    public void addOwner(Player p, String name) {
        String path = p.getName()+".owns";
        List<String> owns;
        configManager.reloadConfig("players.yml");
        players = configManager.getFileConfig("players.yml");
        if(!players.isSet(path)) {
            owns = new ArrayList<>();
        } else {
            owns = players.getStringList(path);
        }
        if(!owns.contains(name)) {
            owns.add(name);
            players.set(path, owns);
            configManager.saveConfig("players.yml", true);
        }
    }

    public void removeOwner(Player p, String name) {
        String path = p.getName()+".owns";
        List<String> owns;
        configManager.reloadConfig("players.yml");
        players = configManager.getFileConfig("players.yml");
        if(!players.isSet(path)) {
            owns = new ArrayList<>();
        } else {
            owns = players.getStringList(path);
        }
        if(owns.contains(name)) {
            owns.remove(name);
            players.set(path, owns);
            configManager.saveConfig("players.yml", true);
        }
    }

    /**
     * Removes the computer
     * @param name Name of computer to remove
     */
    private void removeComputer(String name) {
        RemoteOS remote = RemoteOS.fromName(name);
        if(remote != null) {
            remote.destroy();
        }
        stopCapture(systems.get(name));
        tasks.get(name).cancel();
        tasks.remove(name);
        List<MonitorPiece> monitor = monitors.get(name);
        for(MonitorPiece piece : monitor) {
            final MapView map = piece.mapView;
            final ItemFrame frame = piece.frame;

            interactiveTiles.remove(frame);
            frame.remove();
        }
        List<InputInfo> toRemove = new ArrayList<>();
        for(InputInfo inputInfo : unhandledInputs) {
            if(inputInfo.index().name.equals(name))
                toRemove.add(inputInfo);
        }
        packets.remove(systems.get(name));
        toRemove.forEach(unhandledInputs::remove);
        registeredComputers.remove(name);
        computersPhysicalData.remove(name);
        monitors.remove(name);
    }

    /**
     * Process the input
     * @param entity Player
     * @param interactType Type of interaction
     * @return Whether the input was handled
     */
    private boolean processInput(Player entity, InputInfo.InteractType interactType) {
        Block block = entity.getTargetBlock(null, 100);
        Location blockLoc = block.getLocation();
        AABB aabb = new AABB(blockLoc.toVector().add(new Vector(0.5D, 0.5D, 0.5D)), -0.5625D, 0.5625D, -0.5625D, 0.5625D, -0.5625D, 0.5625D);
        Vector intersection = aabb.getIntersection(entity.getEyeLocation().toVector(), entity.getEyeLocation().toVector().add(entity.getLocation().getDirection().multiply(10)));
        if(intersection != null) {

            for(ItemFrame tile : interactiveTiles.keySet()) {

                Vector displacement = null;
                if(tile.getFacing() == BlockFace.SOUTH) displacement = new Vector(0, 0, 1);
                else if(tile.getFacing() == BlockFace.NORTH) displacement = new Vector(0, 0, -1);
                else if(tile.getFacing() == BlockFace.WEST) displacement = new Vector(-1, 0, 0);
                else if(tile.getFacing() == BlockFace.EAST) displacement = new Vector(1, 0, 0);
                if(displacement != null) {
                    Location expLoc = blockLoc.clone().add(displacement);
                    Location tileLoc = tile.getLocation();
                    if(expLoc.getWorld().getName().equals(tileLoc.getWorld().getName())
                            && expLoc.getBlockX() == tileLoc.getBlockX()
                            && expLoc.getBlockY() == tileLoc.getBlockY()
                            && expLoc.getBlockZ() == tileLoc.getBlockZ()) {
                        double u = 0, v = 0;
                        if(tile.getFacing() == BlockFace.SOUTH) {
                            u = intersection.getX() - expLoc.getBlockX();
                            v = 1.f - (intersection.getY() - expLoc.getBlockY());
                        }
                        else if(tile.getFacing() == BlockFace.NORTH) {
                            u = 1.f - (intersection.getX() - expLoc.getBlockX());
                            v = 1.f - (intersection.getY() - expLoc.getBlockY());
                        }
                        else if(tile.getFacing() == BlockFace.WEST) {
                            u = intersection.getZ() - expLoc.getBlockZ();
                            v = 1.f - (intersection.getY() - expLoc.getBlockY());
                        }
                        else if(tile.getFacing() == BlockFace.EAST) {
                            u = 1.f - (intersection.getZ() - expLoc.getBlockZ());
                            v = 1.f - (intersection.getY() - expLoc.getBlockY());
                        }
                        int subX = (int)(u*128);
                        int subY = (int)(v*128);
                        boolean hasPerm = entity.hasPermission("totalcomputers.use");
                        ItemStack itemInHand;
                        if(is1_8) itemInHand = entity.getInventory().getItemInHand();
                        else itemInHand = entity.getInventory().getItemInMainHand();
                        boolean nHoldItem = itemInHand.getType() == Material.AIR || itemInHand.getType() == Material.BARRIER;
                        if(hasPerm && nHoldItem) {
                            unhandledInputs.add(new InputInfo(interactiveTiles.get(tile), subX, subY, interactType, entity));
                        } else if(!hasPerm) {
                            entity.sendMessage(replyPrefix + ChatColor.GRAY + Localization.get(137));
                        } else {
                            entity.sendMessage(replyPrefix + ChatColor.GRAY + Localization.get(138));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Prints message about invalid usage of command
     * @param sender Command executor
     */
    private void invalidUsage(CommandSender sender) {
        sender.sendMessage(replyPrefix + ChatColor.RED + Localization.get(139));
    }

    /**
     * @return Whether the selection is enabled
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isSelectionEnabled() {
        return config.getBoolean("selection");
    }

    /**
     * Calculates information about selection: axis, direction, width, height etc. (See record SelectionArea)
     * @param player Player
     */
    private void computeArea(Player player) {
        SelectionArea.Axis axis;
        SelectionArea.Direction direction;
        Location first = firstPoses.get(player);
        Location second = secondPoses.get(player);
        if(!first.getWorld().getName().equals(second.getWorld().getName())) {
            player.sendMessage(replyPrefix + ChatColor.RED + Localization.get(140));
            return;
        }
        if (first.getBlockX() == second.getBlockX()) axis = SelectionArea.Axis.X;
        else if (first.getBlockY() == second.getBlockY()) axis = SelectionArea.Axis.Y;
        else if (first.getBlockZ() == second.getBlockZ()) axis = SelectionArea.Axis.Z;
        else {
            player.sendMessage(replyPrefix + ChatColor.RED + Localization.get(141));
            return;
        }

        Location loc = player.getLocation();

        int width, height;

        if(axis == SelectionArea.Axis.X) {
            double x = loc.getX();
            direction = (x < first.getBlockX())? SelectionArea.Direction.LEFT : SelectionArea.Direction.RIGHT;
            width = Math.abs(first.getBlockZ() - second.getBlockZ())+1;
            height = Math.abs(first.getBlockY() - second.getBlockY())+1;
        } else if(axis == SelectionArea.Axis.Y) {
            double y = loc.getY();
            direction = (y < first.getBlockY())? SelectionArea.Direction.DOWN : SelectionArea.Direction.UP;
            width = Math.abs(first.getBlockX() - second.getBlockX())+1;
            height = Math.abs(first.getBlockZ() - second.getBlockZ())+1;
        } else {
            double z = loc.getZ();
            direction = (z < first.getBlockZ())? SelectionArea.Direction.BACKWARD : SelectionArea.Direction.FORWARD;
            width = Math.abs(first.getBlockX() - second.getBlockX())+1;
            height = Math.abs(first.getBlockY() - second.getBlockY())+1;
        }

        int area = width * height;
        areas.remove(player);
        if(area == 1) {
            player.sendMessage(replyPrefix + ChatColor.RED + Localization.get(142));
            return;
        }
        areas.put(player, new SelectionArea(first, second, axis, direction, width, height, area));
        player.sendMessage(replyPrefix + ChatColor.LIGHT_PURPLE + Localization.get(143)+direction+Localization.get(144)+area+Localization.get(145)+width+Localization.get(146)+height+Localization.get(147));
    }

    /**
     * Creates monitor piece
     * @param name Name of the computer
     * @param area Physical data of the computer
     * @param x1 World X
     * @param y1 World Y
     * @param z1 World Z
     * @param displacement Displacement
     * @param monitorPieces Monitor
     * @param i Index of the piece
     */
    private int proceedMonitorPiece(TotalOS os, String name, SelectionArea area, int x1, int y1, int z1, Vector displacement, List<MonitorPiece> monitorPieces, int[] i) {
        Location loc = new Location(area.firstPos.getWorld(), x1, y1, z1).add(displacement);

        final MapView map = Bukkit.createMap(loc.getWorld());
        map.setScale(MapView.Scale.FARTHEST);

        Material material;
        try {
            Field filledMap = Material.class.getDeclaredField("FILLED_MAP");
            filledMap.setAccessible(true);
            material = (Material) filledMap.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            material = Material.MAP;
        }
        ItemStack is = null;
        Object mapId = 0;
        try {
            mapId = map.getClass().getMethod("getId").invoke(map);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(isLegacy) {
            is = new ItemStack(material, 1, (Short) mapId );
        }
        else {
            is = new ItemStack(material, 1);

            try {
                MapMeta meta = (MapMeta) is.getItemMeta();
                Method setMapId = MapMeta.class.getMethod("setMapId", int.class);
                setMapId.invoke(meta, (int)mapId);
                is.setItemMeta(meta);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                System.err.println("Failed to assign map id");
            }
        }

        ItemFrame f = null;
        try {
            for(Entity entity : loc.getWorld().getEntitiesByClasses(ItemFrame.class)) {
                if(entity.getLocation().getBlockX() == loc.getBlockX() && entity.getLocation().getBlockY() == loc.getBlockY() && entity.getLocation().getBlockZ() == loc.getBlockZ()) {
                    f = (ItemFrame) entity;
                    break;
                }
            }
            if(f == null) {
                f = area.firstPos.getWorld().spawn(loc, ItemFrame.class);
                BlockFace face;
                if(area.direction == SelectionArea.Direction.LEFT) face = BlockFace.WEST;
                else if(area.direction == SelectionArea.Direction.RIGHT) face = BlockFace.EAST;
                else if(area.direction == SelectionArea.Direction.BACKWARD) face = BlockFace.NORTH;
                else face = BlockFace.SOUTH;
                if(invisibleFrames)
                    f.setVisible(false);
                f.setFacingDirection(face, true);
            }

        } catch (Exception ignored) {}

        if(f != null) {
            f.setItem(is);
            interactiveTiles.put(f, new MonitorPieceIndex(name, i[0]));
            monitorPieces.add(new MonitorPiece(map, f));
        } else logger.info("Failed to init computer: Unable to create or load item frame.");

        i[0]++;
        return (mapId instanceof Short)? (short)mapId : (int)mapId;
    }

    /**
     * Initializes the computer
     * @param name Name of the computer to initialize
     */
    private void initComputer(String name) {
        final SelectionArea area = computersPhysicalData.get(name);

        Vector displacement;

        int topRightX = Math.max(area.firstPos.getBlockX(), area.secondPos.getBlockX()),
                topRightY = Math.max(area.firstPos.getBlockY(), area.secondPos.getBlockY()),
                topRightZ = Math.max(area.firstPos.getBlockZ(), area.secondPos.getBlockZ()),
                downLeftX = Math.min(area.firstPos.getBlockX(), area.secondPos.getBlockX()),
                downLeftY = Math.min(area.firstPos.getBlockY(), area.secondPos.getBlockY()),
                downLeftZ = Math.min(area.firstPos.getBlockZ(), area.secondPos.getBlockZ());

        if(area.direction == SelectionArea.Direction.RIGHT) displacement = new Vector(1, 0, 0);
        else if(area.direction == SelectionArea.Direction.LEFT) displacement = new Vector(-1, 0, 0);
        else if(area.direction == SelectionArea.Direction.FORWARD) displacement = new Vector(0, 0, 1);
        else if(area.direction == SelectionArea.Direction.BACKWARD) displacement = new Vector(0, 0, -1);
        else displacement = new Vector();

        final int[] i = {0};

        List<MonitorPiece> monitorPieces = new ArrayList<>();

        boolean invert = area.direction == SelectionArea.Direction.BACKWARD || area.direction == SelectionArea.Direction.RIGHT;

        TotalOS os = new TotalOS(area.width*128, area.height*128, name);
        os.motionCapture = this;

        int[] maps = new int[area.area];

        for(int y1 = topRightY; y1 >= downLeftY; y1--) {
            if(invert) {
                for (int x1 = topRightX; x1 >= downLeftX; x1--) {
                    for (int z1 = topRightZ; z1 >= downLeftZ; z1--) {
                        maps[i[0]] = proceedMonitorPiece(os, name, area, x1, y1, z1, displacement, monitorPieces, i);
                    }
                }
            } else {
                for (int x1 = downLeftX; x1 <= topRightX; x1++) {
                    for (int z1 = downLeftZ; z1 <= topRightZ; z1++) {
                        maps[i[0]] = proceedMonitorPiece(os, name, area, x1, y1, z1, displacement, monitorPieces, i);
                    }
                }
            }
        }

        if(sender != null) {

            Object[] frame1 = new Object[area.area], frame2 = new Object[area.area],
                    frame3 = new Object[area.area], frame4 = new Object[area.area];
            BufferedImage empty = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
            for(int j = 0; j < area.area; j++) {
                try {
                    frame1[j] = sender.createPacket(maps[j], empty);
                    frame2[j] = sender.createPacket(maps[j], empty);
                    frame3[j] = sender.createPacket(maps[j], empty);
                    frame4[j] = sender.createPacket(maps[j], empty);
                } catch (ReflectiveOperationException e) {
                    logger.warning("Failed to create packet");
                }
            }
            packets.put(os, new DoubleBuffer(frame1, frame2, frame3, frame4));

            int[] uncaught = { -2 };
            Throwable[] prevException = { null };
            tasks.put(os.name, Bukkit.getScheduler().runTaskTimer(this, () -> {
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    try {
                        Object screen;
                        RemoteOS remote = RemoteOS.fromName(os.name);
                        if(remote != null) screen = remote.getBuffer();
                        else if(allowServerboundComputers) screen = os.renderFrame();
                        else return;

                        if(screen == null) throw new Exception("Failed to render screen");

                        Object[] framePacket = packets.get(os).get();

                        for (int x = 0; x < area.width; x++) {
                            for (int y = 0; y < area.height; y++) {
                                int id = y * area.width + x;
                                int absX = x * 128;
                                int absY = y * 128;
                                try {
                                    if(screen instanceof BufferedImage)
                                        sender.modifyPacket(framePacket[id],
                                                ((BufferedImage)screen).getSubimage(absX, absY, 128, 128));
                                    else {
                                        final byte[] cbBuf = new byte[128*128];
                                        System.arraycopy((byte[])screen, id*128*128, cbBuf, 0, cbBuf.length);
                                        sender.modifyPacket(framePacket[id], cbBuf);
                                    }
                                } catch (ReflectiveOperationException e) {
                                    logger.warning("Failed to create packet");
                                    logger.warning(" -> " + e.getMessage());
                                }
                            }
                        }

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if(!player.getWorld().getName().equals(area.firstPos.getWorld().getName())) continue;
                            if(player.getLocation().distanceSquared(area.firstPos) > 1024) continue;
                            for (int id = 0; id < area.area; id++) {
                                try {
                                    sender.sendPacket(player, framePacket[id]);
                                } catch (ReflectiveOperationException e) {
                                    logger.warning("Failed to create packet");
                                    logger.warning(" -> " + e.getMessage());
                                }
                            }
                        }

                        if(os.getState().equals(TotalOS.ComputerState.RUNNING)) {
                            if (uncaught[0] > 0 && prevException[0] != null) {
                                System.err.println("An error occurred in the OS/application. This did not affect the operation of the OS, but if something does not work properly, create an issue on GitHub. In other cases, this error can be ignored.");
                                System.err.println("Stack Trace:");
                                prevException[0].printStackTrace();
                                prevException[0] = null;
                            }
                        }
                        uncaught[0] = 0;
                    } catch(Throwable e) {
                        if(uncaught[0] < 0) {
                            uncaught[0]++;
                        } else {
                            if (e instanceof OutOfMemoryError) {
                                os.invokeBSoD("Not enough RAM", new Throwable(e), 0x03);
                            } else if (uncaught[0] / (delay == 0 ? 60 : (20 / delay)) >= 3) {
                                os.invokeBSoD("Critical Error", new Throwable(e), 0x01);
                                prevException[0] = null;
                                System.err.println("Critical Error -> Uncaught exception: " + e.getMessage());
                            } else {
                                uncaught[0]++;
                                prevException[0] = e;
                            }
                        }
                    }
                });

                List<TotalComputers.InputInfo> handledInputs = new ArrayList<>();
                for (int x = 0; x < area.width; x++) {
                    for (int y = 0; y < area.height; y++) {
                        int id = y * area.width + x;
                        int absX = x * 128;
                        int absY = y * 128;
                        for (TotalComputers.InputInfo inputInfo : unhandledInputs.toArray(new InputInfo[0])) {
                            if (inputInfo.index().name().equals(name) && inputInfo.index().index() == id) {
                                executors.put(os, inputInfo.player);
                                RemoteOS remote = RemoteOS.fromName(os.name);
                                if(remote != null) remote.sendTouchEvent(absX + inputInfo.x(), absY + inputInfo.y(),
                                        inputInfo.interactType(),
                                        inputInfo.player().hasPermission("totalcomputers.admin"));
                                else if(allowServerboundComputers)
                                    os.processTouch(absX + inputInfo.x(), absY + inputInfo.y(),
                                        inputInfo.interactType(),
                                        inputInfo.player().hasPermission("totalcomputers.admin"));
                                executors.remove(os);
                                handledInputs.add(inputInfo);
                            }
                        }
                    }
                }
                handledInputs.forEach(unhandledInputs::remove);
            }, 0, delay));

        }

        systems.put(name, os);
        monitors.remove(name);
        monitors.put(name, monitorPieces);
    }

}
