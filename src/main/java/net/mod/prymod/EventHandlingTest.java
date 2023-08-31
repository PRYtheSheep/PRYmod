package net.mod.prymod;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Pig;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.renderable.IRenderable;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mod.prymod.ModBlock.PRYBlock;
import net.mod.prymod.ModBlock.PRYBlockEntity;
import net.mod.prymod.ModBlock.PRYRadar;
import net.mod.prymod.ModBlock.PRYRadarEntity;
import net.mod.prymod.Renderer.RGB;
import net.mod.prymod.Renderer.TestRenderer;
import net.mod.prymod.itemMod.custom.ProximityArrowEntity;
import net.mod.prymod.itemMod.custom.TestWand;
import net.mod.prymod.itemMod.networking.ModMessages;
import net.mod.prymod.itemMod.networking.packets.PRYBlockRendererC2SPacket;
import net.mod.prymod.sound.ModSounds;
import org.lwjgl.opengl.GL11;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;


import java.util.LinkedList;

import java.util.Optional;

public class EventHandlingTest {
    @SubscribeEvent
    public void onLoginTest(PlayerEvent.PlayerLoggedInEvent event){
        //Set true to display floating message, set false to display message in chat window
        event.getEntity().displayClientMessage(Component.literal("Test message"), false);
    }
    @SubscribeEvent
    public void attackPigTest(AttackEntityEvent event){
        Entity target = event.getTarget();
        if(target instanceof Pig){
            if(event.getEntity().level().isClientSide) return;
            event.getEntity().displayClientMessage(Component.literal("pig attacked test msg"), false);

        }
    }
    @SubscribeEvent
    public void rightClickTest(PlayerInteractEvent.RightClickItem event){
        //explodes the target mob
        EntityHitResult result = getClosestEntity(getPlayerPOVHitResult(event.getEntity()), event.getEntity());
        if(event.getItemStack().getItem() instanceof TestWand){
            event.getLevel().playSound(event.getEntity(), event.getPos(), ModSounds.EXPLOSION_ON_CAST.get(), SoundSource.BLOCKS, 1f, 1f);
        }

        if(result != null && !event.getEntity().level().isClientSide && event.getItemStack().getItem() instanceof TestWand){
            event.getEntity().displayClientMessage(Component.literal("test from rightclick §4entity"), false);
            event.getLevel().explode(
                    null,
                    result.getEntity().getX(),
                    result.getEntity().getY(),
                    result.getEntity().getZ(),
                    5F,
                    Level.ExplosionInteraction.TNT);
            event.getEntity().getCooldowns().addCooldown(event.getItemStack().getItem(), 10);
            return;
        }

        //explodes the target block
        if(event.getItemStack().getItem() instanceof TestWand && !event.getEntity().level().isClientSide){
            event.getEntity().displayClientMessage(Component.literal("test from rightclick"), false);
            double rayTraceDistance = 100D;
            HitResult hit = event.getEntity().pick(rayTraceDistance, 0, false);
            event.getLevel().explode(
                    null,
                    hit.getLocation().x,
                    hit.getLocation().y+0.5f,
                    hit.getLocation().z,
                    5F,
                    Level.ExplosionInteraction.TNT);



            event.getEntity().getCooldowns().addCooldown(event.getItemStack().getItem(), 10);
        }
    }

    public static LinkedList<Entity> getPlayerPOVHitResult(Player player) {
        LinkedList<Entity> returnList = new LinkedList<Entity>();

        float playerRotX = player.getXRot();
        float playerRotY = player.getYRot();

        Vec3 startPos = player.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float)Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float)Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = 10000D;
        Vec3 endVec = startPos.add((double)additionX * d0, (double)additionY * d0, (double)additionZ * d0);
        AABB startEndBox = new AABB(startPos, endVec);
        Entity entity = null;
        for(Entity entity1 : player.level().getEntities(player, startEndBox, (val) -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    returnList.add(entity1);
                    startPos = optional.orElse(startPos);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceTo(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == player.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            returnList.add(entity1);
                            startPos = vec31;
                        }
                    } else {
                        entity = entity1;
                        returnList.add(entity1);
                        startPos = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return returnList;
    }

    public static EntityHitResult getClosestEntity(LinkedList<Entity> entityList, Player player) {
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entityList) {
            player.displayClientMessage(Component.literal(String.valueOf(entity.getName())), false);
            double distance = player.distanceTo(entity);  // Calculate the distance between the player and the entity

            if (distance < closestDistance) {
                closestEntity = entity;
                closestDistance = distance;
            }
        }

        if(closestEntity == null) return null;
        else{
            return new EntityHitResult(closestEntity);
        }
    }

    @SubscribeEvent
    public void deathTest(LivingDeathEvent event){
        //if(event.getEntity().equals(PRYRadarEntity.radarTarget)){
        //    PRYRadarEntity.radarTarget = null;
        //}
    }

    @SubscribeEvent
    public void hurtEvent(LivingHurtEvent event){
        if(event.getSource().getEntity() instanceof ProximityArrowEntity arrowEntity){
            arrowEntity.entityOwner.inflight = false;
        }
    }

    @SubscribeEvent
    public void registerAdditional(ModelEvent.RegisterAdditional event){
        event.register(new ResourceLocation(PRYmod.MODID, "textures/block/prylauncher_0.png"));
        event.register(new ResourceLocation(PRYmod.MODID, "textures/block/prylauncher_6.png"));
    }

    @SubscribeEvent
    public void breakingBlock(BlockEvent.BreakEvent event){
        Block brokenBlock = event.getState().getBlock();
        if(brokenBlock instanceof PRYRadar){
            //PRYRadarEntity.radarTarget = null;
            //PRYRadarEntity.livingEntityLinkedList.clear();
            PRYRadarEntity.livingEntityRGBHashMap.clear();
            TestRenderer.previousPos.clear();
        }
        else if(brokenBlock instanceof PRYBlock){
        }
    }

}
