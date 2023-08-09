package net.mod.prymod.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.mod.prymod.ModBlock.Cable;
import net.mod.prymod.ModBlock.PRYBlock;

import java.util.LinkedList;
import java.util.Stack;

public class DFSforBlock {

    //For launcher only
    public static BlockPos isConnectedToBlockEntity(BlockEntity startEntity, Class<? extends BlockEntity> c1){
        Level level = startEntity.getLevel();
        BlockPos startPos = startEntity.getBlockPos();

        if(level==null) return null;
        //Depth first search using a stack
        //Search order positive x, positive z, negative x, negative z
        //Discovered positions are stored in a LinkedList

        //Initialise the discovered linkedlist and stack
        LinkedList<BlockPos> discovered = new LinkedList<>();
        Stack<BlockPos> stack = new Stack<>();

        //Push in the start position into stack and set it as discovered
        stack.push(startEntity.getBlockPos());

        //Depth first search loop
        while(!stack.isEmpty()){
            BlockPos currentPos = stack.peek();
            BlockPos nextPos = null;

            //Exit condition
            if(c1.isInstance(level.getBlockEntity(currentPos))){
                return currentPos;
            }

            //Cable must connect to the designated port of the block, so check if the current position is the start
            //position
            if(currentPos.equals(startPos)){
                switch(startEntity.getBlockState().getValue(PRYBlock.FACING)){
                    case NORTH -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
                    }
                    case EAST -> {
                        nextPos = new BlockPos(currentPos.getX()+1, currentPos.getY(), currentPos.getZ());
                    }
                    case SOUTH -> {
                        nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
                    }
                    case WEST -> {
                        nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
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
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || c1.isInstance(level.getBlockEntity(nextPos)))){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try positive z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || c1.isInstance(level.getBlockEntity(nextPos)))){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative x next
            nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || c1.isInstance(level.getBlockEntity(nextPos)))){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || c1.isInstance(level.getBlockEntity(nextPos)))){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //No positions added to stack, pop the top one
            stack.pop();
        }
        return null;
    }

    private static boolean contains(LinkedList<BlockPos> ll, BlockPos pos){
        for(BlockPos position : ll){
            if(pos.equals(position)){
                return true;
            }
        }
        return false;
    }

}
