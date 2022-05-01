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
import com.jnngl.totalcomputers.motion.MotionCapabilities;
import com.jnngl.totalcomputers.motion.MotionCapture;
import com.jnngl.totalcomputers.motion.MotionCaptureDesc;
import com.jnngl.totalcomputers.sound.SoundWebServer;
import com.jnngl.totalcomputers.sound.SoundWebSocketServer;
import com.jnngl.totalcomputers.system.TotalOS;
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
import org.spigotmc.event.entity.EntityDismountEvent;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    /** Unhandled touch inputs */ public List<InputInfo> unhandledInputs;
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

    @Override
    public boolean startCapture(MotionCaptureDesc desc, TotalOS os) {
        if(!executors.containsKey(os)) {
            logger.warning("Motion Capture: Target not found.");
            return false;
        }
        if(targets.containsKey(os)) {
            logger.warning("Motion capture is busy.");
            executors.get(os).sendMessage(replyPrefix + ChatColor.RED + targets.get(os).target.getName() + " is already using this feature on this computer.");
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

        target.sendMessage(replyPrefix + ChatColor.BLUE + "You are now using motion capture!");
        target.sendMessage(replyPrefix + ChatColor.BLUE + "Configuration: ");
        target.sendMessage(replyPrefix + ChatColor.BLUE + "  Movement: " + (desc.requiresMovementCapture()? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));
        target.sendMessage(replyPrefix + ChatColor.BLUE + "  Gaze: " + (desc.requiresGazeDirectionCapture()? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));
        target.sendMessage(replyPrefix + ChatColor.BLUE + "  Jump: " + (desc.requiresJumpCapture()? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));
        target.sendMessage(replyPrefix + ChatColor.BLUE + "  Sneak: " + (desc.requiresSneakCapture()? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));
        target.sendMessage(replyPrefix + ChatColor.BLUE + "  Slot: " + (desc.requiresSlotCapture()? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));
        target.sendMessage(replyPrefix + ChatColor.BLUE + "  Item Drop: " + (desc.requiresItemDropCapture()? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));
        target.sendMessage(replyPrefix + ChatColor.BLUE + "Type "+ChatColor.GREEN+"/tcmp release"+ChatColor.BLUE+" to stop it");

        Arrow arrow = target.getWorld().spawn(target.getLocation(), Arrow.class);
        arrow.setGravity(false);
        arrow.setInvulnerable(true);
        arrow.setPassenger(target);

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

    @Override
    public boolean stopCapture(TotalOS os) {
        if(!executors.containsKey(os)) return false;
        if(executors.get(os).getName().equals(targets.get(os).target.getName())) {
            forceStopCapture(os);
            return true;
        }
        logger.warning("Motion capture is busy.");
        executors.get(os).sendMessage(replyPrefix + ChatColor.RED + targets.get(os).target.getName() + " is already using this feature on this computer.");
        return false;
    }

    @Override
    public void forceStopCapture(TotalOS os) {
        if(!targets.containsKey(os)) return;
        if(slots.containsKey(targets.get(os).target)) {
            Inventory inv = targets.get(os).target.getInventory();
            SlotControl event = slots.get(targets.get(os).target);
            event.task.cancel();
            inv.setItem(event.s1, event.first);
            inv.setItem(event.s2, event.second);
            inv.setItem(event.s3, event.third);
            slots.remove(targets.get(os).target);
        }
        drop.remove(targets.get(os).target);
        PacketListener.removePlayer(targets.get(os).target);
        Entity vehicle = targets.get(os).target.getVehicle();
        if(vehicle != null) {
            vehicle.getPassengers().remove(targets.get(os).target);
            vehicle.remove();
        }
        targets.get(os).target.sendMessage(replyPrefix + ChatColor.BLUE +
                "You are no longer using the capture feature.");
        locked.remove(targets.get(os).target);
        targets.remove(os);
    }

    @Override
    public boolean isCapturing(TotalOS os) {
        return targets.containsKey(os);
    }

    @EventHandler
    public void drop(PlayerDropItemEvent e) {
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

    @EventHandler
    public void dismount(EntityDismountEvent e) {
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
        private final Object[] frame1;
        private final Object[] frame2;
        private byte current = 0;

        public DoubleBuffer(Object[] frame1, Object[] frame2) {
            this.frame1 = frame1;
            this.frame2 = frame2;
        }

        public Object[] get() {
            if(current == 0) {
                current = 1;
                return frame1;
            } else {
                current = 0;
                return frame2;
            }
        }

    }

    /**
     * Information about part of the computer
     */
    public static record MonitorPieceIndex(String name, int index) {}

    /* *************** CODE SECTION: INITIALIZATION *************** */

    /**
     * Initializes config field
     */
    private void loadConfigs() {
        if(configManager == null) return;
        config = configManager.getFileConfig("config.yml");
        computers = configManager.getFileConfig("computers.yml");
        players = configManager.getFileConfig("players.yml");
        delay = config.getInt("delay-ticks");
    }

    /**
     * Loads all computers from config
     */
    private void loadComputers() {
        for(BukkitTask task : tasks.values())
            task.cancel();
        tasks.clear();
        unhandledInputs = new ArrayList<>();
        monitors = new HashMap<>();
        targets = new HashMap<>();
        locked = new HashMap<>();
        slots = new HashMap<>();
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
        if(!config.isSet("selection")) config.set("selection", true);
        if(!config.isSet("allowCraft")) config.set("allowCraft", false);
        if(!config.isSet("craft.row1")) config.set("craft.row1", "   ");
        if(!config.isSet("craft.row2")) config.set("craft.row2", "   ");
        if(!config.isSet("craft.row3")) config.set("craft.row3", "   ");
        if(!config.isSet("craft.ingredients")) config.set("craft.ingredients", new ArrayList<String>());
        configManager.saveAllConfigs(true);
        tasks = new HashMap<>();
        packets = new HashMap<>();
        loadComputers();

        if(config.getBoolean("allowCraft")) {
            do {
                String row1 = config.getString("craft.row1");
                String row2 = config.getString("craft.row2");
                String row3 = config.getString("craft.row3");
                if(row1.trim().equals("") && row2.trim().equals("") && row3.trim().equals("")) {
                    logger.log(Level.WARNING, "Crafting recipe is empty!");
                    break;
                }
                List<String> raw = config.getStringList("craft.ingredients");

                record Ingredient(char key, Material mat) {}

                List<Ingredient> ingredients = new ArrayList<>();

                boolean isOK = true;

                for(String line : raw) {
                    String[] parts = line.split(" ");
                    if(parts.length != 2) {
                        logger.log(Level.WARNING, "Invalid ingredient data");
                        logger.log(Level.WARNING, "Should be: '<key char> <material>'");
                        isOK = false;
                        break;
                    }

                    if(parts[0].length() != 1) {
                        logger.log(Level.WARNING, "`"+parts[0]+"' is not a character!");
                        isOK = false;
                        break;
                    }

                    Material material = Material.matchMaterial(parts[1].replace('-', '_'));

                    if(material == null) {
                        logger.log(Level.WARNING, "Could not find material `"+parts[1]+"'!");
                        isOK = false;
                        break;
                    }

                    ingredients.add(new Ingredient(parts[0].charAt(0), material));
                }

                if(!isOK) break;

                {
                    String check = row1+row2+row3;
                    for(char key : check.toCharArray()) {
                        boolean found = false;
                        for(Ingredient i : ingredients) {
                            if(i.key == key) {
                                found = true;
                                break;
                            }
                        }
                        if(!found) {
                            logger.log(Level.WARNING, "Ingredient `"+key+"' not found!");
                            isOK = false;
                            break;
                        }
                    }
                    if(!isOK) break;
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
                meta.setDisplayName("Computer");
                List<String> lore = new ArrayList<>();
                lore.add("Now you can create your own computer :D");
                lore.add("1. /tcmp wand");
                lore.add("2. Select area");
                lore.add("3. /tcmp create <name>");
                meta.setLore(lore);
                result.setItemMeta(meta);

                ShapedRecipe recipe = new ShapedRecipe(this.recipe = new NamespacedKey(this, "computer_recipe"), result);
                recipe.shape(row1, row2, row3);

                for(Ingredient i : ingredients) recipe.setIngredient(i.key, i.mat);

                getServer().addRecipe(recipe);
                logger.log(Level.INFO, "Crafting recipe successfully created!");
            } while(false);
        }

        getServer().getPluginManager().registerEvents(this, this);
        firstPoses = new HashMap<>();
        secondPoses = new HashMap<>();
        areas = new HashMap<>();
        logger.info("Total Computers enabled. (Made by JNNGL)");

        logger.info("Starting sound server...");
        try {
            SoundWebServer.run();
            SoundWebSocketServer.runServer();
        } catch (UnknownHostException e) {
            logger.warning("Failed to start sound server :(");
            logger.warning("[SoundServer] -> "+e.getMessage());
        }
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
                if(!sender.hasPermission("totalcomputers.command.totalcomputers")) { // Check permissions
                    sender.sendMessage(replyPrefix + ChatColor.RED + "You do not have enough permissions.");
                    return true;
                }
                if(args.length == 0 || args[0].equalsIgnoreCase("help")) { // Help subcommand
                    sender.sendMessage(replyPrefix + "Help [1/1]");
                    sender.sendMessage(ChatColor.GOLD + "Aliases: " + ChatColor.WHITE  + " /tcomputers, /tcmp");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers help" + ChatColor.WHITE + " - show help menu.");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers sound" + ChatColor.WHITE + " - creates a link to access audio.");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers create <name>" + ChatColor.WHITE + " - creates new computer at area of wall selected with wand. (See also /totalcomputers selection)");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers remove <name>" + ChatColor.WHITE + " - removes computer.");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers list" + ChatColor.WHITE + " - prints list of created computers.");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers data <name>" + ChatColor.WHITE + " - prints information about computer.");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers selection <enable|disable|toggle|state>" + ChatColor.WHITE + " - enables/disables/toggles/prints possibility of wall area selection with wand.");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers wand" + ChatColor.WHITE + " - gives wand");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers paste <text>" + ChatColor.WHITE + " - pastes text. (Keyboard alternative)");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers erase <all|numChars>" + ChatColor.WHITE + " - erases text. (Keyboard alternative)");
                    sender.sendMessage(ChatColor.GOLD + "/totalcomputers reload" + ChatColor.WHITE + " - reloads all configuration files.");
                }
                else if(args[0].equalsIgnoreCase("sound")) { // Sound subcommand
                    String link;
                    if(links.containsKey((Player)sender)) {
                        link = links.get((Player)sender);
                    } else {
                        link = "http://"+Bukkit.getServer().getIp()+":7254/index.html?name="+sender.getName()+"&sessionId="+(free_session++);
                        links.put((Player)sender, link);
                    }
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + "Open this website in your browser: "+ChatColor.WHITE+link);
                }
                else if(args[0].equalsIgnoreCase("reload")) { // Reload subcommand
                    if(!sender.hasPermission("totalcomputers.plugin.manage")) {
                        sender.sendMessage(replyPrefix + ChatColor.RED +
                                "You do not have enough permissions!");
                        return true;
                    }
                    boolean success = configManager.reloadAllConfigs();
                    loadConfigs();
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + "Configuration files has been" +
                            " successfully reloaded!");
                    if(success) {
                        loadComputers();
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + "All computers has been " +
                                "successfully reloaded.");
                    }
                    else sender.sendMessage(replyPrefix + ChatColor.RED   + "Something went wrong!");
                }
                else if(args[0].equalsIgnoreCase("paste")) { // Paste subcommand
                    if(!sender.hasPermission("totalcomputers.use")) {
                        sender.sendMessage(replyPrefix + ChatColor.GRAY + "You don't have enough permissions!");
                        return true;
                    }

                    if(args.length == 1) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + "Nothing to paste.");
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
                        sender.sendMessage(replyPrefix + ChatColor.GRAY + "You don't have enough permissions!");
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
                            sender.sendMessage(replyPrefix + ChatColor.RED + "`"+args[1]+"' is not a number.");
                        }
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("selection")) { // Selection subcommand
                    if(args.length >= 2) {
                        if(!sender.hasPermission("totalcomputers.plugin.manage")) {
                            sender.sendMessage(replyPrefix + ChatColor.RED +
                                    "You do not have enough permissions!");
                        }
                        if(args[1].equalsIgnoreCase("enable")) {
                            config.set("selection", true);
                            sender.sendMessage(replyPrefix + "Selection using wand successfully enabled.");
                            configManager.saveConfig("config.yml");
                        }
                        else if(args[1].equalsIgnoreCase("disable")) {
                            config.set("selection", false);
                            sender.sendMessage(replyPrefix + "Selection using wand successfully disabled.");
                            configManager.saveConfig("config.yml");
                        }
                        else if(args[1].equalsIgnoreCase("toggle")) {
                            boolean currentState;
                            config.set("selection", (currentState = !isSelectionEnabled()));
                            sender.sendMessage(replyPrefix + "Selection using wand successfully " + (currentState? "enabled" : "disabled") + '.');
                            configManager.saveConfig("config.yml");
                        }
                        else if(args[1].equalsIgnoreCase("state")) {
                            sender.sendMessage(replyPrefix + "Selection using wand is currently " + (config.getBoolean("selection")? "enabled" : "disabled") + '.');
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
                                        "You do not have enough permissions!");
                                return true;
                            }
                            if(getUnregComputers(player) <= 0) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        "You have placed all your !");
                                return true;
                            }
                        }
                        if(!areas.containsKey(player)) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "You have not selected area or it is invalid. Select it with wand. Make sure area selection is enabled.");
                            return true;
                        }
                        SelectionArea area = areas.get(player);
                        if((float)area.width / (float)area.height < 4f/3f) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Aspect ratio should be at least 4:3");
                            return true;
                        }
                        if(area.width > 16) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Maximum width in blocks is 16.");
                            return true;
                        }
                        if(area.height > 9) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Maximum height in blocks is 9.");
                            return true;
                        }
                        if(area.width < 4) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Minimum width in blocks is 4.");
                            return true;
                        }
                        if(area.height < 3) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Minimum height in blocks is 3.");
                            return true;
                        }
                        if(area.axis == SelectionArea.Axis.Y) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Computer cannot be placed on horizontal surface.");
                            return true;
                        }
                        if(registeredComputers.contains(args[1])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "Computer with this name already exists.");
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
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + "Computer with name '"+args[1]+"' successfully created!");
                        decreaseUnregComputers(player);
                        addOwner(player, args[1]);
                    } else if(args.length > 2) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + "Computer name cannot contain spaces!");
                    } else invalidUsage(sender);
                }
                else if(args[0].equalsIgnoreCase("remove")) { // Remove subcommand
                    if(args.length == 2) {
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            if(!sender.hasPermission("totalcomputers.manage.crafted")
                                    || !playerOwns((Player) sender, args[1])) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        "You do not have enough permissions!");
                                return true;
                            }
                        }
                        if(!registeredComputers.contains(args[1])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "There is no such computer with name '"+args[1]+"'.");
                            return true;
                        }
                        removeComputer(args[1]);
                        computers.set("computers."+args[1], null);
                        computers.set("computers.names", registeredComputers);
                        configManager.saveConfig("computers.yml", true);
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + "Computer with name '"+args[1]+"' successfully removed!");
                        increaseUnregComputers((Player) sender);
                        removeOwner((Player) sender, args[1]);
                    } else if(args.length > 2) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + "Computer name cannot contain spaces!");
                    } else invalidUsage(sender);
                }
                else if(args[0].equalsIgnoreCase("list")) { // List subcommand
                    List<String> comps;
                    if(!sender.hasPermission("totalcomputers.manage.all")) {
                        if(!sender.hasPermission("totalcomputers.manage.crafted")) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "You do not have enough" +
                                    " permissions!");
                            return true;
                        }

                        comps = new ArrayList<>();
                        for(String comp : registeredComputers) {
                            if(playerOwns((Player) sender, comp)) comps.add(comp);
                        }
                    } else comps = registeredComputers;
                    if(comps.isEmpty()) {
                        sender.sendMessage(replyPrefix + ChatColor.GREEN + "None.");
                        return true;
                    }
                    StringBuilder list = new StringBuilder();
                    for(String name : comps) list.append(name).append(", ");
                    list.delete(list.length()-2, list.length()-1);
                    sender.sendMessage(replyPrefix + ChatColor.GREEN + "Available computers: " + ChatColor.RESET + list);
                }
                else if(args[0].equalsIgnoreCase("wand")) { // Wand subcommand
                    ItemStack wand = new ItemStack(Material.STICK, 1);
                    ItemMeta meta = wand.getItemMeta();
                    meta.setDisplayName("TotalComputers");
                    List<String> lore = new ArrayList<>();
                    lore.add("Select area");
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
                else if(args[0].equalsIgnoreCase("data")) { // Data subcommand
                    if(args.length == 2) {
                        if(!sender.hasPermission("totalcomputers.manage.all")) {
                            if(!sender.hasPermission("totalcomputers.manage.crafted")
                                    || !playerOwns((Player) sender, args[1])) {
                                sender.sendMessage(replyPrefix + ChatColor.RED +
                                        "You do not have enough permissions!");
                                return true;
                            }
                        }
                        if(!registeredComputers.contains(args[1])) {
                            sender.sendMessage(replyPrefix + ChatColor.RED + "There is no such computer with name '"+args[1]+"'.");
                            return true;
                        }
                        SelectionArea area = computersPhysicalData.get(args[1]);
                        sender.sendMessage(replyPrefix + "Information:");
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Name: " + ChatColor.RESET + args[1]);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "First Corner Position: " + ChatColor.RESET);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "   X: " + ChatColor.RESET + area.firstPos.getBlockX());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "   Y: " + ChatColor.RESET + area.firstPos.getBlockY());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "   Z: " + ChatColor.RESET + area.firstPos.getBlockZ());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Second Corner Position: " + ChatColor.RESET);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "   X: " + ChatColor.RESET + area.secondPos.getBlockX());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "   Y: " + ChatColor.RESET + area.secondPos.getBlockY());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "   Z: " + ChatColor.RESET + area.secondPos.getBlockZ());
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Axis: " + ChatColor.RESET + area.axis);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Facing Direction: " + ChatColor.RESET + area.direction);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Width: " + ChatColor.RESET + area.width);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Height: " + ChatColor.RESET + area.height);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "Area: " + ChatColor.RESET + area.area);
                        sender.sendMessage(replyPrefix + ChatColor.GOLD + "World: " + ChatColor.RESET + area.firstPos.getWorld().getName());
                    } else if(args.length > 2) {
                        sender.sendMessage(replyPrefix + ChatColor.RED + "Computer name cannot contain spaces!");
                    } else invalidUsage(sender);
                } else {
                    invalidUsage(sender);
                }
            }
        } else sender.sendMessage(replyPrefix + ChatColor.RED + "Only players can execute this command.");
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
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> variants = new ArrayList<>();
        if(!(sender instanceof Player player)) return variants;
        if(!sender.hasPermission("totalcomputers.command.totalcomputers")) return variants;
        String[] all = {};
        if(command.getName().equalsIgnoreCase("totalcomputers")) {
            if(args.length == 1) {
                if(locked.containsKey(player)) all = new String[]{"help", "sound", "create", "remove", "selection", "list",
                        "reload", "data", "wand", "paste", "erase", "release"};
                else all = new String[]{"help", "sound", "create", "remove", "selection", "list",
                        "reload", "data", "wand", "paste", "erase"};
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
        if(!(e.getWhoClicked() instanceof Player player)) return;
        ItemStack result = e.getRecipe().getResult();
        ItemMeta meta = result.getItemMeta();
        if(meta == null) return;
        String name = meta.getDisplayName();
        if(!name.equals("Computer")) return;
        List<String> lore = meta.getLore();
        if(lore == null || lore.size() != 4) return;
        if(!lore.get(0).equals("Now you can create your own computer :D")) return;
        if(!lore.get(1).equals("1. /tcmp wand")) return;
        if(!lore.get(2).equals("2. Select area")) return;
        if(!lore.get(3).equals("3. /tcmp create <name>")) return;
        increaseUnregComputers(player);
        for(String msg : lore) player.sendMessage(replyPrefix + ChatColor.GREEN + msg);
    }

    /**
     * Processes selection with wand. Permission:  <code>totalcomputers.selection</code>
     * @param event Event (PlayerInteractEvent)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void selectionEvent(PlayerInteractEvent event) {
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
        if(!lore.get(0).equals("Select area")) return;
        Action action = event.getAction();
        Location loc, oldLoc = null;
        boolean displayArea = false;
        if(action.equals(Action.LEFT_CLICK_BLOCK)) {
            if(firstPoses.containsKey(player)) oldLoc = firstPoses.get(player);
            firstPoses.remove(player);
            firstPoses.put(player, loc = event.getClickedBlock().getLocation());
            event.setCancelled(true);
            if(oldLoc == null || !oldLoc.equals(loc)) {
                player.sendMessage(replyPrefix + ChatColor.LIGHT_PURPLE + "First position is set. (X: "+loc.getBlockX()+", Y: "+loc.getBlockY()+", Z: "+loc.getBlockZ()+")");
            }
            if(secondPoses.containsKey(player)) computeArea(player);
        } else if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if(secondPoses.containsKey(player)) oldLoc = secondPoses.get(player);
            secondPoses.remove(player);
            secondPoses.put(player, loc = event.getClickedBlock().getLocation());
            event.setCancelled(true);
            if(oldLoc == null || !oldLoc.equals(loc)) {
                player.sendMessage(replyPrefix + ChatColor.LIGHT_PURPLE + "Second position is set. (X: "+loc.getBlockX()+", Y: "+loc.getBlockY()+", Z: "+loc.getBlockZ()+")");
            }
            if(firstPoses.containsKey(player)) computeArea(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoin(PlayerJoinEvent event) {
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
        Player player = event.getPlayer();
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
            logger.warning("Failed to shutdown server");
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
            sender.sendMessage(replyPrefix + ChatColor.RED + "You are too far from the computer");
            return null;
        }

        TotalOS os = systems.get(nearest);
        if(os.keyboard == null || !os.keyboard.isControlTaken()) {
            sender.sendMessage(replyPrefix + ChatColor.RED + "Open keyboard first");
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
        unhandledInputs.removeAll(toRemove);
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
                            entity.sendMessage(replyPrefix + ChatColor.GRAY + "You don't have enough permissions!");
                        } else {
                            entity.sendMessage(replyPrefix + ChatColor.GRAY + "You are trying to use the computer with an item in main hand!");
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
        sender.sendMessage(replyPrefix + ChatColor.RED + "Invalid usage! Use '/totalcomputers help' for information about commands.");
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
            player.sendMessage(replyPrefix + ChatColor.RED + "Positions are located in different worlds!");
            return;
        }
        if (first.getBlockX() == second.getBlockX()) axis = SelectionArea.Axis.X;
        else if (first.getBlockY() == second.getBlockY()) axis = SelectionArea.Axis.Y;
        else if (first.getBlockZ() == second.getBlockZ()) axis = SelectionArea.Axis.Z;
        else {
            player.sendMessage(replyPrefix + ChatColor.RED + "Selected area should be flat!");
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
            player.sendMessage(replyPrefix + ChatColor.RED + "Selected area cannot be 1*1.");
            return;
        }
        areas.put(player, new SelectionArea(first, second, axis, direction, width, height, area));
        player.sendMessage(replyPrefix + ChatColor.LIGHT_PURPLE + "Selected Area: [Direction: "+direction+", Area: "+area+" ("+width+"*"+height+")]");
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
                setMapId.invoke(meta, mapId);
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
            if(f == null)
                f = area.firstPos.getWorld().spawn(loc, ItemFrame.class);
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

            Object[] frame1 = new Object[area.area], frame2 = new Object[area.area];
            BufferedImage empty = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
            for(int j = 0; j < area.area; j++) {
                try {
                    frame1[j] = sender.createPacket(maps[j], empty);
                    frame2[j] = sender.createPacket(maps[j], empty);
                } catch (ReflectiveOperationException e) {
                    logger.warning("Failed to create packet");
                }
            }
            packets.put(os, new DoubleBuffer(frame1, frame2));

            int[] uncaught = { 0 };
            tasks.put(os.name, Bukkit.getScheduler().runTaskTimer(this, () -> {
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    try {
                        List<TotalComputers.InputInfo> handledInputs = new ArrayList<>();
                        for (int x = 0; x < area.width; x++) {
                            for (int y = 0; y < area.height; y++) {
                                int id = y * area.width + x;
                                int absX = x * 128;
                                int absY = y * 128;
                                for (TotalComputers.InputInfo inputInfo : unhandledInputs.toArray(new InputInfo[0])) {
                                    if (inputInfo.index().name().equals(name) && inputInfo.index().index() == id) {
                                        executors.put(os, inputInfo.player);
                                        os.processTouch(absX + inputInfo.x(), absY + inputInfo.y(), inputInfo.interactType(), inputInfo.player().hasPermission("totalcomputers.admin"));
                                        executors.remove(os);
                                        handledInputs.add(inputInfo);
                                    }
                                }
                            }
                        }
                        unhandledInputs.removeAll(handledInputs);

                        BufferedImage screen = os.renderFrame();
                        if(screen == null) throw new Exception("Failed to render screen");

                        Object[] framePacket = packets.get(os).get();

                        for (int x = 0; x < area.width; x++) {
                            for (int y = 0; y < area.height; y++) {
                                int id = y * area.width + x;
                                int absX = x * 128;
                                int absY = y * 128;
                                try {
                                    sender.modifyPacket(framePacket[id], screen.getSubimage(absX, absY, 128, 128));
                                } catch (ReflectiveOperationException e) {
                                    logger.warning("Failed to create packet");
                                    logger.warning(" -> " + e.getMessage());
                                }
                            }
                        }


                        for (Player player : Bukkit.getOnlinePlayers()) {
                            for (int id = 0; id < area.area; id++) {
                                try {
                                    sender.sendPacket(player, framePacket[id]);
                                } catch (ReflectiveOperationException e) {
                                    logger.warning("Failed to create packet");
                                    logger.warning(" -> " + e.getMessage());
                                }
                            }
                        }
                        uncaught[0] = 0;
                    } catch(Throwable e) {
                        if(e instanceof OutOfMemoryError) {
                            os.invokeBSoD("Not enough RAM", e, 0x03);
                        }
                        else if(uncaught[0]/(delay == 0? 60 : (20/delay)) >= 3) {
                            os.invokeBSoD("Fatal ERROR", new Throwable(e), 0x01);
                        }
                        else {
                            uncaught[0]++;
                            System.err.println("Fatal ERROR #"+uncaught[0]+" -> Uncaught exception: " + e.getMessage());
                        }
                    }
                });
            }, 0, delay));

        }

        systems.put(name, os);
        monitors.remove(name);
        monitors.put(name, monitorPieces);
    }

}
