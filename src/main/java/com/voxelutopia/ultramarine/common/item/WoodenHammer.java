package com.voxelutopia.ultramarine.common.item;

import com.voxelutopia.ultramarine.common.block.RailingBlock;
import com.voxelutopia.ultramarine.common.block.state.ModBlockStateProperties;
import com.voxelutopia.ultramarine.init.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WoodenHammer extends Item {

    public WoodenHammer() {
        super(new Item.Properties().durability(100));
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        ItemStack item = pContext.getItemInHand();
        var player = Optional.ofNullable(pContext.getPlayer());
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        boolean success = false;
        if (blockstate.hasProperty(ModBlockStateProperties.SHIFTED)) {
            level.setBlock(blockpos, blockstate.setValue(ModBlockStateProperties.SHIFTED,
                    !blockstate.getValue(ModBlockStateProperties.SHIFTED)), Block.UPDATE_ALL);
            success = true;
        }
        if (blockstate.hasProperty(ModBlockStateProperties.CHIRAL_BLOCK_TYPE)) {
            level.setBlock(blockpos, blockstate.setValue(ModBlockStateProperties.CHIRAL_BLOCK_TYPE,
                    blockstate.getValue(ModBlockStateProperties.CHIRAL_BLOCK_TYPE).getOpposite()), Block.UPDATE_ALL);
            success = true;
        }
        if (blockstate.hasProperty(ModBlockStateProperties.LOCKED) && player.isPresent() && player.get().isCrouching()) {
            blockstate = blockstate.setValue(ModBlockStateProperties.LOCKED,
                    !blockstate.getValue(ModBlockStateProperties.LOCKED));
            if (blockstate.getBlock() instanceof RailingBlock railingBlock) {
                blockstate = railingBlock.updatePole(blockstate);
            }
            level.setBlock(blockpos, blockstate, Block.UPDATE_ALL);
            success = true;
        }
        if (success) {
            player.ifPresent(player1 -> {
                item.hurtAndBreak(1, player1, p -> p.broadcastBreakEvent(pContext.getHand()));
                player1.awardStat(Stats.ITEM_USED.get(item.getItem()));
                level.playSound(player.get(), blockpos, ModSounds.WOOD_HAMMER, SoundSource.BLOCKS, 1, 0.75f);
            });
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useOn(pContext);
    }


}
