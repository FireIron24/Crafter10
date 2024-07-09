package com.fireIron24.crafter10.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;


@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {

    /**
     * Overwrite the getAnalogOutputSignal method to change its behavior.
     * @author FireIro24
     * @reason to get to getRedstoneSignal with blockEntity reference
     * @param blockState The block state.
     * @param level The level.
     * @param blockPos The block position.
     * @return The new analog output signal.
     */
    @Overwrite
    public int  getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof CrafterBlockEntity) {
            return (getRedstoneSignal((CrafterBlockEntity) blockEntity));
        } else {
            return 0;
        }
    }

    @Unique
    public int getRedstoneSignal(CrafterBlockEntity blockentity) {
        int i = 0;
        short extra = 0;
        boolean fillableSlot = false;
        for(int j = 0; j < blockentity.getContainerSize(); ++j) {
            ItemStack itemStack = blockentity.getItem(j);
            if (blockentity.isSlotDisabled(j)) {
                ++i;
                ++extra;
            } else if (!itemStack.isEmpty()) {
                fillableSlot=true;
                if (itemStack.getMaxStackSize()==1) {
                    ++i;
                    ++extra;
                } else {
                    ++i;
                    if (itemStack.getCount() >1) {
                        ++extra;
                    }
                }
            }
        }

        return (extra == 9 && fillableSlot) ? (i + 1) : i;
    }








}
