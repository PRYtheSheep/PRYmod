package net.mod.prymod.ModBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mod.prymod.Renderer.RGB;
import net.mod.prymod.Renderer.TestRenderer;
import net.mod.prymod.utils.DFSutils;

import java.util.*;

public class PRYRadarEntity extends BlockEntity {
    public PRYRadarEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYRADARENTITY.get(), pos, state);
    }

    public LivingEntity radarTarget = null;
    public static LinkedHashMap<LivingEntity, RGB> livingEntityRGBHashMap = new LinkedHashMap<>();
    private final DFSutils utils = new DFSutils() {
        @Override
        public BlockPos getStartPos(BlockEntity blockEntity) {
            BlockPos currentPos = blockEntity.getBlockPos();
            switch(blockEntity.getBlockState().getValue(PRYBlock.FACING)){
                case NORTH -> {
                    return new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                }
                case EAST -> {
                    return new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                }
                case SOUTH -> {
                    return new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                }
                case WEST -> {
                    return new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                }
            }
            return null;
        }
    };

    public void tick(){
        if(this.level.isClientSide) return;

        if(utils.isConnectedToBlockEntity(this, PRYGeneratorEntity.class) == null){
            livingEntityRGBHashMap.remove(radarTarget);
            radarTarget = null;
            return;
        }

        if(utils.isConnectedToBlockEntity(this, PRYBlockEntity.class) == null && radarTarget != null){
            livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
        }

        if(radarTarget != null && radarTarget.getHealth() == 0) {
            livingEntityRGBHashMap.remove(radarTarget);
            radarTarget = null;
        }

        if(Minecraft.getInstance().player != null && radarTarget != null){
            if(radarTarget.distanceTo(Minecraft.getInstance().player) > 128) {
                livingEntityRGBHashMap.remove(radarTarget);
                radarTarget = null;
            }
        }

        if(radarTarget != null && !livingEntityRGBHashMap.containsKey(radarTarget)){
            //livingEntityLinkedList.add(radarTarget);
            livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
        }

        AABB startEndBox = null;
        switch(this.getBlockState().getValue(PRYRadar.FACING)){
            case NORTH -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()-90,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()+1,
                        this.getBlockPos().getX()+90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()+90);
            }
            case EAST -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()-1,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()-90,
                        this.getBlockPos().getX()-90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()+90);
            }
            case SOUTH -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()-90,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()-1,
                        this.getBlockPos().getX()+90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()-90);
            }
            case WEST -> {
                startEndBox = new AABB(
                        this.getBlockPos().getX()+1,
                        this.getBlockPos().getY(),
                        this.getBlockPos().getZ()-90,
                        this.getBlockPos().getX()+90,
                        this.getBlockPos().getY()+90,
                        this.getBlockPos().getZ()+90);
            }
        }
        if(radarTarget == null){
            List<Entity> list1 = this.level.getEntities(null, startEndBox);
            for(Entity entity : list1){
                if(entity instanceof Bat && ((Bat) entity).getHealth() != 0){
                    radarTarget = (LivingEntity) entity;
                    //livingEntityLinkedList.add(radarTarget);
                    livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
                    break;
                }
            }
        }
    }
}
