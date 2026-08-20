package wardarsoplya.nopork.mc.client;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

import java.util.List;

public class NoPork implements ModInitializer {
	@Override
	public void onInitialize() {
		DefaultItemComponentEvents.MODIFY.register(context -> {
			ConsumableComponent badConsumable = ConsumableComponent.builder()
					.consumeSeconds(1.6f)
					.consumeEffect(new ApplyEffectsConsumeEffect(
							List.of(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0))
					))
					.build();

			context.modify(Items.PORKCHOP, builder -> builder.add(DataComponentTypes.CONSUMABLE, badConsumable));
			context.modify(Items.COOKED_PORKCHOP, builder -> builder.add(DataComponentTypes.CONSUMABLE, badConsumable));
		});
	}
}