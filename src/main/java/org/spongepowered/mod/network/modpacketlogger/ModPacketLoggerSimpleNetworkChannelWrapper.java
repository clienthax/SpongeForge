/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.network.modpacketlogger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.mod.mixin.core.fml.common.network.handshake.NetworkDispatcherMixin_Forge;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;

public class ModPacketLoggerSimpleNetworkChannelWrapper<REQ extends IMessage, REPLY extends IMessage> extends SimpleChannelHandlerWrapper<REQ, REPLY> {

    public ModPacketLoggerSimpleNetworkChannelWrapper(Class<? extends IMessageHandler<? super REQ, ? extends REPLY>> handler, Side side, Class<REQ> requestType) {
        super(handler, side, requestType);
        this.onInit(side);
    }

    public ModPacketLoggerSimpleNetworkChannelWrapper(IMessageHandler<? super REQ, ? extends REPLY> handler, Side side, Class<REQ> requestType) {
        super(handler, side, requestType);
        this.onInit(side);
    }

    private void onInit(Side side) {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, REQ msg) throws Exception {
            try {
                try {
                    SpongeImpl.getScheduler().submitAsyncTask(() -> {
                        final ByteBuf buffer = Unpooled.buffer();
                        msg.toBytes(buffer);
                        final EntityPlayerMP entityPlayerMP = (EntityPlayerMP) ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).get();
                        final String log = entityPlayerMP.getName() + " " + msg.getClass().getName() + " " + Arrays.toString(buffer.array());
                        FileUtils.writeStringToFile(new File("packetlogs/"+LocalDate.now()+"/"+entityPlayerMP.getName()+".txt"), log+"\n", true);
                        return null;
                    });
                } catch (Exception ignored) { }

                super.channelRead0(ctx, msg);
            } catch (Exception e) {
                throw new RuntimeException("Exception when invoking mod packet handler!", e);
            }
    }

}
