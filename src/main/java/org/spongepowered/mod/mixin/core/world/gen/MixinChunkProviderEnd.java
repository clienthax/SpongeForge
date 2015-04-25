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
package org.spongepowered.mod.mixin.core.world.gen;

import net.minecraft.world.chunk.IChunkProvider;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkProviderEnd.class)
public abstract class MixinChunkProviderEnd implements IChunkProvider {

    @Shadow private int chunkX;
    @Shadow private int chunkZ;
    @Shadow private World endWorld;

    /**
     * We overwrite this function with just the forge event call as the
     * functionality of replacing the stone blocks with end stone has been moved
     * to a genpop (EndBiomeGeneratorPopulator) attached to BiomeGenEnd.
     * 
     * This method should have overwrite priority over an identical overwrite
     * (sans the forge event) in common.
     * 
     * @author Deamon
     */
    @Overwrite
    public void func_180519_a(ChunkPrimer primer) {
        ReplaceBiomeBlocks event = new ReplaceBiomeBlocks(this, this.chunkX, this.chunkZ, primer, this.endWorld);
        MinecraftForge.EVENT_BUS.post(event);

        // BEGIN removed code

//        if (event.getResult() == Result.DENY) return;
//        for (int i = 0; i < 16; ++i)
//        {
//            for (int j = 0; j < 16; ++j)
//            {
//                byte b0 = 1;
//                int k = -1;
//                IBlockState iblockstate = Blocks.end_stone.getDefaultState();
//                IBlockState iblockstate1 = Blocks.end_stone.getDefaultState();
//
//                for (int l = 127; l >= 0; --l)
//                {
//                    IBlockState iblockstate2 = primer.getBlockState(i, l, j);
//
//                    if (iblockstate2.getBlock().getMaterial() == Material.air)
//                    {
//                        k = -1;
//                    }
//                    else if (iblockstate2.getBlock() == Blocks.stone)
//                    {
//                        if (k == -1)
//                        {
//                            if (b0 <= 0)
//                            {
//                                iblockstate = Blocks.air.getDefaultState();
//                                iblockstate1 = Blocks.end_stone.getDefaultState();
//                            }
//
//                            k = b0;
//
//                            if (l >= 0)
//                            {
//                                primer.setBlockState(i, l, j, iblockstate);
//                            }
//                            else
//                            {
//                                primer.setBlockState(i, l, j, iblockstate1);
//                            }
//                        }
//                        else if (k > 0)
//                        {
//                            --k;
//                            primer.setBlockState(i, l, j, iblockstate1);
//                        }
//                    }
//                }
//            }
//        }

        // END removed code
    }

}
