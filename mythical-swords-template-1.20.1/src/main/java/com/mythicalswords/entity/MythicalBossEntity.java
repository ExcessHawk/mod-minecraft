package com.mythicalswords.entity;

import com.mythicalswords.core.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public abstract class MythicalBossEntity extends HostileEntity {

    private final ServerBossBar bossBar;
    protected int currentPhase = 1;
    private static final float PHASE2_THRESHOLD = 0.60f;
    private static final float PHASE3_THRESHOLD = 0.30f;
    // Cleanup: remove dead boss entity after 5 minutes (6000 ticks)
    private static final int DEATH_CLEANUP_TICKS = 6000;
    private int deathTimer = -1;

    public MythicalBossEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.bossBar = new ServerBossBar(this.getDisplayName(),
            BossBar.Color.PURPLE, BossBar.Style.PROGRESS);
    }

    @Override
    public net.minecraft.entity.EntityData initialize(net.minecraft.world.ServerWorldAccess world,
            net.minecraft.world.LocalDifficulty difficulty,
            net.minecraft.entity.SpawnReason spawnReason,
            net.minecraft.entity.EntityData entityData, NbtCompound entityNbt) {
        // Apply config multipliers once, on first spawn (persisted in the
        // attribute base afterwards, so no double-scaling on world reload)
        var config = com.mythicalswords.config.ModConfig.get();
        var health = this.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MAX_HEALTH);
        if (health != null && config.bossHealthMultiplier != 1.0f) {
            health.setBaseValue(health.getBaseValue() * config.bossHealthMultiplier);
            this.setHealth(this.getMaxHealth());
        }
        var damage = this.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (damage != null && config.bossDamageMultiplier != 1.0f) {
            damage.setBaseValue(damage.getBaseValue() * config.bossDamageMultiplier);
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void mobTick() {
        super.mobTick();
        float healthPercent = this.getHealth() / this.getMaxHealth();
        this.bossBar.setPercent(healthPercent);
        checkPhaseTransition(healthPercent);
    }

    @Override
    public void tick() {
        super.tick();
        // Cleanup dead boss after timeout
        if (!this.isAlive()) {
            if (deathTimer < 0) deathTimer = 0;
            if (++deathTimer >= DEATH_CLEANUP_TICKS) {
                this.discard();
            }
        }
    }

    private void checkPhaseTransition(float healthPercent) {
        if (this.getWorld().isClient) return;
        if (currentPhase == 1 && healthPercent <= PHASE2_THRESHOLD) {
            transitionToPhase(2);
        } else if (currentPhase == 2 && healthPercent <= PHASE3_THRESHOLD) {
            transitionToPhase(3);
        }
    }

    private void transitionToPhase(int newPhase) {
        currentPhase = newPhase;
        onPhaseTransition(newPhase);

        // Visual: particle burst
        if (this.getWorld() instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                this.getX(), this.getY() + 1, this.getZ(), 3, 1, 1, 1, 0);
            sw.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING,
                this.getX(), this.getY() + 1, this.getZ(), 30, 0.5, 1, 0.5, 0.3);
        }

        // Sound
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
            ModSounds.BOSS_PHASE_TRANSITION, SoundCategory.HOSTILE, 2.0f, 1.0f);

        // Announce to nearby players
        String phaseName = newPhase == 2 ? "Phase II" : "Phase III — ENRAGE";
        Formatting color = newPhase == 2 ? Formatting.YELLOW : Formatting.RED;
        for (var player : this.getWorld().getPlayers()) {
            if (player.squaredDistanceTo(this) <= 2500) { // 50 blocks
                player.sendMessage(
                    Text.literal("[")
                        .append(this.getDisplayName())
                        .append(Text.literal("] — " + phaseName + "!").formatted(color)),
                    false);
            }
        }

        // Change boss bar color per phase
        setBossBarColor(newPhase == 2 ? BossBar.Color.YELLOW : BossBar.Color.RED);
    }

    /** Override in subclass for phase-specific behavior */
    protected void onPhaseTransition(int newPhase) {}

    public int getCurrentPhase() { return currentPhase; }

    @Override
    public void setCustomName(Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    protected void setBossBarColor(BossBar.Color color) {
        this.bossBar.setColor(color);
    }

    protected void setBossBarStyle(BossBar.Style style) {
        this.bossBar.setStyle(style);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("BossPhase", currentPhase);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        currentPhase = nbt.getInt("BossPhase");
        if (currentPhase < 1) currentPhase = 1;
    }
}
