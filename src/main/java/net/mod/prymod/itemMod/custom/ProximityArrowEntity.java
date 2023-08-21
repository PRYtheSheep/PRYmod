package net.mod.prymod.itemMod.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;
import net.mod.prymod.ModBlock.PRYBlockEntity;

@SuppressWarnings("LanguageDetectionInspection")
public class ProximityArrowEntity extends AbstractArrow {

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public ProximityArrowEntity(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    protected ProximityArrowEntity(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_, PRYBlockEntity entity) {
        super(p_36721_, p_36722_);
        this.entityOwner = entity;
    }

    protected ProximityArrowEntity(EntityType<? extends AbstractArrow> p_36711_, double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
        super(p_36711_, p_36712_, p_36713_, p_36714_, p_36715_);
    }

    protected ProximityArrowEntity(EntityType<? extends AbstractArrow> p_36717_, LivingEntity p_36718_, Level p_36719_) {
        super(p_36717_, p_36718_, p_36719_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }


    //boolean blockHit = false;
    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        //blockHit = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        super.onHitEntity(p_36757_);
    }

    public void setEntityOwner(PRYBlockEntity entityOwner) { this.entityOwner = entityOwner; }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void setTarget(LivingEntity target) { this.target = target; }

    public PRYBlockEntity entityOwner = null;
    int progress = 0;
    public Entity target = null;
    public Vec3 previousPosition = null;

    @Override
    public void tick() {
        if(this.level().isClientSide) return;
        super.tick();
        Player player = this.level().getNearestPlayer(this, 50);

        if(target == null){
            this.kill();
            return;
        }


        Vec3 resultantVector = new Vec3((target.getEyePosition().x - this.getX()),
                (target.getEyePosition().y - this.getY()),
                (target.getEyePosition().z - this.getZ()));

        if(target != null){
            Vec3 currentPos = new Vec3(this.getX(), this.getY(), this.getZ());
            Vec3 particlePosDifference = this.getDeltaMovement().scale(-1);
            if(particlePosDifference.length() > 1){
                particlePosDifference = particlePosDifference.scale(1/particlePosDifference.length());
            }
            Vec3 particlePos = currentPos.add(particlePosDifference.scale(3));
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0, particlePosDifference.x, particlePosDifference.y,particlePosDifference.z, 1);
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0, particlePosDifference.x, particlePosDifference.y,particlePosDifference.z, 1.1);
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0, particlePosDifference.x, particlePosDifference.y,particlePosDifference.z, 0.9);
        }

        if(!this.level().isClientSide() && target != null && progress <= 1){
            this.level().playSound(this, this.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 10f, 10f);

            this.setDeltaMovement(this.getDeltaMovement().normalize());
            progress++;
            return;
        }

        if(target != null){

            this.setDeltaMovement(this.getDeltaMovement().add(resultantVector.normalize().scale(0.8)).normalize());

            if(resultantVector.length() <= 2 && !this.level().isClientSide && entityOwner != null){
                this.level().explode(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        3F,
                        Level.ExplosionInteraction.NONE);
                this.kill();

                entityOwner.inflight = false;
                target = null;
                if(player != null){
                    player.displayClientMessage(Component.literal("§cMissile detonated"), true);
                }
            }
            if(progress > 200){
                this.kill();
                entityOwner.inflight = false;
                target = null;
                if(player != null){
                    player.displayClientMessage(Component.literal("§eMissile lost"), true);
                }
            }

            if(previousPosition == null){
                previousPosition = new Vec3(this.getX(), this.getY(), this.getZ());
            }
            else{
                Vec3 currentPosition = new Vec3(this.getX(), this.getY(), this.getZ());
                if(previousPosition.equals(currentPosition)){
                    this.level().explode(
                            null,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            3F,
                            Level.ExplosionInteraction.NONE);
                    this.kill();

                    entityOwner.inflight = false;
                    target = null;
                    if(player != null){
                        player.displayClientMessage(Component.literal("§cMissile detonated"), true);
                    }
                }
                else{
                    previousPosition = currentPosition;
                }
            }

            progress++;
        }
    }

    public boolean isFoil() {
        return false;
    }

    private Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = (float) Math.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = (float) Math.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = (float) -Math.cos(-pitch * 0.017453292F);
        float f3 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vec3(f1 * f2 * -1, f3, f * f2);
    }
}
