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

import java.util.*;

public class PRYRadarEntity extends BlockEntity {
    public PRYRadarEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYRADARENTITY.get(), pos, state);
    }

    public LivingEntity radarTarget = null;
    //public static LinkedList<LivingEntity> livingEntityLinkedList = new LinkedList<>();
    public static LinkedHashMap<LivingEntity, RGB> livingEntityRGBHashMap = new LinkedHashMap<>();
    public void tick(){
        if(this.level.isClientSide) return;
        if(!isConnectedToGenerator(this.getBlockPos())){
            //livingEntityLinkedList.remove(radarTarget);
            livingEntityRGBHashMap.remove(radarTarget);
            radarTarget = null;
            return;
        }

        if(!isConnectedToLauncher(this.getBlockPos()) && radarTarget != null){
            livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
        }

        if(radarTarget != null && radarTarget.getHealth() == 0) {
            //livingEntityLinkedList.remove(radarTarget);
            livingEntityRGBHashMap.remove(radarTarget);
            radarTarget = null;
        }

        if(Minecraft.getInstance().player != null && radarTarget != null){
            if(radarTarget.distanceTo(Minecraft.getInstance().player) > 128) {
                //livingEntityLinkedList.remove(radarTarget);
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
                if(entity instanceof Player && ((Player) entity).getHealth() != 0){
                    radarTarget = (LivingEntity) entity;
                    //livingEntityLinkedList.add(radarTarget);
                    livingEntityRGBHashMap.put(radarTarget, new RGB(1, 1, 0));
                    break;
                }
            }
        }
    }

    private boolean isConnectedToGenerator(BlockPos startPos){
        if(level==null) return false;
        //Depth first search using a stack
        //Search order positive x, positive z, negative x, negative z
        //Discovered positions are stored in a LinkedList

        //Initialise the discovered linkedlist and stack
        LinkedList<BlockPos> discovered = new LinkedList<>();
        Stack<BlockPos> stack = new Stack<>();

        //Push in the start position into stack and set it as discovered
        stack.push(startPos);

        //Depth first search loop
        while(!stack.isEmpty()){
            BlockPos currentPos = stack.peek();
            BlockPos nextPos = null;

            //Exit condition
            if(level.getBlockEntity(currentPos) instanceof PRYGeneratorEntity){
                return true;
            }

            //Cable must connect to the designated port of the block, so check if the current position is the start
            //position
            if(currentPos.equals(startPos)){
                switch(this.getBlockState().getValue(PRYRadar.FACING)){
                    case NORTH -> {
                        nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                    }
                    case EAST -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                    }
                    case SOUTH -> {
                        nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                    }
                    case WEST -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                    }
                }
                Block block = level.getBlockState(nextPos).getBlock();
                if(block instanceof Cable && !contains(discovered, nextPos)){
                    stack.push(nextPos);
                    discovered.add(nextPos);
                    discovered.add(startPos);
                }
                else stack.pop();
                continue;
            }

            //Try positive x first
            nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try positive z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative x next
            nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYGenerator)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //No positions added to stack, pop the top one
            stack.pop();
        }
        return false;
    }

    private boolean isConnectedToLauncher(BlockPos startPos){
        if(level==null) return false;
        //Depth first search using a stack
        //Search order positive x, positive z, negative x, negative z
        //Discovered positions are stored in a LinkedList

        //Initialise the discovered linkedlist and stack
        LinkedList<BlockPos> discovered = new LinkedList<>();
        Stack<BlockPos> stack = new Stack<>();

        //Push in the start position into stack and set it as discovered
        stack.push(startPos);

        //Depth first search loop
        while(!stack.isEmpty()){
            BlockPos currentPos = stack.peek();
            BlockPos nextPos = null;

            //Exit condition
            if(level.getBlockEntity(currentPos) instanceof PRYBlockEntity){
                return true;
            }

            //Cable must connect to the designated port of the block, so check if the current position is the start
            //position
            if(currentPos.equals(startPos)){
                switch(this.getBlockState().getValue(PRYBlock.FACING)){
                    case NORTH -> {
                        nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                    }
                    case EAST -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                    }
                    case SOUTH -> {
                        nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
                    }
                    case WEST -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                    }
                }
                Block block = level.getBlockState(nextPos).getBlock();
                if(block instanceof Cable && !contains(discovered, nextPos)){
                    stack.push(nextPos);
                    discovered.add(nextPos);
                    discovered.add(startPos);
                }
                else stack.pop();
                continue;
            }

            //Try positive x first
            nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYBlock)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try positive z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYBlock)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative x next
            nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYBlock)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYBlock)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //No positions added to stack, pop the top one
            stack.pop();
        }
        return false;
    }

    private boolean contains(LinkedList<BlockPos> ll, BlockPos pos){
        for(BlockPos position : ll){
            if(pos.equals(position)){
                return true;
            }
        }
        return false;
    }
}
