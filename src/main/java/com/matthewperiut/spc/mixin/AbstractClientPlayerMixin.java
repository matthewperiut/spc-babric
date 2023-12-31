package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.util.SPChatUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
    @Shadow
    protected Minecraft minecraft;

    @Inject(method = "sendChatMessage", at = @At("HEAD"))
    public void sendFeedbackInSP(String par1, CallbackInfo ci) {
        if (!((Minecraft) FabricLoader.getInstance().getGameInstance()).hasLevel()) // sp check
        {
            PlayerBase player = ((PlayerBase) (Object) this);

            if (par1.charAt(0) == '/') {
                // handle commands
                SPChatUtil.handleCommand(new SharedCommandSource(player), par1.substring(1));
            } else {
                ((Minecraft) FabricLoader.getInstance().getGameInstance()).overlay.addChatMessage("<" + player.name + "> " + par1);
            }
        }
    }
}
