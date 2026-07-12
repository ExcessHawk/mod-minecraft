package com.mythicalswords.abilities;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

/**
 * Shield Reflection - Aegis Edge's special ability
 * Grants the player a damage reflection buff and resistance
 * Stores a marker in NBT to indicate the shield is active
 * The actual reflection is handled by the AffinityEventHandler
 */
public class ShieldReflectionAbility implements WeaponAbility {
    
    private static final int COOLDOWN_TICKS = 500; // 25 seconds
    private static final int DURATION_TICKS = 100; // 5 seconds
    public static final String NBT_SHIELD_ACTIVE = "ShieldReflectionActive";
    public static final String NBT_SHIELD_END_TIME = "ShieldReflectionEndTime";
    
    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack weapon) {
        if (world.isClient) {
            return false;
        }
        
        // Mark the weapon as having active shield reflection
        NbtCompound nbt = weapon.getOrCreateNbt();
        nbt.putBoolean(NBT_SHIELD_ACTIVE, true);
        nbt.putLong(NBT_SHIELD_END_TIME, world.getTime() + DURATION_TICKS);
        
        // Apply resistance and absorption effects to simulate shield
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.RESISTANCE,
            DURATION_TICKS,
            1, // Level 2 (40% damage reduction)
            false,
            true,
            true
        ));
        
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.ABSORPTION,
            DURATION_TICKS,
            1, // Level 2 (4 absorption hearts)
            false,
            true,
            true
        ));
        
        // Add glowing effect to show the shield is active
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.GLOWING,
            DURATION_TICKS,
            0,
            false,
            true,
            true
        ));
        
        // Spawn shield particles around the player
        if (world instanceof ServerWorld serverWorld) {
            // Create a protective barrier effect
            for (int i = 0; i < 60; i++) {
                double angle = (i / 60.0) * Math.PI * 2;
                double radius = 1.5;
                double x = player.getX() + Math.cos(angle) * radius;
                double z = player.getZ() + Math.sin(angle) * radius;
                double y = player.getY() + 1.0;
                
                serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    x, y, z,
                    2,
                    0.1, 0.3, 0.1,
                    0.02
                );
            }
            
            // Spawn additional protective particles
            serverWorld.spawnParticles(
                ParticleTypes.ENCHANTED_HIT,
                player.getX(), player.getY() + 1, player.getZ(),
                30,
                0.5, 1.0, 0.5,
                0.1
            );
            
            // Spawn a flash at activation
            serverWorld.spawnParticles(
                ParticleTypes.FLASH,
                player.getX(), player.getY() + 1, player.getZ(),
                1,
                0, 0, 0,
                0
            );
        }
        
        // Play shield activation sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.ITEM_SHIELD_BLOCK,
            SoundCategory.PLAYERS,
            1.0f,
            1.2f
        );
        
        // Play additional enchantment sound
        world.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
            SoundCategory.PLAYERS,
            0.8f,
            1.5f
        );
        
        return true;
    }
    
    @Override
    public int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }
    
    @Override
    public String getName() {
        return "Shield Reflection";
    }
    
    /**
     * Check if shield reflection is currently active on a weapon
     * 
     * @param weapon The weapon to check
     * @param world The world (for time checking)
     * @return true if shield is active
     */
    public static boolean isShieldActive(ItemStack weapon, World world) {
        NbtCompound nbt = weapon.getNbt();
        if (nbt == null || !nbt.contains(NBT_SHIELD_ACTIVE)) {
            return false;
        }
        
        boolean active = nbt.getBoolean(NBT_SHIELD_ACTIVE);
        long endTime = nbt.getLong(NBT_SHIELD_END_TIME);
        
        // Check if the shield has expired
        if (world.getTime() >= endTime) {
            nbt.putBoolean(NBT_SHIELD_ACTIVE, false);
            return false;
        }
        
        return active;
    }
}
