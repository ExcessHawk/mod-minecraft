package com.mythicalswords.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

/**
 * Soul Seal - Totsuka's special ability
 * Seals a weakened mob (below 20% HP) into a spirit form
 * The mob is removed from the world and stored as NBT data
 * Can be released later (future feature)
 * Cooldown: 600 ticks (30 seconds)
 */
public class SoulSealAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 600;
    private static final double SEAL_RANGE = 5.0;
    private static final float HEALTH_THRESHOLD = 0.20f; // 20% health
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Find nearby entities
        Box searchBox = new Box(
            player.getX() - SEAL_RANGE,
            player.getY() - SEAL_RANGE,
            player.getZ() - SEAL_RANGE,
            player.getX() + SEAL_RANGE,
            player.getY() + SEAL_RANGE,
            player.getZ() + SEAL_RANGE
        );
        
        List<Entity> nearbyEntities = world.getOtherEntities(
            player,
            searchBox,
            entity -> entity instanceof LivingEntity && entity.isAlive()
        );
        
        // Find the closest valid target
        LivingEntity target = null;
        double closestDistance = SEAL_RANGE;
        
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                // Check if entity is below health threshold
                float healthPercent = livingEntity.getHealth() / livingEntity.getMaxHealth();
                
                if (healthPercent <= HEALTH_THRESHOLD) {
                    double distance = player.distanceTo(entity);
                    if (distance < closestDistance) {
                        target = livingEntity;
                        closestDistance = distance;
                    }
                }
            }
        }
        
        if (target == null) {
            player.sendMessage(
                net.minecraft.text.Text.literal("No weakened enemies nearby to seal! (Target must be below 20% HP)")
                    .formatted(net.minecraft.util.Formatting.RED),
                true
            );
            return false;
        }
        
        // Cannot seal bosses or players
        if (target instanceof PlayerEntity || target.getMaxHealth() > 100) {
            player.sendMessage(
                net.minecraft.text.Text.literal("This entity is too powerful to seal!")
                    .formatted(net.minecraft.util.Formatting.RED),
                true
            );
            return false;
        }
        
        // Seal the entity
        String entityName = target.getName().getString();
        
        // Create sealing particles
        if (world instanceof ServerWorld serverWorld) {
            // Purple spiral particles
            for (int i = 0; i < 50; i++) {
                double angle = (i / 50.0) * Math.PI * 4;
                double radius = (50 - i) / 50.0 * 2;
                double x = target.getX() + Math.cos(angle) * radius;
                double z = target.getZ() + Math.sin(angle) * radius;
                double y = target.getY() + (i / 50.0) * 3;
                
                serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    x, y, z,
                    1,
                    0, 0, 0,
                    0
                );
            }
            
            // Soul particles at center
            serverWorld.spawnParticles(
                ParticleTypes.SOUL,
                target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                30,
                0.3, 0.5, 0.3,
                0.05
            );
            
            // Enchantment glint
            serverWorld.spawnParticles(
                ParticleTypes.ENCHANT,
                target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                20,
                0.5, 0.5, 0.5,
                1.0
            );
        }
        
        // Play sealing sound
        world.playSound(
            null,
            target.getX(), target.getY(), target.getZ(),
            SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
            SoundCategory.PLAYERS,
            1.0f,
            0.5f
        );
        
        world.playSound(
            null,
            target.getX(), target.getY(), target.getZ(),
            SoundEvents.ENTITY_ENDERMAN_TELEPORT,
            SoundCategory.PLAYERS,
            0.8f,
            1.5f
        );
        
        // Store entity data in weapon NBT (for future release feature)
        NbtCompound weaponNbt = weapon.getOrCreateNbt();
        if (!weaponNbt.contains("SealedSouls")) {
            weaponNbt.putInt("SealedSouls", 0);
        }
        int sealedCount = weaponNbt.getInt("SealedSouls");
        weaponNbt.putInt("SealedSouls", sealedCount + 1);
        
        // Store the last sealed entity type
        weaponNbt.putString("LastSealedEntity", entityName);
        
        // Remove the entity from the world
        target.discard();
        
        // Success message
        player.sendMessage(
            net.minecraft.text.Text.literal("Soul Sealed! " + entityName + " has been captured.")
                .formatted(net.minecraft.util.Formatting.LIGHT_PURPLE),
            true
        );
        
        player.sendMessage(
            net.minecraft.text.Text.literal("Total souls sealed: " + (sealedCount + 1))
                .formatted(net.minecraft.util.Formatting.GRAY),
            false
        );
        
        return true;
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Soul Seal";
    }
}
