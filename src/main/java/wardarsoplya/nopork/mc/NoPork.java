package wardarsoplya.nopork.mc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.dimension.DimensionTypes;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;

import java.util.List;

public class NoPork implements ModInitializer {

	@Override
	public void onInitialize() {

		ConsumableComponent badConsumable = ConsumableComponent.builder()
				.consumeSeconds(1.6f)
				.consumeEffect(new ApplyEffectsConsumeEffect(
						List.of(
								new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0),
								new StatusEffectInstance(StatusEffects.POISON, 200, 0),
								new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 0)
						)
				))
				.build();

		DefaultItemComponentEvents.MODIFY.register(context -> {
			context.modify(
					Items.PORKCHOP,
					builder -> builder.add(
							DataComponentTypes.CONSUMABLE,
							badConsumable
					)
			);

			context.modify(
					Items.COOKED_PORKCHOP,
					builder -> builder.add(
							DataComponentTypes.CONSUMABLE,
							badConsumable
					)
			);
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {

			ItemStack stack = player.getStackInHand(hand);

			if (!stack.isOf(Items.PORKCHOP)
					&& !stack.isOf(Items.COOKED_PORKCHOP)) {
				return ActionResult.PASS;
			}

			if (world.isClient()) {
				return ActionResult.PASS;
			}

			if (world.random.nextInt(10) == 0) {

				ServerWorld serverWorld = (ServerWorld) world;

				LightningEntity lightning =
						new LightningEntity(
								net.minecraft.entity.EntityType.LIGHTNING_BOLT,
								serverWorld
						);

				lightning.setPosition(
						player.getX(),
						player.getY(),
						player.getZ()
				);

				serverWorld.spawnEntity(lightning);
			}

			if (world.random.nextInt(20) == 0
					&& player instanceof ServerPlayerEntity serverPlayer) {

				ServerWorld nether = ((ServerWorld) world)
						.getServer()
						.getWorld(World.NETHER);

				if (nether != null) {
					BlockPos pos = new BlockPos(0, 80, 0);

					serverPlayer.teleportTo(
							new TeleportTarget(
									nether,
									pos.toCenterPos(),
									serverPlayer.getVelocity(),
									serverPlayer.getYaw(),
									serverPlayer.getPitch(),
									TeleportTarget.NO_OP
							)
					);
				}
			}

			return ActionResult.PASS;
		});
	}
}