package org.kettingpowered.ketting.entity.block;

public class CraftCustomHopper<T extends net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity> extends org.bukkit.craftbukkit.v1_20_R1.block.CraftLootable<T> implements org.bukkit.block.Hopper {

    public CraftCustomHopper(org.bukkit.World world, T tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public org.bukkit.inventory.Inventory getSnapshotInventory() {
        return new org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory(this.getSnapshot());
    }

    @Override
    public org.bukkit.inventory.Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory(this.getTileEntity());
    }
}
