package net.shirojr.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.shirojr.init.LAKEntityTypes;
import net.shirojr.init.LAKTags;
import org.jspecify.annotations.Nullable;

public class SeatEntity extends BlockAttachedEntity {

    @Nullable
    private BlockPos attachedPos;
    private int checkInterval;


    public SeatEntity(EntityType<? extends BlockAttachedEntity> type, Level level) {
        super(type, level);
    }

    public SeatEntity(Level level, BlockPos pos) {
        this(LAKEntityTypes.SEAT, level);
        this.setAttachedPos(pos);
    }

    public void setAttachedPos(@Nullable BlockPos attachedPos) {
        this.attachedPos = attachedPos;
    }

    public @Nullable BlockPos getAttachedPos() {
        return attachedPos;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        this.setAttachedPos(BlockPos.containing(x, y, z));
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected boolean shouldPlayLavaHurtSound() {
        return false;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean skipAttackInteraction(Entity source) {
        return true;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void recalculateBoundingBox() {
        this.getDirection();
        AABB aabb = new AABB(pos);
        Vec3 center = aabb.getCenter();
        this.setPosRaw(center.x, center.y, center.z);
        this.setBoundingBox(aabb);
    }

    @Override
    public boolean survives() {
        if (this.getAttachedPos() == null) return true;
        return level().getBlockState(this.getAttachedPos()).is(LAKTags.BlockTags.SEAT_HOLDER);
    }

    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel level) {
            this.checkBelowWorld();
            if (this.checkInterval++ == 100) {
                this.checkInterval = 0;
                if (!this.isRemoved() && !this.survives()) {
                    this.discard();
                    this.dropItem(level, null);
                }
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    public void dropItem(ServerLevel level, @Nullable Entity causedBy) {

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

    }

    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
    }
}
