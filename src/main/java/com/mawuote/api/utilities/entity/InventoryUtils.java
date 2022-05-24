package com.mawuote.api.utilities.entity;

import com.mawuote.api.utilities.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryUtils implements IMinecraft {
    public static int getCombatBlock(String input) {
        int obsidianSlot = findBlock(Blocks.OBSIDIAN, 0, 9);
        int chestSlot = findBlock(Blocks.ENDER_CHEST, 0, 9);

        if (obsidianSlot == -1 && chestSlot == -1) {
            return -1;
        } else if (obsidianSlot != -1 && chestSlot == -1) {
            return obsidianSlot;
        } else if (obsidianSlot == -1) {
            return chestSlot;
        } else {
            if (input.equals("Obsidian")) {
                return obsidianSlot;
            } else {
                return chestSlot;
            }
        }
    }

    public static void switchSlot(int slot, boolean silent) {
        if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
        }
    }

    public static int findItem(Item item, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                return i;
            }
        }

        return -1;
    }

    public static int findBlock(Block block, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock) {
                ItemBlock item = (ItemBlock) stack.getItem();
                if (item.getBlock() == block) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static void switchToSlot(Class<? extends Item> clazz) {

        if (mc.player.getHeldItemMainhand().getItem().getClass().isAssignableFrom(clazz)) return;

        int slot = getHotbarItemSlot(clazz);

        if (slot == -1) return;

        mc.player.inventory.currentItem = slot;
    }

    public static void switchToSlot(Item item) {
        if (mc.player.getHeldItemMainhand().getItem() == item) return;

        int slot = getHotbarItemSlot(item.getClass());

        if (slot == -1) return;

        mc.player.inventory.currentItem = slot;
    }

    public static void switchToPacketSlot(Item item) {
        if (mc.player.getHeldItemMainhand().getItem() == item) return;

        int slot = getHotbarItemSlot(item.getClass());

        if (slot == -1) return;

        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    public static void switchToPacketSlot(Class<? extends Item> clazz) {
        if (mc.player.getHeldItemMainhand().getItem().getClass().isAssignableFrom(clazz)) return;

        int slot = getHotbarItemSlot(clazz);

        if (slot == -1) return;

        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    public static int findItemInventorySlot(Item item, boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        if (mc.currentScreen instanceof GuiCrafting) {
            return getOtherSlot(10, 45);
        }
        return getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)mc.player.inventoryContainer.getInventory().get(current));
        }
        return fullInventorySlots;
    }

    private static Map<Integer, ItemStack> getOtherSlot(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)mc.player.openContainer.getInventory().get(current));
        }
        return fullInventorySlots;
    }

    public static void offhandItem(Item item) {
        final int slot = findItemInventorySlot(item, false);

        if (slot != -1) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }
    }

    public static int getHotbarItemSlot(Class<? extends Item> item) {
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem().getClass().isAssignableFrom(item)) {
                slot = i;

                break;
            }
        }

        return slot;
    }

    public static int getHotbarBlockSlot(Block block) {
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();

            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) {
                slot = i;

                break;
            }
        }

        return slot;
    }

    public static int getHotbarItemSlot(Item item) {
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            Item selection = mc.player.inventory.getStackInSlot(i).getItem();

            if (selection.equals(item)) {
                slot = i;

                break;
            }
        }

        return slot;
    }

    private static int getInventoryItemSlot(Item item) {
        for (int i = 0; i < 36; i++) {
            final Item cacheItem = mc.player.inventory.getStackInSlot(i).getItem();

            if (cacheItem == item) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }

        return -1;
    }

    public boolean isHotbar(int slot) {
        if(slot == 0 || slot == 1 || slot == 2 || slot == 3 || slot == 4 || slot == 5 || slot == 6 || slot == 7 || slot == 8) {
            return true;
        } else {
            return false;
        }
    }

    public enum Items {
        Obsidian, Chest
    }

    public enum SwitchModes {
        Normal, Silent, Strict
    }
}