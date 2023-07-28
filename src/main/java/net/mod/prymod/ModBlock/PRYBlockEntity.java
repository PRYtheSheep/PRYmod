package net.mod.prymod.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.mod.prymod.Renderer.RGB;
import net.mod.prymod.Renderer.TestRenderer;
import net.mod.prymod.itemMod.custom.ProximityArrowEntity;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.PRYBlockEntityS2C;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Predicate;

public class PRYBlockEntity extends BlockEntity {

    int progress = 0;
    public float facing = -1;
    public PRYBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityInit.PRYBLOCKENTITY.get(), pos, state);
    }

    public UUID uuid;
    public LivingEntity target = null;
    public PRYRadarEntity radarEntity = null;
    public boolean pointingAtTarget = false;
    public boolean inflight = false;
    public int progressCount = -1;
    public void tick() {
        if(this.level.isClientSide) return;
        if(!isConnectedToGenerator(this.getBlockPos())) return;
        if(target != null && target.getHealth() == 0){
            target = null;
            inflight = false;
            return;
        }
        if(isConnectedToRadar(this.getBlockPos()) != null){
            BlockPos radarPos = isConnectedToRadar(this.getBlockPos());
            radarEntity = (PRYRadarEntity) this.level.getBlockEntity(radarPos);
        }
        else return;
        Predicate<Entity> predicate = (i) -> (i instanceof Player);
        if(radarEntity.radarTarget != target) progressCount = -1;
        //Spawn projectile if target is found
        if(radarEntity.radarTarget != null){
            Vec3 test = new Vec3((radarEntity.radarTarget.getX() - this.getBlockPos().getX()),
                    (radarEntity.radarTarget.getY() - this.getBlockPos().getY()),
                    (radarEntity.radarTarget.getZ() - this.getBlockPos().getZ()));
            if(test.length() > 90) return;

            target = radarEntity.radarTarget;
            ModMessages.INSTANCE.send(PacketDistributor.ALL.noArg(), new PRYBlockEntityS2C(target.getUUID(), this.getBlockPos()));

            if(progress %2 == 0 && progressCount == -1) progressCount = progress;
            if(progress != -1 && progress - progressCount <= 100){
                RGB currentRGB = PRYRadarEntity.livingEntityRGBHashMap.get(target);
                if(progress %2 == 0 && currentRGB.equals(new RGB(0, 1, 1))) PRYRadarEntity.livingEntityRGBHashMap.put(target, new RGB(1, 1, 0));
                else if(progress %2 == 0 && currentRGB.equals(new RGB(1, 1, 0))) PRYRadarEntity.livingEntityRGBHashMap.put(target, new RGB(0, 1, 1));
                this.inflight = false;
                progress++;
                return;
            }
            else if(progress % 50 == 0 && pointingAtTarget){

                ProximityArrowEntity proxyArrow = new ProximityArrowEntity(ModBlockEntityInit.PROXIMITY_ARROW_ENTITY.get(), this.level);
                proxyArrow.setEntityOwner(this);
                proxyArrow.setPos(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.2, this.getBlockPos().getZ() + 0.5);
                Vec3 resultantVector = new Vec3((target.getX() - this.getBlockPos().getX()),
                        1,
                        (target.getZ() - this.getBlockPos().getZ()));
                if(!inflight){
                    proxyArrow.setDeltaMovement(resultantVector);
                    if(proxyArrow.getDeltaMovement().length() > 1){
                        proxyArrow.setDeltaMovement(proxyArrow.getDeltaMovement().multiply(
                                1/proxyArrow.getDeltaMovement().length(),
                                1/proxyArrow.getDeltaMovement().length(),
                                1/proxyArrow.getDeltaMovement().length()
                        ));
                    }
                    proxyArrow.target = target;
                    this.level.addFreshEntity(proxyArrow);
                    inflight = true;
                    Player player = this.level.getNearestPlayer(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 10000, predicate);
                    if(player != null){
                        player.displayClientMessage(Component.literal("§aMissile launched"), true);
                    }
                }
            }
        }
        progress++;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        //nbt.putInt("progress", this.progress);
        nbt.putFloat("facing", this.facing);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        //this.progress = nbt.getInt("progress");
        this.facing = nbt.getFloat("facing");
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

    private BlockPos isConnectedToRadar(BlockPos startPos){
        if(level==null) return null;
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
            if(level.getBlockEntity(currentPos) instanceof PRYRadarEntity){
                return currentPos;
            }

            //Cable must connect to the designated port of the block, so check if the current position is the start
            //position
            if(currentPos.equals(startPos)){
                switch(this.getBlockState().getValue(PRYRadar.FACING)){
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
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try positive z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()+1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative x next
            nextPos = new BlockPos(currentPos.getX()-1, currentPos.getY(), currentPos.getZ());
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //Try negative z next
            nextPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()-1);
            if(!contains(discovered, nextPos) && (level.getBlockState(nextPos).getBlock() instanceof Cable || level.getBlockState(nextPos).getBlock() instanceof PRYRadar)){
                stack.push(nextPos);
                discovered.add(nextPos);
                continue;
            }

            //No positions added to stack, pop the top one
            stack.pop();
        }
        return null;
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
